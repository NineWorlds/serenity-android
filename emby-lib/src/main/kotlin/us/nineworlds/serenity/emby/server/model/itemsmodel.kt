package us.nineworlds.serenity.emby.server.model

import com.squareup.moshi.Json
import org.joda.time.LocalDateTime

data class QueryResult(
  @Json(name = "Items") val items: List<Item>,
  @Json(name = "TotalRecordCount") val totalRecordCount: Int
)

data class Item(
  @Json(name = "Name") val name: String,
  @Json(name = "ServerId") val serverId: String,
  @Json(name = "Id") val id: String,
  @Json(name = "Etag") val etag: String?,
  @Json(name = "DateCreated") val dateCreated: LocalDateTime?,
  @Json(name = "CollectionType") val collectionType: String?,
  @Json(name = "Container") val container: String?,
  @Json(name = "PremiereDate") val premiereDateTime: LocalDateTime?,
  @Json(name = "Overview") val oveview: String?,
  @Json(name = "ProductionYear") val productionYear: String?,
  @Json(name = "OfficialRating") val officialRating: String?,
  @Json(name = "ParentId") val parentId: String?,
  @Json(name = "Rating") val rating: Double?,
  @Json(name = "CommunityRating") val communityRating: Double?,
  @Json(name = "RunTimeTicks") val runTimeTicks: Long?,
  @Json(name = "UserData") val userData: UserData?,
  @Json(name = "MediaStreams") val mediaStreams: List<MediaStream>?,
  @Json(name = "MediaSources") val mediaSources: List<MediaSource>?,
  @Json(name = "EpisodeCount") val episodeCount: Long?,
  @Json(name = "IndexNumber") val episodeNumber: String?,
  @Json(name = "Type") val type: String?,
  @Json(name = "ParentLogoItemId") val parentLogoItemId: String?,
  @Json(name = "SeriesId") val seriesId: String?,
  @Json(name = "ParentIndexNumber") val parentIndexNumber: String?
)

data class UserData(
  @Json(name = "PlaybackPositionTicks") val playbackPositionTicks: Long?,
  @Json(name = "UnplayedItemCount") val unplayedItemCount: Long?,
  @Json(name = "PlayCount") val playCount: Long?,
  @Json(name = "IsFavorite") val isFavorite: Boolean?,
  @Json(name = "Played") val played: Boolean?,
  @Json(name = "Key") val key: String?,
  @Json(name = "PlaybackPercentage") val playbackPercentage: Double?
)

data class MediaStream(
  @Json(name = "Codec") val codec: String?,
  @Json(name = "DisplayTitle") val displayTitle: String?,
  @Json(name = "AspectRatio") val aspectRatio: String?,
  @Json(name = "Type") val type: String?,
  @Json(name = "ChannelType") val channelType: String?,
  @Json(name = "Channels") val channels: String?,
  @Json(name = "IsTextSubtitleStream") val isSubtitle: Boolean = false,
  @Json(name = "Index") val index: Int?,
  @Json(name = "Language") val language: String?,
  @Json(name = "DisplayLanguage") val displayLanguage: String?
)

data class MediaSource(
  @Json(name = "Id") val id: String?,
  @Json(name = "Container") val container: String?,
  @Json(name = "SupportsDirectStream") val directStream: Boolean?,
  @Json(name = "SupportsTranscoding") val transcoding: Boolean?
)

data class UserItemData(
  @Json(name = "Rating") val rating: Double?,
  @Json(name = "PlayedPercentage") val playedPrecentage: Double?,
  @Json(name = "UnplayedItemCount") val unplayedItemCount: Int?,
  @Json(name = "PlaybackPositionTicks") val playbackPositionTicks: Long?,
  @Json(name = "PlayCount") val playCount: Int?,
  @Json(name = "IsFavorite") val isFavorite: Boolean?,
  @Json(name = "Likes") val likes: Boolean?,
  @Json(name = "LastPlayedDate") val lastPlayedDate: String?,
  @Json(name = "Played") val played: Boolean?,
  @Json(name = "Key") val key: String?,
  @Json(name = "ItemId") val itemId: String?
)

data class Search(
  @Json(name = "SearchHints") val searchHints: List<SearchHint>?,
  @Json(name = "TotalRecordCount") val totalRecordCount: Int?
)

data class SearchHint(
  @Json(name = "ItemId") val id: String?,
  @Json(name = "Name") val name: String?,
  @Json(name = "MatchedTerm") val matchedTerm: String?,
  @Json(name = "IndexNumber") val indexNumber: Int?,
  @Json(name = "ProductionYear") val productionYear: String?,
  @Json(name = "PrimaryImageTag") val primaryImageTag: String?,
  @Json(name = "ThumbImageTag") val thumbImageTag: String?,
  @Json(name = "ThumbImageItemId") val thumbImageItemId: String?,
  @Json(name = "BackdropImageTag") val backdropImageTag: String?,
  @Json(name = "BackdropImageItemId") val backdropImageItemId: String?,
  @Json(name = "IsFolder") val isFolder: Boolean?,
  @Json(name = "RuntimeTicks") val runtimeTicks: Long?,
  @Json(name = "MediaType") val mediatype: String?,
  @Json(name = "StartDate") val startDate: LocalDateTime?,
  @Json(name = "EndDate") val endDate: LocalDateTime?,
  @Json(name = "Series") val series: String?,
  @Json(name = "Status") val status: String?,
  @Json(name = "Type") val type: String?
)