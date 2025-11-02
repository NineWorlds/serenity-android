package us.nineworlds.serenity.core.model.impl

import timber.log.Timber
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask
import java.io.Serializable

abstract class AbstractSeriesContentInfo : SeriesContentInfo, Serializable {
  private var type: Types? = null
  private var id: String? = null
  private var plotSummary: String? = null
  private var posterURL: String? = null
  private var backgroundURL: String? = null
  private var title: String? = null
  private var mediaTagIdentifier: String? = null

  override fun setType(type: Types?) {
    this.type = type
  }

  override fun getType(): Types? = type

  override fun id(): String? = id

  override var parentTitle: String? = null

  override var key: String? = null

  override var generes: List<String>? = null

  override var showsWatched: String? = null

  override var showsUnwatched: String? = null

  override fun getBackgroundURL(): String? = backgroundURL

  override var showMetaDataURL: String? = null

  override var thumbNailURL: String? = null

  override var contentRating: String? = null

  override fun getSummary(): String? = plotSummary

  override fun getImageURL(): String? = posterURL

  override fun getTitle(): String? = title

  override var year: String? = null

  override fun setTitle(title: String?) {
    this.title = title
  }

  override fun setImageURL(posterURL: String?) {
    this.posterURL = posterURL
  }

  override fun setSummary(plotSummary: String?) {
    this.plotSummary = plotSummary
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

  override var studio: String? = null

  override var rating: Double = 0.0

  override val isPartiallyWatched: Boolean
    get() {
      var unwatched = 0
      var watched = 0
      if (showsUnwatched != null) {
        unwatched = showsUnwatched!!.toInt()
      }
      if (showsWatched != null) {
        watched = showsWatched!!.toInt()
      }
      val total = watched + unwatched
      return unwatched != total && watched < total
    }

  override val isUnwatched: Boolean
    get() {
      val unwatched = showsUnwatched!!.toInt()
      return unwatched > 0
    }

  override val isWatched: Boolean
    get() {
      val watchedCount = showsWatched!!.toInt()
      return totalShows() == watchedCount
    }

  override fun totalShows(): Int {
    val unwatched = showsUnwatched!!.toInt()
    val watched = showsWatched!!.toInt()
    return unwatched + watched
  }

  override fun viewedPercentage(): Float {
    if (totalShows() == 0) {
      return 0f
    }
    val watched = showsWatched!!.toFloat()
    return watched / totalShows()
  }

  override fun toggleWatchedStatus() {
    Timber.d("Name: %s", title)
    Timber.d("Key: %s", key)
    Timber.d("Id: %s", id)
    val id = if (id() != null) id() else key
    if (isPartiallyWatched || isUnwatched) {
      WatchedVideoAsyncTask().execute(id)
      showsWatched = totalShows().toString()
      showsUnwatched = "0"
      return
    }
    UnWatchVideoAsyncTask().execute(id)
    showsUnwatched = totalShows().toString()
    showsWatched = "0"
  }

  companion object {
    private const val serialVersionUID = 9068543270225774788L
  }
}
