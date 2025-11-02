package us.nineworlds.serenity.core.model

interface SeriesContentInfo : ContentInfo {
  var key: String?
  var generes: List<String>?
  var showsWatched: String?
  var showsUnwatched: String?
  var showMetaDataURL: String?
  var thumbNailURL: String?
  var contentRating: String?
  var year: String?
  var parentTitle: String?
  var studio: String?
  var rating: Double
  val isPartiallyWatched: Boolean
  val isWatched: Boolean
  val isUnwatched: Boolean
  fun viewedPercentage(): Float
  fun toggleWatchedStatus()
  fun totalShows(): Int
}
