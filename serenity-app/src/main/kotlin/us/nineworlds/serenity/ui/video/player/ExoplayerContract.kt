package us.nineworlds.serenity.ui.video.player

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.emby.model.Video

interface ExoplayerContract {

  interface ExoplayerView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun initializePlayer(videoUrl: String, offset: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideController()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showController()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun pause()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun play()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoadingProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoadingProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun playbackEnded()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showResumeDialog(video: VideoContentInfo)
  }

  interface ExoplayerPresenter {

    fun playBackFromVideoQueue(autoResume: Boolean)

    fun isHudShowing(): Boolean

    fun updateServerPlaybackPosition(currentPostion: Long)

    fun updateWatchedStatus()

    fun videoId(): String

    fun stopPlaying(currentPosition: Long)

    fun startPlaying()

    fun playVideo()
  }
}