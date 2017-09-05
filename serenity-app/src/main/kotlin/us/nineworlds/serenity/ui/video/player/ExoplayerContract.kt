package us.nineworlds.serenity.ui.video.player

import com.arellomobile.mvp.MvpView

interface ExoplayerContract {

  interface ExoplayerView : MvpView {

    fun initializePlayer(videoUrl: String)
  }

  interface ExoplayerPresenter {

    fun playBackFromVideoQueue()
  }

}