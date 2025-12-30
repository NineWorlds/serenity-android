package us.nineworlds.serenity.jellyfin.server

import com.squareup.moshi.Json

data class JellyfinServerInfo(@Json(name = "Address") val remoteAddres: String, @Json(name = "Id") val id: String, @Json(name = "Name") val name: String)
