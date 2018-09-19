package us.nineworlds.serenity.emby.server.api

import retrofit2.Call
import retrofit2.http.GET
import us.nineworlds.serenity.emby.server.model.PublicUserInfo

interface UsersService {

  @GET("/emby/Users/Public")
  fun allPublicUsers(): Call<List<PublicUserInfo>>

}