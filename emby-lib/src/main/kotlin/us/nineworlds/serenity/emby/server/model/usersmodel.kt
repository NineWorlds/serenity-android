package us.nineworlds.serenity.emby.server.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublicUserInfo(@JsonProperty("Name") val name: String?,
    @JsonProperty("ServerId") val serverId: String?,
    @JsonProperty("Id") val id: String?,
    @JsonProperty("HasPassword") val hasPassword: Boolean,
    @JsonProperty("HasConfiguredPassword") val hasConfiguredPassword: Boolean,
    @JsonProperty("HasConfiguredEasyPassword") val hasConfiguredEasyPassword: Boolean,
    @JsonProperty("LastLoginDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    val lastLoginDate: LocalDateTime?,
    @JsonProperty("LastActivityDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    val lastActivityDate: LocalDateTime?)


@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticateUserByName(@JsonProperty("Username") val username: String,
    @JsonProperty("PassowrdMd5") val passwordMD5: String,
    @JsonProperty("Password") val password: String,
    @JsonProperty("Pw") val pw: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticationResult(@JsonProperty("User") val userInfo: PublicUserInfo,
    @JsonProperty("AccessToken") val accesToken: String,
    @JsonProperty("ServerId") val serverId: String)