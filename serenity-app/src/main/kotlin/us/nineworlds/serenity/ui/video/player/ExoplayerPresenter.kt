package us.nineworlds.serenity.ui.video.player

import android.view.View
import com.birbit.android.jobqueue.JobManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import toothpick.Toothpick
import us.nineworlds.serenity.common.android.mediacodec.MediaCodecInfoUtil
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.annotations.OpenForTesting
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.events.video.OnScreenDisplayEvent
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.jobs.video.StartPlaybackJob
import us.nineworlds.serenity.jobs.video.StopPlaybackJob
import us.nineworlds.serenity.jobs.video.UpdatePlaybackPostionJob
import us.nineworlds.serenity.jobs.video.WatchedStatusJob
import us.nineworlds.serenity.ui.video.player.ExoplayerContract.ExoplayerPresenter
import us.nineworlds.serenity.ui.video.player.ExoplayerContract.ExoplayerView
import java.util.LinkedList
import javax.inject.Inject

@OpenForTesting
@InjectViewState
@StateStrategyType(SkipStrategy::class)
class ExoplayerPresenter : MvpPresenter<ExoplayerView>(), ExoplayerPresenter,
  PlayerControlView.VisibilityListener, Player.EventListener {

  @Inject
  lateinit var logger: Logger

  @field:[Inject ForVideoQueue]
  internal lateinit var videoQueue: LinkedList<VideoContentInfo>

  @Inject
  internal lateinit var serenityClient: SerenityClient

  @Inject
  internal lateinit var eventBus: EventBus

  @Inject
  internal lateinit var jobManager: JobManager

  @Inject
  internal lateinit var androidHelper: AndroidHelper

  internal lateinit var video: VideoContentInfo

  private var onScreenControllerShowing: Boolean = false

  override fun attachView(view: ExoplayerView?) {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
    super.attachView(view)
    eventBus.register(this)
  }

  override fun detachView(view: ExoplayerView?) {
    super.detachView(view)
    eventBus.unregister(this)
  }

  override fun updateWatchedStatus() {
    jobManager.addJobInBackground(WatchedStatusJob(video.id()))
  }

  override fun onSeekProcessed() = Unit

  override fun onPositionDiscontinuity(reason: Int) = Unit

  override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) = Unit

  override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    if (Player.STATE_ENDED == playbackState) {
      stopPlaying(video.resumeOffset.toLong())
      viewState.playbackEnded()
      return
    }
  }

  override fun onLoadingChanged(isLoading: Boolean) = Unit

  override fun onRepeatModeChanged(repeatMode: Int) = Unit

  override fun videoId(): String = video.id()

  override fun stopPlaying(currentPosition: Long) {
    jobManager.addJobInBackground(StopPlaybackJob(video.id(), currentPosition))
  }

  override fun startPlaying() {
    jobManager.addJobInBackground(StartPlaybackJob(video.id()))
  }

  override fun onVisibilityChange(visibility: Int) {
    if (visibility == View.GONE) {
      logger.debug("Controller View was hidden")
      onScreenControllerShowing = false
    }

    if (visibility == View.VISIBLE) {
      logger.debug("Controller View was shown")
      onScreenControllerShowing = true
    }
  }

  override fun isHudShowing(): Boolean = onScreenControllerShowing

  @Subscribe(threadMode = MAIN)
  fun onOnScreenDisplayEvent(event: OnScreenDisplayEvent) {
    if (event.isShowing) {
      viewState.hideController()
    } else {
      viewState.showController()
    }
  }

  override fun updateServerPlaybackPosition(currentPostion: Long) {
    video.resumeOffset = currentPostion.toInt()
    jobManager.addJobInBackground(UpdatePlaybackPostionJob(video))
  }

  override fun playBackFromVideoQueue(autoResume: Boolean) {
    if (videoQueue.isEmpty()) {
      return
    }

    this.video = videoQueue.poll()

    if (!autoResume && video.isPartiallyWatched) {
      viewState.showResumeDialog(video)
      return
    }

    playVideo()
  }

  override fun playVideo() {
    val videoUrl: String = transcoderUrl()

    viewState.initializePlayer(videoUrl, video.resumeOffset)
  }

  internal fun isDirectPlaySupportedForContainer(video: VideoContentInfo): Boolean {
    val mediaCodecInfoUtil = MediaCodecInfoUtil()
    video.container?.let {
      video.container = if (video.container.contains("mp4")) {
        "mp4"
      } else {
        video.container
      }
      val isVideoContainerSupported =
        mediaCodecInfoUtil.isExoPlayerContainerSupported("video/${video.container.substringBefore(",")}")
      var isAudioCodecSupported =
        selectCodec(mediaCodecInfoUtil.findCorrectAudioMimeType("audio/${video.audioCodec}"))
      val isVideoSupported =
        selectCodec(mediaCodecInfoUtil.findCorrectVideoMimeType("video/${video.videoCodec}"))

      isAudioCodecSupported = if (androidHelper.isNvidiaShield || androidHelper.isBravia) {
        when (video.audioCodec.toLowerCase()) {
          "eac3", "ac3", "dts", "truehd" -> true
          else -> isAudioCodecSupported
        }
      } else {
        isAudioCodecSupported
      }

      logger.debug("Audio Codec:  ${video.audioCodec} support returned $isAudioCodecSupported")
      logger.debug("Video Codec:  ${video.videoCodec} support returned $isVideoSupported")
      logger.debug("Video Container:  ${video.container} support returned $isVideoContainerSupported")

      if (isVideoSupported && isAudioCodecSupported && isVideoContainerSupported!!) {
        return true
      }
    }

    return false
  }

  private fun transcoderUrl(): String {
    logger.debug("ExoPlayerPresenter: Container: ${video.container} Audio: ${video.audioCodec}")
    if (isDirectPlaySupportedForContainer(video)) {
      logger.debug("ExoPlayerPresenter: Direct playing ${video.directPlayUrl}")
      return video.directPlayUrl
    }

    val transcodingUrl = serenityClient.createTranscodeUrl(video.id(), video.resumeOffset)

    logger.debug("ExoPlayerPresenter: Transcoding Url: $transcodingUrl")
    return transcodingUrl
  }

  private fun selectCodec(mimeType: String): Boolean =
    MediaCodecInfoUtil().isCodecSupported(mimeType)
}