/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.nineworlds.serenity.ui.video.player;

import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.GeobFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.id3.UrlLinkFrame;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.inject.Inject;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.core.logger.Logger;

/**
 * Logs player events using {@link Log}.
 */
public final class EventLogger implements Player.EventListener, AudioRendererEventListener, VideoRendererEventListener,
    AdaptiveMediaSourceEventListener, ExtractorMediaSource.EventListener, DefaultDrmSessionManager.EventListener,
    MetadataRenderer.Output {

  private static final int MAX_TIMELINE_ITEM_LINES = 3;
  private static final NumberFormat TIME_FORMAT;

  static {
    TIME_FORMAT = NumberFormat.getInstance(Locale.US);
    TIME_FORMAT.setMinimumFractionDigits(2);
    TIME_FORMAT.setMaximumFractionDigits(2);
    TIME_FORMAT.setGroupingUsed(false);
  }

  private final MappingTrackSelector trackSelector;
  private final Timeline.Window window;
  private final Timeline.Period period;
  private final long startTimeMs;

  @Inject Logger logger;

  public EventLogger(MappingTrackSelector trackSelector) {
    this.trackSelector = trackSelector;
    window = new Timeline.Window();
    period = new Timeline.Period();
    startTimeMs = SystemClock.elapsedRealtime();
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  // Player.EventListener

  @Override public void onLoadingChanged(boolean isLoading) {
    logger.debug("loading [" + isLoading + "]");
  }

  @Override public void onPlayerStateChanged(boolean playWhenReady, int state) {
    logger.debug("state [" + getSessionTimeString() + ", " + playWhenReady + ", " + getStateString(state) + "]");
  }

  @Override public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
    logger.debug("repeatMode [" + getRepeatModeString(repeatMode) + "]");
  }

  @Override public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

  }

  public void onPositionDiscontinuity() {
    logger.debug("positionDiscontinuity");
  }

  @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    logger.debug("playbackParameters " + String.format("[speed=%.2f, pitch=%.2f]", playbackParameters.speed,
        playbackParameters.pitch));
  }

  @Override public void onSeekProcessed() {

  }

  public void onTimelineChanged(Timeline timeline, Object manifest) {
    int periodCount = timeline.getPeriodCount();
    int windowCount = timeline.getWindowCount();
    logger.debug("sourceInfo [periodCount=" + periodCount + ", windowCount=" + windowCount);
    for (int i = 0; i < Math.min(periodCount, MAX_TIMELINE_ITEM_LINES); i++) {
      timeline.getPeriod(i, period);
      logger.debug("  " + "period [" + getTimeString(period.getDurationMs()) + "]");
    }
    if (periodCount > MAX_TIMELINE_ITEM_LINES) {
      logger.debug("  ...");
    }
    for (int i = 0; i < Math.min(windowCount, MAX_TIMELINE_ITEM_LINES); i++) {
      timeline.getWindow(i, window);
      logger.debug("  "
          + "window ["
          + getTimeString(window.getDurationMs())
          + ", "
          + window.isSeekable
          + ", "
          + window.isDynamic
          + "]");
    }
    if (windowCount > MAX_TIMELINE_ITEM_LINES) {
      logger.debug("  ...");
    }
    logger.debug("]");
  }

  @Override public void onPlayerError(ExoPlaybackException e) {
    logger.error("playerFailed [" + getSessionTimeString() + "]", e);
  }

  @Override public void onPositionDiscontinuity(int reason) {

  }

  @Override public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

  }

  @Override public void onTracksChanged(TrackGroupArray ignored, TrackSelectionArray trackSelections) {
    MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
    if (mappedTrackInfo == null) {
      logger.debug("Tracks []");
      return;
    }
    logger.debug("Tracks [");
    // Log tracks associated to renderers.
    for (int rendererIndex = 0; rendererIndex < mappedTrackInfo.length; rendererIndex++) {
      TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
      TrackSelection trackSelection = trackSelections.get(rendererIndex);
      if (rendererTrackGroups.length > 0) {
        logger.debug("  Renderer:" + rendererIndex + " [");
        for (int groupIndex = 0; groupIndex < rendererTrackGroups.length; groupIndex++) {
          TrackGroup trackGroup = rendererTrackGroups.get(groupIndex);
          String adaptiveSupport = getAdaptiveSupportString(trackGroup.length,
              mappedTrackInfo.getAdaptiveSupport(rendererIndex, groupIndex, false));
          logger.debug("    Group:" + groupIndex + ", adaptive_supported=" + adaptiveSupport + " [");
          for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
            String status = getTrackStatusString(trackSelection, trackGroup, trackIndex);
            String formatSupport =
                getFormatSupportString(mappedTrackInfo.getTrackFormatSupport(rendererIndex, groupIndex, trackIndex));
            logger.debug("      "
                + status
                + " Track:"
                + trackIndex
                + ", "
                + Format.toLogString(trackGroup.getFormat(trackIndex))
                + ", supported="
                + formatSupport);
          }
          logger.debug("    ]");
        }
        // Log metadata for at most one of the tracks selected for the renderer.
        if (trackSelection != null) {
          for (int selectionIndex = 0; selectionIndex < trackSelection.length(); selectionIndex++) {
            Metadata metadata = trackSelection.getFormat(selectionIndex).metadata;
            if (metadata != null) {
              logger.debug("    Metadata [");
              printMetadata(metadata, "      ");
              logger.debug("    ]");
              break;
            }
          }
        }
        logger.debug("  ]");
      }
    }
    // Log tracks not associated with a renderer.
    TrackGroupArray unassociatedTrackGroups = mappedTrackInfo.getUnassociatedTrackGroups();
    if (unassociatedTrackGroups.length > 0) {
      logger.debug("  Renderer:None [");
      for (int groupIndex = 0; groupIndex < unassociatedTrackGroups.length; groupIndex++) {
        logger.debug("    Group:" + groupIndex + " [");
        TrackGroup trackGroup = unassociatedTrackGroups.get(groupIndex);
        for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
          String status = getTrackStatusString(false);
          String formatSupport = getFormatSupportString(RendererCapabilities.FORMAT_UNSUPPORTED_TYPE);
          logger.debug("      "
              + status
              + " Track:"
              + trackIndex
              + ", "
              + Format.toLogString(trackGroup.getFormat(trackIndex))
              + ", supported="
              + formatSupport);
        }
        logger.debug("    ]");
      }
      logger.debug("  ]");
    }
    logger.debug("]");
  }

  // MetadataRenderer.Output

  @Override public void onMetadata(Metadata metadata) {
    logger.debug("onMetadata [");
    printMetadata(metadata, "  ");
    logger.debug("]");
  }

  // AudioRendererEventListener

  @Override public void onAudioEnabled(DecoderCounters counters) {
    logger.debug("audioEnabled [" + getSessionTimeString() + "]");
  }

  @Override public void onAudioSessionId(int audioSessionId) {
    logger.debug("audioSessionId [" + audioSessionId + "]");
  }

  @Override
  public void onAudioDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
    logger.debug("audioDecoderInitialized [" + getSessionTimeString() + ", " + decoderName + "]");
  }

  @Override public void onAudioInputFormatChanged(Format format) {
    logger.debug("audioFormatChanged [" + getSessionTimeString() + ", " + Format.toLogString(format) + "]");
  }

  @Override public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

  }

  @Override public void onAudioDisabled(DecoderCounters counters) {
    logger.debug("audioDisabled [" + getSessionTimeString() + "]");
  }

  // VideoRendererEventListener

  @Override public void onVideoEnabled(DecoderCounters counters) {
    logger.debug("videoEnabled [" + getSessionTimeString() + "]");
  }

  @Override
  public void onVideoDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
    logger.debug("videoDecoderInitialized [" + getSessionTimeString() + ", " + decoderName + "]");
  }

  @Override public void onVideoInputFormatChanged(Format format) {
    logger.debug("videoFormatChanged [" + getSessionTimeString() + ", " + Format.toLogString(format) + "]");
  }

  @Override public void onVideoDisabled(DecoderCounters counters) {
    logger.debug("videoDisabled [" + getSessionTimeString() + "]");
  }

  @Override public void onDroppedFrames(int count, long elapsed) {
    logger.debug("droppedFrames [" + getSessionTimeString() + ", " + count + "]");
  }

  @Override
  public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
    logger.debug("videoSizeChanged [" + width + ", " + height + "]");
  }

  @Override public void onRenderedFirstFrame(Surface surface) {
    logger.debug("renderedFirstFrame [" + surface + "]");
  }

  // DefaultDrmSessionManager.EventListener

  @Override public void onDrmSessionManagerError(Exception e) {
    printInternalError("drmSessionManagerError", e);
  }

  @Override public void onDrmKeysRestored() {
    logger.debug("drmKeysRestored [" + getSessionTimeString() + "]");
  }

  @Override public void onDrmKeysRemoved() {
    logger.debug("drmKeysRemoved [" + getSessionTimeString() + "]");
  }

  @Override public void onDrmKeysLoaded() {
    logger.debug("drmKeysLoaded [" + getSessionTimeString() + "]");
  }

  // ExtractorMediaSource.EventListener

  @Override public void onLoadError(IOException error) {
    printInternalError("loadError", error);
  }

  // Internal methods

  private void printInternalError(String type, Exception e) {
    logger.error("internalError [" + getSessionTimeString() + ", " + type + "]", e);
  }

  private void printMetadata(Metadata metadata, String prefix) {
    for (int i = 0; i < metadata.length(); i++) {
      Metadata.Entry entry = metadata.get(i);
      if (entry instanceof TextInformationFrame) {
        TextInformationFrame textInformationFrame = (TextInformationFrame) entry;
        logger.debug(prefix + String.format("%s: value=%s", textInformationFrame.id, textInformationFrame.value));
      } else if (entry instanceof UrlLinkFrame) {
        UrlLinkFrame urlLinkFrame = (UrlLinkFrame) entry;
        logger.debug(prefix + String.format("%s: url=%s", urlLinkFrame.id, urlLinkFrame.url));
      } else if (entry instanceof PrivFrame) {
        PrivFrame privFrame = (PrivFrame) entry;
        logger.debug(prefix + String.format("%s: owner=%s", privFrame.id, privFrame.owner));
      } else if (entry instanceof GeobFrame) {
        GeobFrame geobFrame = (GeobFrame) entry;
        logger.debug(
            prefix + String.format("%s: mimeType=%s, filename=%s, description=%s", geobFrame.id, geobFrame.mimeType,
                geobFrame.filename, geobFrame.description));
      } else if (entry instanceof ApicFrame) {
        ApicFrame apicFrame = (ApicFrame) entry;
        logger.debug(prefix + String.format("%s: mimeType=%s, description=%s", apicFrame.id, apicFrame.mimeType,
            apicFrame.description));
      } else if (entry instanceof CommentFrame) {
        CommentFrame commentFrame = (CommentFrame) entry;
        logger.debug(prefix + String.format("%s: language=%s, description=%s", commentFrame.id, commentFrame.language,
            commentFrame.description));
      } else if (entry instanceof Id3Frame) {
        Id3Frame id3Frame = (Id3Frame) entry;
        logger.debug(prefix + String.format("%s", id3Frame.id));
      } else if (entry instanceof EventMessage) {
        EventMessage eventMessage = (EventMessage) entry;
        logger.debug(
            prefix + String.format("EMSG: scheme=%s, id=%d, value=%s", eventMessage.schemeIdUri, eventMessage.id,
                eventMessage.value));
      }
    }
  }

  private String getSessionTimeString() {
    return getTimeString(SystemClock.elapsedRealtime() - startTimeMs);
  }

  private static String getTimeString(long timeMs) {
    return timeMs == C.TIME_UNSET ? "?" : TIME_FORMAT.format((timeMs) / 1000f);
  }

  private static String getStateString(int state) {
    switch (state) {
      case Player.STATE_BUFFERING:
        return "B";
      case Player.STATE_ENDED:
        return "E";
      case Player.STATE_IDLE:
        return "I";
      case Player.STATE_READY:
        return "R";
      default:
        return "?";
    }
  }

  private static String getFormatSupportString(int formatSupport) {
    switch (formatSupport) {
      case RendererCapabilities.FORMAT_HANDLED:
        return "YES";
      case RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES:
        return "NO_EXCEEDS_CAPABILITIES";
      case RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE:
        return "NO_UNSUPPORTED_TYPE";
      case RendererCapabilities.FORMAT_UNSUPPORTED_TYPE:
        return "NO";
      default:
        return "?";
    }
  }

  private static String getAdaptiveSupportString(int trackCount, int adaptiveSupport) {
    if (trackCount < 2) {
      return "N/A";
    }
    switch (adaptiveSupport) {
      case RendererCapabilities.ADAPTIVE_SEAMLESS:
        return "YES";
      case RendererCapabilities.ADAPTIVE_NOT_SEAMLESS:
        return "YES_NOT_SEAMLESS";
      case RendererCapabilities.ADAPTIVE_NOT_SUPPORTED:
        return "NO";
      default:
        return "?";
    }
  }

  private static String getTrackStatusString(TrackSelection selection, TrackGroup group, int trackIndex) {
    return getTrackStatusString(
        selection != null && selection.getTrackGroup() == group && selection.indexOf(trackIndex) != C.INDEX_UNSET);
  }

  private static String getTrackStatusString(boolean enabled) {
    return enabled ? "[X]" : "[ ]";
  }

  private static String getRepeatModeString(@Player.RepeatMode int repeatMode) {
    switch (repeatMode) {
      case Player.REPEAT_MODE_OFF:
        return "OFF";
      case Player.REPEAT_MODE_ONE:
        return "ONE";
      case Player.REPEAT_MODE_ALL:
        return "ALL";
      default:
        return "?";
    }
  }

  @Override public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

  }

  @Override public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

  }

  @Override public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId,
      LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

  }

  @Override public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId,
      LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

  }

  @Override public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId,
      LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

  }

  @Override public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId,
      LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData,
      IOException error, boolean wasCanceled) {
    printInternalError("loadError", error);
  }

  @Override public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

  }

  @Override public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId,
      MediaLoadData mediaLoadData) {

  }

  @Override public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId,
      MediaLoadData mediaLoadData) {

  }
}