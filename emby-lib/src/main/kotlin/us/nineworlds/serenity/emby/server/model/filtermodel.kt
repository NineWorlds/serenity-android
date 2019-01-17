package us.nineworlds.serenity.emby.server.model

import com.squareup.moshi.Json

data class QueryFilters(
  @Json(name = "Genres") val genres: List<NameGuidPair>?,
  @Json(name = "Tags") val tags: List<String>?
)

data class NameGuidPair(
  @Json(name = "Name") val name: String,
  @Json(name = "Id") val id: String
)