package us.nineworlds.serenity.ui.video.player

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import us.nineworlds.plex.rest.PlexappFactory
import us.nineworlds.serenity.common.injection.SerenityObjectGraph
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo
import us.nineworlds.serenity.injection.ForVideoQueue
import java.util.LinkedList
import javax.inject.Inject

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
    val video: VideoContentInfo = videoQueue.poll()
    videoURL = video.directPlayUrl
    this.video = video
    if (video is EpisodePosterInfo) {
      if (video.getParentPosterURL() != null) {
        posterURL = video.getParentPosterURL()
      }
    }
    if (video.subtitle != null && "none" != video.subtitle.format) {
      subtitleURL = video.subtitle.key
      subtitleType = video.subtitle.format
    }

    viewState.initializePlayer(video)
  }

}