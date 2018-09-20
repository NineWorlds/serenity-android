package us.nineworlds.serenity.emby.server.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import us.nineworlds.serenity.emby.server.model.AuthenticateUserByName
import us.nineworlds.serenity.emby.server.model.AuthenticationResult
import us.nineworlds.serenity.emby.server.model.PublicUserInfo

interface UsersService {

  @GET("/emby/Users/Public")
  fun allPublicUsers(): Call<List<PublicUserInfo>>

  @POST("/emby/Users/AuthenticateByName")
  fun authenticate(@Body authenticateUserByName: AuthenticateUserByName, @HeaderMap headerMap: Map<String, String>): Call<AuthenticationResult>

}