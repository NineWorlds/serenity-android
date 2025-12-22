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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.common.Metadata;
import androidx.media3.exoplayer.metadata.MetadataOutput;
import androidx.media3.extractor.metadata.emsg.EventMessage;
import androidx.media3.extractor.metadata.id3.ApicFrame;
import androidx.media3.extractor.metadata.id3.CommentFrame;
import androidx.media3.extractor.metadata.id3.GeobFrame;
import androidx.media3.extractor.metadata.id3.Id3Frame;
import androidx.media3.extractor.metadata.id3.PrivFrame;
import androidx.media3.extractor.metadata.id3.TextInformationFrame;
import androidx.media3.extractor.metadata.id3.UrlLinkFrame;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;
import androidx.media3.exoplayer.video.VideoRendererEventListener;

import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.core.logger.Logger;

/**
 * Logs player events using {@link Log}.
 */
@UnstableApi
public final class EventLogger implements AudioRendererEventListener, VideoRendererEventListener,
        MediaSourceEventListener, MetadataOutput {

  private static final int MAX_TIMELINE_ITEM_LINES = 3;
  private static final NumberFormat TIME_FORMAT;

  static {
    TIME_FORMAT = NumberFormat.getInstance(Locale.US);
    TIME_FORMAT.setMinimumFractionDigits(2);
    TIME_FORMAT.setMaximumFractionDigits(2);
    TIME_FORMAT.setGroupingUsed(false);
  }

  private final MappingTrackSelector trackSelector;
  private final long startTimeMs;

  @Inject Logger logger;

  public EventLogger(MappingTrackSelector trackSelector) {
    this.trackSelector = trackSelector;
    startTimeMs = SystemClock.elapsedRealtime();
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
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

  @Override
  public void onAudioDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
    logger.debug("audioDecoderInitialized [" + getSessionTimeString() + ", " + decoderName + "]");
  }

  @Override public void onAudioInputFormatChanged(Format format, @Nullable DecoderReuseEvaluation decoderReuseEvaluation) {
    logger.debug("audioFormatChanged [" + getSessionTimeString() + ", " + Format.toLogString(format) + "]");
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

  @Override public void onVideoInputFormatChanged(Format format, @Nullable DecoderReuseEvaluation decoderReuseEvaluation) {
    logger.debug("videoFormatChanged [" + getSessionTimeString() + ", " + Format.toLogString(format) + "]");
  }

  @Override public void onVideoDisabled(DecoderCounters counters) {
    logger.debug("videoDisabled [" + getSessionTimeString() + "]");
  }

  @Override public void onDroppedFrames(int count, long elapsed) {
    logger.debug("droppedFrames [" + getSessionTimeString() + ", " + count + "]");
  }

  // Internal methods
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
}
