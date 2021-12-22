package us.nineworlds.serenity.emby.server.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import us.nineworlds.serenity.emby.server.model.*

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
  fun fetchItemQuery(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Query("ParentId") parentId: String? = null,
    @Query("Recursive") recursive: Boolean = true,
    @Query("IncludeItemTypes") includeItemType: String? = null,
    @Query("SortBy") sortOptions: String = "SortName",
    @Query("SortOrder") sortOrder: String = "Ascending",
    @Query("Genres") genre: String?,
    @Query("IsPlayed") isPlayed: Boolean? = null,
    @Query("LimitCount") limitCount: Int? = null,
    @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,SeasonCount,EpisodeCount,UserData,OfficialRating,CommunityRating",
    @Query("Ids") ids: String? = null
  ): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items")
  fun resumableItems(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Query("ParentId") parentId: String,
    @Query("Recursive") recursive: Boolean = true,
    @Query("SortBy") sortOptions: String = "DatePlayed",
    @Query("SortOrder") sortOrder: String = "Descending",
    @Query("Filters") filters: String = "IsResumable",
    @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,UserData,OfficialRating,CommunityRating"
  ): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items")
  fun unwatchedItems(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Query("ParentId") parentId: String,
    @Query("Recursive") recursive: Boolean = true,
    @Query("SortBy") sortOptions: String = "DatePlayed",
    @Query("SortOrder") sortOrder: String = "Descending",
    @Query("Filters") filters: String = "IsUnplayed",
    @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,UserData,OfficialRating,CommunityRating"
  ): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items?Limit=20")
  fun latestItems(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Query("ParentId") parentId: String,
    @Query("Recursive") recursive: Boolean = true,
    @Query("SortBy") sortOptions: String = "DateCreated",
    @Query("SortOrder") sortOrder: String = "Descending",
    @Query("IsPlayed") isPlayed: Boolean = false,
    @Query("Filters") filters: String = "IsNotFolder,IsUnPlayed",
    @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,UserData,OfficialRating,CommunityRating"
  ): Call<QueryResult>

  @GET("/emby/Users/{userId}/Items/{itemId}")
  fun fetchItem(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Path("itemId") itemId: String
  ): Call<Item>

  @POST("/emby/Users/{userId}/PlayedItems/{itemId}")
  fun played(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Path("itemId") itemId: String
  ): Call<UserItemData>

  @DELETE("/emby/Users/{userId}/PlayedItems/{itemId}")
  fun unplayed(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Path("itemId") itemId: String
  ): Call<UserItemData>

  @POST("/emby/Users/{userId}/PlayingItems/{itemId}/Progress")
  fun progress(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Path("itemId") itemId: String,
    @Query("MediaSourceId") mediaSourceId: String? = null,
    @Query("PositionTicks") positionTicks: Long
  ): Call<Void>

  @POST("/emby/Users/{userId}/PlayingItems/{itemId}")
  fun startPlaying(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Path("itemId") itemId: String,
    @Query("MediaSourceId") mediaSourceId: String? = null
  ): Call<Void>

  @DELETE("/emby/Users/{userId}/PlayingItems/{itemId}")
  fun stopPlaying(
    @HeaderMap headerMap: Map<String, String>,
    @Path("userId") userId: String,
    @Path("itemId") itemId: String,
    @Query("MediaSourceId") mediaSourceId: String? = null,
    @Query("PositionTicks") positionTicks: String
  ): Call<Void>

  @GET("/emby/Search/Hints")
  fun search(
    @HeaderMap headerMap: Map<String, String>,
    @Query("UserId") userId: String,
    @Query("SearchTerm") searchTerm: String,
    @Query("IsMovie") isMovie: Boolean = true,
    @Query("IncludeItemTypes") includeItemTypes: String? = "Movie",
    @Query("Limit") limit: Int? = 25
  ): Call<Search>
}