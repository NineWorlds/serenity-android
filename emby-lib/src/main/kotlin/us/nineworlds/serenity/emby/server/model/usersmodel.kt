package us.nineworlds.serenity.emby.server.model

import com.squareup.moshi.Json
import org.joda.time.LocalDateTime

data class PublicUserInfo(
  @Json(name = "Name") val name: String?,
  @Json(name = "ServerId") val serverId: String?,
  @Json(name = "Id") val id: String?,
  @Json(name = "HasPassword") val hasPassword: Boolean,
  @Json(name = "HasConfiguredPassword") val hasConfiguredPassword: Boolean,
  @Json(name = "HasConfiguredEasyPassword") val hasConfiguredEasyPassword: Boolean,
  @Json(name = "LastLoginDate") val lastLoginDate: LocalDateTime?,
  @Json(name = "LastActivityDate") val lastActivityDate: LocalDateTime?
)

data class AuthenticateUserByName(
  @Json(name = "Username") val username: String,
  @Json(name = "PassowrdMd5") val passwordMD5: String,
  @Json(name = "Password") val password: String,
  @Json(name = "Pw") val pw: String
)

data class AuthenticationResult(
  @Json(name = "User") val userInfo: PublicUserInfo,
  @Json(name = "AccessToken") val accesToken: String,
  @Json(name = "ServerId") val serverId: String
)