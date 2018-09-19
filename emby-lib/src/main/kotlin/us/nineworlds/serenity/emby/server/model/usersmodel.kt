package us.nineworlds.serenity.emby.server.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublicUserInfo(@JsonProperty("Name") val name: String,
    @JsonProperty("ServerId") val serverId: String,
    @JsonProperty("Id") val id: String,
    @JsonProperty("HasPassword") val hasPassword: Boolean,
    @JsonProperty("HasConfiguredPassword") val hasConfiguredPassword: Boolean,
    @JsonProperty("HasConfiguredEasyPassword") val hasConfiguredEasyPassword: Boolean,
    @JsonProperty("LastLoginDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")
    val lastLoginDate: LocalDateTime,
    @JsonProperty("LastActivityDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")
    val lastActivityDate: LocalDateTime)

