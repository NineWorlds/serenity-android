package us.nineworlds.serenity.emby.server

import com.squareup.moshi.Json


data class EmbyServerInfo(
    @Json(name ="Address") val remoteAddres: String,
    @Json(name = "Id") val id: String,
    @Json(name ="Name") val name: String)
