package us.nineworlds.serenity.core.model

interface SeriesContentInfo : ContentInfo {
    var key: String?
    var generes: List<String>?
    var showsWatched: String?
    var showsUnwatched: String?
    var showMetaDataURL: String?
    var thumbNailURL: String?
    fun toggleWatchedStatus()
    fun totalShows(): Int
}
