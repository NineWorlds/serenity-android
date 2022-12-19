package us.nineworlds.serenity.common.rest

import us.nineworlds.serenity.common.media.model.IMediaContainer
import java.io.IOException
import java.lang.Exception

interface SerenityClient {

    @Throws(Exception::class)
    fun fetchSimilarItemById(itemId: String, types: Types): IMediaContainer

    @Throws(Exception::class)
    fun fetchItemById(itemId: String): IMediaContainer

    @Throws(Exception::class)
    fun retrieveRootData(): IMediaContainer

    @Throws(Exception::class)
    fun retrieveLibrary(): IMediaContainer

    @Throws(Exception::class)
    fun retrieveItemByCategories(): IMediaContainer

    @Throws(Exception::class)
    fun retrieveCategoriesById(key: String): IMediaContainer

    @Throws(Exception::class)
    fun retrieveItemByIdCategory(key: String, category: String, types: Types): IMediaContainer

    @Throws(Exception::class)
    fun retrieveItemByIdCategory(key: String, category: String, types: Types, startIndex: Int = 0, limit: Int? = null): IMediaContainer

    @Throws(Exception::class)
    fun retrieveItemByCategories(key: String, category: String, secondaryCategory: String): IMediaContainer

    @Throws(Exception::class)
    fun retrieveSeasons(key: String): IMediaContainer

    @Throws(Exception::class)
    fun retrieveMusicMetaData(key: String): IMediaContainer

    @Throws(Exception::class)
    fun retrieveEpisodes(key: String): IMediaContainer

    @Throws(Exception::class)
    fun retrieveMovieMetaData(key: String): IMediaContainer

    @Throws(Exception::class)
    fun searchMovies(key: String, query: String): IMediaContainer?

    @Throws(Exception::class)
    fun searchEpisodes(key: String, query: String): IMediaContainer?

    fun updateBaseUrl(baseUrl: String)

    fun baseURL(): String?

    @Throws(IOException::class)
    fun watched(key: String): Boolean

    @Throws(IOException::class)
    fun unwatched(key: String): Boolean

    @Throws(IOException::class)
    fun progress(key: String, offset: String): Boolean
    fun createMediaTagURL(resourceType: String, resourceName: String, identifier: String): String?
    fun createSectionsURL(key: String, category: String): String
    fun createSectionsURL(): String
    fun createSectionsUrl(key: String): String
    fun createMovieMetadataURL(key: String): String
    fun createEpisodesURL(key: String): String
    fun createSeasonsURL(key: String): String
    fun createImageURL(url: String, width: Int, height: Int): String
    fun createTranscodeUrl(id: String, offset: Int): String
    fun reinitialize()
    fun userInfo(userId: String): SerenityUser?
    fun allAvailableUsers(): List<SerenityUser>
    fun authenticateUser(user: SerenityUser): SerenityUser
    fun createUserImageUrl(user: SerenityUser, width: Int, height: Int): String
    fun startPlaying(key: String)
    fun stopPlaying(key: String, offset: Long)
    fun retrieveSeriesById(key: String, categoryId: String): IMediaContainer
    fun retrieveSeriesCategoryById(key: String): IMediaContainer

    /**
     * Whether the client supports multiple users or not.  Plex is false, Emby is true.
     * @return true or false if the client supports multiple users
     */
    fun supportsMultipleUsers(): Boolean
}