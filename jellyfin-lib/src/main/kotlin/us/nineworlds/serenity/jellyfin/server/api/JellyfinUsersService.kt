package us.nineworlds.serenity.jellyfin.server.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import us.nineworlds.serenity.jellyfin.server.model.AuthenticateUserByName
import us.nineworlds.serenity.jellyfin.server.model.AuthenticationResult
import us.nineworlds.serenity.jellyfin.server.model.Item
import us.nineworlds.serenity.jellyfin.server.model.PublicUserInfo
import us.nineworlds.serenity.jellyfin.server.model.QueryResult
import us.nineworlds.serenity.jellyfin.server.model.Search
import us.nineworlds.serenity.jellyfin.server.model.UserItemData

interface JellyfinUsersService {

    @GET("/Users/Public")
    fun allPublicUsers(): Call<List<PublicUserInfo>>

    @POST("/Users/AuthenticateByName")
    fun authenticate(@Body authenticateUserByName: AuthenticateUserByName, @HeaderMap headerMap: Map<String, String>): Call<AuthenticationResult>

    /**
     * use this to provide menus.  You can ignore the Folders
     * Folders will be identified by CollectionType: folders.  Movies are CollectionType: movies, TV Shows are CollectionType: tvshows
     * 1. User must be logged in
     * 2. Token must be valid
     *
     * The plex corresponding call is retrieveRootData()
     */
    @GET("/Users/{userId}/Views")
    fun usersViews(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String): Call<QueryResult>

    @GET("/Users/{userId}/Items")
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
        @Suppress(
            "ktlint:standard:max-line-length"
        ) @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,SeasonCount,EpisodeCount,UserData,OfficialRating,CommunityRating,SeriesName,SeasonName,",
        @Query("Ids") ids: String? = null,
        @Query("StartIndex") startIndex: Int = 0,
        @Query("Limit") limit: Int? = null
    ): Call<QueryResult>

    @GET("/Users/{userId}/Items")
    fun resumableItems(
        @HeaderMap headerMap: Map<String, String>,
        @Path("userId") userId: String,
        @Query("ParentId") parentId: String,
        @Query("Recursive") recursive: Boolean = true,
        @Query("SortBy") sortOptions: String = "DatePlayed",
        @Query("SortOrder") sortOrder: String = "Descending",
        @Query("Filters") filters: String = "IsResumable",
        @Suppress("ktlint:standard:max-line-length")
        @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,UserData,OfficialRating,CommunityRating",
        @Query("IncludeItemTypes") includeItemType: String? = null,
        @Query("StartIndex") startIndex: Int = 0,
        @Query("Limit") limit: Int? = null
    ): Call<QueryResult>

    @GET("/Users/{userId}/Items")
    fun unwatchedItems(
        @HeaderMap headerMap: Map<String, String>,
        @Path("userId") userId: String,
        @Query("ParentId") parentId: String,
        @Query("Recursive") recursive: Boolean = true,
        @Query("SortBy") sortOptions: String = "DatePlayed",
        @Query("SortOrder") sortOrder: String = "Descending",
        @Query("Filters") filters: String = "IsUnplayed",
        @Suppress("ktlint:standard:max-line-length")
        @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,UserData,OfficialRating,CommunityRating",
        @Query("IncludeItemTypes") includeItemType: String? = null,
        @Query("StartIndex") startIndex: Int = 0,
        @Query("Limit") limit: Int? = null
    ): Call<QueryResult>

    @GET("/Users/{userId}/Items?Limit=20")
    fun latestItems(
        @HeaderMap headerMap: Map<String, String>,
        @Path("userId") userId: String,
        @Query("ParentId") parentId: String,
        @Query("Recursive") recursive: Boolean = true,
        @Query("SortBy") sortOptions: String = "DateCreated",
        @Query("SortOrder") sortOrder: String = "Descending",
        @Query("IsPlayed") isPlayed: Boolean = false,
        @Query("Filters") filters: String = "IsNotFolder,IsUnPlayed",
        @Suppress("ktlint:standard:max-line-length")
        @Query("Fields") fields: String = "Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,UserData,OfficialRating,CommunityRating",
        @Query("IncludeItemTypes") includeItemType: String? = null,
        @Query("StartIndex") startIndex: Int = 0,
        @Query("Limit") limit: Int? = null
    ): Call<QueryResult>

    @GET("/Users/{userId}/Items/{itemId}")
    fun fetchItem(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String, @Path("itemId") itemId: String): Call<Item>

    @POST("/Users/{userId}/PlayedItems/{itemId}")
    fun played(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String, @Path("itemId") itemId: String): Call<UserItemData>

    @DELETE("/Users/{userId}/PlayedItems/{itemId}")
    fun unplayed(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String, @Path("itemId") itemId: String): Call<UserItemData>

    @POST("/Users/{userId}/PlayingItems/{itemId}/Progress")
    fun progress(
        @HeaderMap headerMap: Map<String, String>,
        @Path("userId") userId: String,
        @Path("itemId") itemId: String,
        @Query("MediaSourceId") mediaSourceId: String? = null,
        @Query("PositionTicks") positionTicks: Long
    ): Call<Void>

    @POST("/Users/{userId}/PlayingItems/{itemId}")
    fun startPlaying(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String, @Path("itemId") itemId: String, @Query("MediaSourceId") mediaSourceId: String? = null): Call<Void>

    @DELETE("/Users/{userId}/PlayingItems/{itemId}")
    fun stopPlaying(
        @HeaderMap headerMap: Map<String, String>,
        @Path("userId") userId: String,
        @Path("itemId") itemId: String,
        @Query("MediaSourceId") mediaSourceId: String? = null,
        @Query("PositionTicks") positionTicks: String
    ): Call<Void>

    @GET("/Search/Hints")
    fun search(
        @HeaderMap headerMap: Map<String, String>,
        @Query("UserId") userId: String,
        @Query("SearchTerm") searchTerm: String,
        @Query("IsMovie") isMovie: Boolean = true,
        @Query("IncludeItemTypes") includeItemTypes: String? = "Movie",
        @Query("Limit") limit: Int? = 25
    ): Call<Search>

    @GET("/Movies/{itemId}/Similar")
    fun fetchSimilarItemById(
        @HeaderMap headerMap: Map<String, String>,
        @Path("itemId") itemId: String,
        @Query("UserId") userId: String,
        @Query("IncludeItemTypes") includeItemType: String? = null,
        @Suppress("ktlint:standard:max-line-length")
        @Query("Fields") fields: String = "Name,Id,Overview,MediaStreams,Studios,ParentId,Genres,MediaSources,SeasonCount,EpisodeCount,UserData,OfficialRating,CommunityRating,SeriesName,SeasonName",
        @Query("Limit") limit: Int? = 25
    ): Call<QueryResult>
}
