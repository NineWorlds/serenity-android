package us.nineworlds.serenity.emby.server

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI


data class EmbyServerInfo(
    @JsonProperty("Address") val remoteAddres: String,
    @JsonProperty("Id") val id: String,
    @JsonProperty("Name") val name: String)
