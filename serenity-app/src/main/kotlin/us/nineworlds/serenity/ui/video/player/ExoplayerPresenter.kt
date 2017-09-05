package us.nineworlds.serenity.ui.video.player

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import us.nineworlds.plex.rest.PlexappFactory
import us.nineworlds.serenity.common.annotations.OpenForTesting
import us.nineworlds.serenity.common.injection.SerenityObjectGraph
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.ui.video.player.ExoplayerContract.ExoplayerPresenter
import java.util.LinkedList
import javax.inject.Inject

@OpenForTesting
@InjectViewState
class ExoplayerPresenter : MvpPresenter<ExoplayerContract.ExoplayerView>(), ExoplayerPresenter {

  @field:[Inject ForVideoQueue]
  internal lateinit var videoQueue: LinkedList<VideoContentInfo>

  @Inject
  internal lateinit var plexFactory: PlexappFactory

  internal lateinit var video: VideoContentInfo

  init {
    SerenityObjectGraph.instance.inject(this)
  }

  override fun playBackFromVideoQueue() {
    if (videoQueue.isEmpty()) {
      return
    }

    this.video = videoQueue.poll()

    val videoUrl: String = transcoderUrl()

    viewState.initializePlayer(videoUrl)
  }

  internal fun isDirectPlaySupportedForContainer(video: VideoContentInfo): Boolean {

    val isAudioCodecSupported = selectCodec("audio/" + video.audioCodec)

    Log.d("ExoPlayerPresenter", "Audio Codec:  " + video.audioCodec + " support returned " + isAudioCodecSupported)

    if (video.container.equals("avi") || !isAudioCodecSupported) {
      return false
    }
    return true
  }

  private fun transcoderUrl(): String {
    if (isDirectPlaySupportedForContainer(video)) {
      return video.directPlayUrl
    }

    return plexFactory.getTranscodeUrl(video.id(), video.resumeOffset)
  }

  private fun selectCodec(mimeType: String): Boolean {
    var actualCode = mimeType;
    if (actualCode.contains("h264")) {
      actualCode = "video/H264"
    }
    val codec = MediaCodecUtil.getDecoderInfo(actualCode, false) ?: return false
    return codec.isCodecSupported(mimeType)
  }
}