package us.nineworlds.serenity.ui.video.player

import com.arellomobile.mvp.MvpView

interface ExoplayerContract {

  interface ExoplayerView : MvpView {

    fun initializePlayer(videoUrl: String, offset: Int)

    fun hideController()

    fun showController()

    fun pause()

    fun play()

    fun hideLoadingProgress()

    fun showLoadingProgress()
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