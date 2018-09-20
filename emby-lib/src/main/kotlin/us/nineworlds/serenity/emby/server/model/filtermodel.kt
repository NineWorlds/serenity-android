package us.nineworlds.serenity.emby.server.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class QueryFilters(@JsonProperty("Genres") val genres: List<NameGuidPair>?,
    @JsonProperty("Tags") val tags: List<String>?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NameGuidPair(@JsonProperty("Name") val name: String,
    @JsonProperty("Id") val id: String)