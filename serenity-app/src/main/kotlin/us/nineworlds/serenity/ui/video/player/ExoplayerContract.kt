package us.nineworlds.serenity.ui.video.player

import com.arellomobile.mvp.MvpView
import us.nineworlds.serenity.core.model.VideoContentInfo

interface ExoplayerContract {

  interface ExoplayerView : MvpView {

    fun initializePlayer(video: VideoContentInfo)
  }

}