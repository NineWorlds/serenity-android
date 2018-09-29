package us.nineworlds.serenity.emby.server.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import us.nineworlds.serenity.emby.server.model.AuthenticateUserByName
import us.nineworlds.serenity.emby.server.model.AuthenticationResult
import us.nineworlds.serenity.emby.server.model.PublicUserInfo
import us.nineworlds.serenity.emby.server.model.QueryResult

interface UsersService {

  @GET("/emby/Users/Public")
  fun allPublicUsers(): Call<List<PublicUserInfo>>

  @POST("/emby/Users/AuthenticateByName")
  fun authenticate(@Body authenticateUserByName: AuthenticateUserByName, @HeaderMap headerMap: Map<String, String>): Call<AuthenticationResult>

  /**
   * use this to provide menus.  You can ignore the Folders
   * Folders will be identified by CollectionType: folders.  Movies are CollectionType: movies, TV Shows are CollectionType: tvshows
   * 1. User must be logged in
   * 2. Token must be valid
   *
   * The plex corresponding call is retrieveRootData()
   */
  @GET("/emby/Users/{userId}/Views")
  fun usersViews(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items")
  fun fetchItemQuery(@HeaderMap headerMap: Map<String, String>,
      @Path("userId") userId: String,
      @Query( "ParentId") parentId: String,
      @Query("Recursive") recursive: Boolean = true,
      @Query("SortBy") sortOptions: String = "SortName",
      @Query("SortOrder") sortOrder: String = "Ascending",
      @Query( "Genres") genre:String?,
      @Query( "IsPlayed") isPlayed: Boolean? = null,
      @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources"): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items")
  fun resumableItems(@HeaderMap headerMap: Map<String, String>,
      @Path("userId") userId: String,
      @Query( "ParentId") parentId: String,
      @Query("Recursive") recursive: Boolean = true,
      @Query("SortBy") sortOptions: String = "DatePlayed",
      @Query("SortOrder") sortOrder: String = "Descending",
      @Query("Filters") filters: String = "IsResumable",
      @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources"): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items")
  fun unwatchedItems(@HeaderMap headerMap: Map<String, String>,
      @Path("userId") userId: String,
      @Query( "ParentId") parentId: String,
      @Query("Recursive") recursive: Boolean = true,
      @Query("SortBy") sortOptions: String = "DatePlayed",
      @Query("SortOrder") sortOrder: String = "Descending",
      @Query("Filters") filters: String = "IsUnplayed",
      @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources"): Call<QueryResult>


  @GET("/emby/Users/{userId}/Items/Latest")
  fun latestItems(@HeaderMap headerMap: Map<String, String>,
      @Path("userId") userId: String,
      @Query( "ParentId") parentId: String,
      @Query("Recursive") recursive: Boolean = true,
      @Query("IsPlayed") isPlayed: Boolean = false,
      @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources"): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items/{itemId}")
  fun fetchItem(@HeaderMap headerMap: Map<String, String>,
      @Path("userId") userId: String,
      @Path("itemId") itemId: String): Call<QueryResult>

}