package us.nineworlds.serenity.emby.server.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import us.nineworlds.serenity.emby.server.model.QueryFilters

interface FilterService {

  @GET("/emby/Genres?EnableUserData=false&SortBy=SortName&SortOrder=Ascending&EnableTotalRecordCount=false&EnableImages=false")
  fun availableFilters(
    @HeaderMap headerMap: Map<String, String>,
    @Query("userId") userId: String,
    @Query("ParentId") itemId: String? = null,
    @Query("Recursive") recursive: Boolean = true
  ): Call<QueryFilters>
}