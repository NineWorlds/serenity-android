package us.nineworlds.serenity.emby.server.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class QueryResult(@JsonProperty("Items") val items: List<Item>,
    @JsonProperty("TotalRecordCount") val totalRecordCount: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Item(@JsonProperty("Name") val name: String,
    @JsonProperty("ServerId") val serverId: String,
    @JsonProperty("Id") val id: String,
    @JsonProperty("Etag") val etag: String,
    @JsonProperty("DateCreated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    val dateCreated: LocalDateTime)