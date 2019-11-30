package us.nineworlds.serenity.ui.video.player

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

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
  }

  interface ExoplayerPresenter {

    fun playBackFromVideoQueue()

    fun isHudShowing(): Boolean

    fun updateServerPlaybackPosition(currentPostion: Long)

    fun updateWatchedStatus()

    fun videoId(): String

    fun stopPlaying()

    fun startPlaying()
  }
}