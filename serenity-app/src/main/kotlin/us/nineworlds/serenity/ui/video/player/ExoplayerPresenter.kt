package us.nineworlds.serenity.ui.video.player

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import us.nineworlds.plex.rest.PlexappFactory
import us.nineworlds.serenity.OpenForTesting
import us.nineworlds.serenity.common.injection.SerenityObjectGraph
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo
import us.nineworlds.serenity.injection.ForVideoQueue
import java.util.LinkedList
import javax.inject.Inject

@OpenForTesting
@InjectViewState
class ExoplayerPresenter : MvpPresenter<ExoplayerContract.ExoplayerView>() {

  @field:[Inject ForVideoQueue]
  lateinit var videoQueue: LinkedList<VideoContentInfo>

  @Inject lateinit var plexFactory: PlexappFactory

  lateinit var videoURL: String
  lateinit var video: VideoContentInfo
  lateinit var posterURL: String

  var subtitleURL: String? = null
  var subtitleType: String? = null

  init {
    SerenityObjectGraph.instance.inject(this)
  }

  fun playBackFromVideoQueue() {
    if (videoQueue.isEmpty()) {
      return
    }

    this.video = videoQueue.poll()
    videoURL = video.directPlayUrl
    if (video is EpisodePosterInfo) {
      if (video.getParentPosterURL() != null) {
        posterURL = video.getParentPosterURL()
      }
    }
    if (video.subtitle != null && "none" != video.subtitle.format) {
      subtitleURL = video.subtitle.key
      subtitleType = video.subtitle.format
    }

    val videoUrl: String = transcoderUrl()

    viewState.initializePlayer(videoUrl)
  }

  fun isDirectPlaySupportedForContainer(video: VideoContentInfo): Boolean {

    val isAudioCodecSupported = selectCodec("audio/" + video.audioCodec)

    Log.d("ExoPlayerPresenter", "Audio Codec:  " + video.audioCodec + " support returned " + isAudioCodecSupported)

    if (video.container.equals("avi") || !isAudioCodecSupported) {
      return false
    }
    return true
  }

  fun transcoderUrl(): String {
    if (isDirectPlaySupportedForContainer(video)) {
      return video.directPlayUrl
    }

    return plexFactory.getTranscodeUrl(video.id(), video.resumeOffset)
  }

  fun selectCodec(mimeType: String): Boolean {
    var actualCode = mimeType;
    if (actualCode.contains("h264")) {
      actualCode = "video/H264"
    }
    val codec = MediaCodecUtil.getDecoderInfo(actualCode, false) ?: return false
    return codec.isCodecSupported(mimeType)
  }
}