package us.nineworlds.serenity.emby.server.model

import com.squareup.moshi.Json
import org.joda.time.LocalDateTime

data class QueryResult(@Json(name = "Items") val items: List<Item>,
                       @Json(name = "TotalRecordCount") val totalRecordCount: Int)

data class Item(@Json(name = "Name") val name: String,
    @Json(name = "ServerId") val serverId: String,
    @Json(name = "Id") val id: String,
    @Json(name = "Etag") val etag: String?,
    @Json(name = "DateCreated") val dateCreated: LocalDateTime?,
    @Json(name = "CollectionType") val collectionType: String?)