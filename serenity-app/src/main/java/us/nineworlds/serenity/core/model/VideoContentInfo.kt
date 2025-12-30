package us.nineworlds.serenity.core.model

import us.nineworlds.serenity.core.model.impl.Subtitle

interface VideoContentInfo : ContentInfo {
    val longTitle: String?
    var directPlayUrl: String?
    var actors: List<String>?
    var audioCodec: String?
    var castInfo: String?
    var contentRating: String?
    var directors: List<String>?
    var genres: List<String>?
    var videoCodec: String?
    var videoResolution: String?
    var writers: List<String>?
    var year: String?
    var aspectRatio: String?
    var viewCount: Int
    var container: String?
    var parentPosterURL: String?
    var grandParentPosterURL: String?
    val audioChannels: String?
    var resumeOffset: Int
    var duration: Int
    val season: String?
    var seasonNumber: Int
    val episode: String?
    var episodeNumber: Int
    var originalAirDate: String?
    var seriesTitle: String?
    var subtitle: Subtitle?
    var studio: String?
    var rating: Double
    var parentKey: String?
    val isPartiallyWatched: Boolean
    val isWatched: Boolean
    val isUnwatched: Boolean
    fun viewedPercentage(): Float
    fun toggleWatchStatus()
    fun hasTrailer(): Boolean
    fun setTrailer(trailer: Boolean)
    fun trailerId(): String?
    fun setTrailerId(id: String?)
    var availableSubtitles: List<Subtitle>?
    var tagLine: String?
    var seriesName: String?
}
