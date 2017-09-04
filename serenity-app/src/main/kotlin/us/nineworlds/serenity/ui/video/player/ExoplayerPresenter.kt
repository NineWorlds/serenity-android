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

    //val videoUrl = "http://192.168.86.27:32400/video/:/transcode/universal/start.mkv?path=http%3A%2F%2F127.0.0.1%3A32400%2Flibrary%2Fmetadata%2F1&mediaIndex=0&partIndex=0&protocol=http&offset=0&fastSeek=1&copyts=1&directPlay=0&directStream=1&subtitleSize=100&audioBoost=100&maxVideoBitrate=20000&videoQuality=100&videoResolution=1280x720&session=069a0dc3-25ed-4a89-85f3-e6bda28dba42&subtitles=burn&Accept-Language=en&X-Plex-Product=Plex+Web&X-Plex-Version=2.4.9&X-Plex-Client-Identifier=tll8dnyyw0f&X-Plex-Platform=Opera&X-Plex-Platform-Version=47.0&X-Plex-Device=Linux&X-Plex-Device-Name=Plex+Web+(Opera)"

    viewState.initializePlayer(videoUrl)
  }

  fun isDirectPlaySupportedForContainer(video: VideoContentInfo): Boolean {
    if (video.container.equals("avi")) {
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

}