package us.nineworlds.serenity.core.model.impl

import android.content.res.Resources
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.SerenityConstants
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask
import java.io.Serializable

abstract class AbstractVideoContentInfo(private val resources: Resources?) : VideoContentInfo, Serializable {

  private var id: String? = null
  private var type: Types? = null

  private var plotSummary: String? = null
  private var posterURL: String? = null
  private var backgroundURL: String? = null
  private var title: String? = null
  private var originalTitle: String? = null
  private var mediaTagIdentifier: String? = null

  override fun id(): String? = id

  override fun getType(): Types? = type

  override fun setType(type: Types?) {
    this.type = type
  }

  override fun getSummary(): String? = plotSummary

  override fun getBackgroundURL(): String? = backgroundURL

  override fun getImageURL(): String? = posterURL

  override fun getTitle(): String? = title

  override fun setTitle(title: String?) {
    this.title = title
  }

  override fun setImageURL(imageURL: String?) {
    this.posterURL = imageURL
  }

  override fun setSummary(summary: String?) {
    this.plotSummary = summary
  }

  override fun setBackgroundURL(backgroundURL: String?) {
    this.backgroundURL = backgroundURL
  }

  override fun setId(id: String?) {
    this.id = id
  }

  override fun getMediaTagIdentifier(): String? = mediaTagIdentifier

  override fun setMediaTagIdentifier(mediaTagIdentifier: String?) {
    this.mediaTagIdentifier = mediaTagIdentifier
  }

  override val longTitle: String?
    get() {
      val seriesTitle = seriesTitle
      return if (seriesTitle == null) {
        title
      } else resources?.getString(R.string.long_title, seriesTitle, seasonNumber, episodeNumber, title)
    }
  override var directPlayUrl: String? = null
  override var actors: List<String>? = null
  override var audioCodec: String? = null
  override var castInfo: String? = null
  override var contentRating: String? = null
  override var directors: List<String>? = null
  override var genres: List<String>? = null
  override var videoCodec: String? = null
  override var videoResolution: String? = null
  override var writers: List<String>? = null
  override var year: String? = null
  override var aspectRatio: String? = null
  override var viewCount: Int = 0
  override var container: String? = null
  override var parentPosterURL: String? = null
  override var grandParentPosterURL: String? = null
  override val audioChannels: String? = null
  override var resumeOffset: Int = 0
  override var duration: Int = 0
  override val season: String?
    get() = resources?.getString(R.string.season_) + seasonNumber
  override var seasonNumber: Int = 0
  override val episode: String?
    get() = resources?.getString(R.string.episode_) + episodeNumber
  override var episodeNumber: Int = 0
  override var originalAirDate: String? = null
  override var seriesTitle: String? = null
  override var subtitle: Subtitle? = null
  override var studio: String? = null
  override var rating: Double = 0.0
  override var parentKey: String? = null
  override val isPartiallyWatched: Boolean
    get() {
      if (resumeOffset > 0) {
        val percentWatched = viewedPercentage()
        if (percentWatched <= SerenityConstants.WATCHED_PERCENT) {
          return true
        }
      }
      return false
    }
  override val isWatched: Boolean
    get() {
      val percentWatched = viewedPercentage()
      if (percentWatched > SerenityConstants.WATCHED_PERCENT) {
        return true
      }
      return if (resumeOffset == 0 && viewCount > 0) {
        true
      } else false
    }
  override val isUnwatched: Boolean
    get() = if (viewCount == 0) {
      true
    } else false

  override fun viewedPercentage(): Float {
    val duration = duration.toFloat()
    val offset = resumeOffset.toFloat()
    if (duration == 0f) {
      return 0f
    }
    return offset / duration
  }

  override fun toggleWatchStatus() {
    if (isPartiallyWatched || isUnwatched) {
      WatchedVideoAsyncTask().execute(id())
      viewCount++
      return
    }
    UnWatchVideoAsyncTask().execute(id())
    viewCount = 0
  }

  private var _hasTrailer: Boolean = false

  override fun hasTrailer(): Boolean = _hasTrailer

  override fun setTrailer(trailer: Boolean) {
    _hasTrailer = trailer
  }

  private var _trailerId: String? = null
  override fun trailerId(): String? = _trailerId

  override fun setTrailerId(id: String?) {
    _trailerId = id
  }

  override var availableSubtitles: List<Subtitle>? = null
  override var tagLine: String? = null
  override var seriesName: String? = null

  companion object {
    private const val serialVersionUID = 4744447508883279194L
  }
}
