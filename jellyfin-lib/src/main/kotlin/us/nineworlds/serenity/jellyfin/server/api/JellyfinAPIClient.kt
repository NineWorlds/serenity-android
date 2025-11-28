package us.nineworlds.serenity.jellyfin.server.api

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.LocalDateTime
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.jellyfin.BuildConfig
import us.nineworlds.serenity.jellyfin.adapters.JellyfinMediaContainerAdaptor
import us.nineworlds.serenity.jellyfin.moshi.LocalDateJsonAdapter
import us.nineworlds.serenity.jellyfin.server.model.AuthenticateUserByName
import us.nineworlds.serenity.jellyfin.server.model.AuthenticationResult
import us.nineworlds.serenity.jellyfin.server.model.Item
import us.nineworlds.serenity.jellyfin.server.model.PublicUserInfo
import us.nineworlds.serenity.jellyfin.server.model.QueryFilters
import us.nineworlds.serenity.jellyfin.server.model.QueryResult
import java.io.File
import java.io.IOException
import java.util.UUID

class JellyfinAPIClient(val context: Context, baseUrl: String = "http://localhost:8096") : SerenityClient {

    private val usersService: JellyfinUsersService
    private val filterService: JellyfinFilterService

    var baseUrl: String
    var accessToken: String? = null
    lateinit var serverId: String
    var userId: String? = null
    var deviceId: String
    var deviceName: String
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        this.baseUrl = baseUrl
        val logger = HttpLoggingInterceptor()
        val cacheDir = File(context.cacheDir, "JellyfinClient")

        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(cacheDir, cacheSize.toLong())

        val okClient = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        logger.level = HttpLoggingInterceptor.Level.BASIC
        okClient.addInterceptor(logger)
        okClient.cache(cache)

        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDateTime::class.java, LocalDateJsonAdapter()).build()

        val builder = Retrofit.Builder()
        val jellyfinRetrofit = builder.baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okClient.build())
                .build()

        usersService = jellyfinRetrofit.create(JellyfinUsersService::class.java)
        filterService = jellyfinRetrofit.create(JellyfinFilterService::class.java)


        deviceId = pseudoUniqueID()
        deviceName = "${Build.MANUFACTURER} ${Build.MODEL} "
        Timber.d(this::class.java.simpleName, "Device Id: $deviceId")
        Timber.d(this::class.java.simpleName, "Device Name : $deviceName")
    }

    fun fetchAllPublicUsers(): List<PublicUserInfo> {
        val allPublicUsers = usersService.allPublicUsers()
        return allPublicUsers.executeOrThrow()
    }

    fun userImageUrl(userId: String): String {
        return "$baseUrl/Users/$userId/Images/Primary"
    }

    fun authenticate(userName: String, password: String = ""): AuthenticationResult {
        val authenticationResul = AuthenticateUserByName(userName, "", password, password)
        val call = usersService.authenticate(authenticationResul, headerMap())
        val body = call.executeOrThrow()

        accessToken = body.accesToken
        serverId = body.serverId
        userId = body.userInfo.id!!

        val prefEditor = prefs.edit()

        prefEditor.putString("userId", userId)
        prefEditor.putString("jellyfinAccessToken", accessToken)
        prefEditor.apply()

        return body
    }

    fun currentUserViews(): QueryResult {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.usersViews(headerMap(), userId!!)
        return call.executeOrThrow()
    }

    fun filters(itemId: String? = null, tags: List<String>? = null): QueryFilters {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = filterService.availableFilters(headerMap(), userId!!)
        return call.executeOrThrow()
    }

    override fun fetchItemById(itemId: String): IMediaContainer {
        try {
            val result = fetchItem(itemId)
            return when (result.type) {
                "Series" -> JellyfinMediaContainerAdaptor().createSeriesList(listOf(result))
                else -> JellyfinMediaContainerAdaptor().createVideoList(listOf(result))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return JellyfinMediaContainerAdaptor().createVideoList(emptyList())
    }

    fun fetchItem(id: String): Item {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.fetchItem(headerMap(), userId!!, id)

        return call.executeOrThrow()
    }

    fun fetchItemQuery(id: String): QueryResult {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.fetchItemQuery(headerMap(), userId!!, id, genre = null)

        return call.executeOrThrow()
    }

    private fun headerMap(): Map<String, String> {
        val headers = HashMap<String, String>()
        val authorizationValue =
                "MediaBrowser Client=\"Jellyfin Client\", Device=\"$deviceName\", DeviceId=\"$deviceId\", Version=\"${BuildConfig.CLIENT_VERSION}.0\""
        headers["X-Emby-Authorization"] = authorizationValue
        if (accessToken != null) {
            headers["Authorization"] = "Bearer $accessToken"
        }
        return headers
    }

    override fun userInfo(userId: String): SerenityUser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allAvailableUsers(): MutableList<SerenityUser> {
        val allPublicUsers = fetchAllPublicUsers()
        val allUsers = ArrayList<SerenityUser>()
        for (user in allPublicUsers) {
            val builder = us.nineworlds.serenity.common.rest.impl.SerenityUser.builder()
            val sernityUser = builder.userName(user.name)
                    .userId(user.id)
                    .hasPassword(user.hasPassword)
                    .build()
            allUsers.add(sernityUser)
        }
        return allUsers
    }

    override fun authenticateUser(user: SerenityUser): SerenityUser {
        val authenticatedUser = authenticate(user.userName)

        return us.nineworlds.serenity.common.rest.impl.SerenityUser.builder()
                .accessToken(authenticatedUser.accesToken)
                .userName(user.userName)
                .userId(user.userId)
                .hasPassword(user.hasPassword())
                .build()
    }

    override fun retrieveRootData(): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }

        val call = usersService.usersViews(headerMap(), userId!!)

        val queryResult = call.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createMainMenu(queryResult.items)
    }

    override fun retrieveLibrary(): IMediaContainer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     *
     */
    override fun retrieveItemByCategories(): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.usersViews(headerMap(), userId!!)

        val queryResult = call.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createMainMenu(queryResult.items)
    }

    override fun retrieveCategoriesById(key: String): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = filterService.availableFilters(headerMap(), userId = userId!!, itemId = key)

        val queryResult = call.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createCategory(queryResult.genres!!)
    }

    override fun retrieveItemByIdCategory(key: String, category: String, types: Types): IMediaContainer {
        return retrieveItemByIdCategory(key, category, types, 0, null)
    }

    override fun fetchSimilarItemById(itemId: String, types: Types): IMediaContainer {
        val type = when (types) {
            Types.MOVIES -> "Movie"
            Types.SEASON -> "Season"
            Types.SERIES -> "Series"
            else -> "Episode"
        }

        val call = usersService.fetchSimilarItemById(headerMap(), itemId = itemId, userId = userId!!, includeItemType = "Movie")

        val results = call.executeOrThrow()
        return JellyfinMediaContainerAdaptor().createVideoList(results.items)
    }

    override fun retrieveItemByIdCategory(key: String, category: String, types: Types, startIndex: Int, limit: Int?): IMediaContainer {
        var isPlayed: Boolean? = null

        val genre = when (category) {
            "all", "unwatched" -> null
            else -> category
        }

        val type = when (types) {
            Types.MOVIES -> "Movie"
            Types.SEASON -> "Season"
            Types.SERIES -> "Series"
            else -> "Episode"
        }

        if (userId == null) {
            userId = fetchUserId()
        }

        val call = when (category) {
            "ondeck" -> {
                if (type == "Season" || type == "Series") {
                    usersService.resumableItems(headerMap(), userId = userId!!, parentId = key, includeItemType = "Episode")
                } else {
                    usersService.resumableItems(headerMap(), userId = userId!!, parentId = key, includeItemType = type)
                }
            }
            "recentlyAdded" -> {
                if (type == "Season" || type == "Series") {
                    usersService.latestItems(headerMap(), userId = userId!!, parentId = key, includeItemType = "Episode")
                } else {
                    usersService.latestItems(headerMap(), userId = userId!!, parentId = key, includeItemType = type)
                }
            }
            "unwatched" -> {
                usersService.unwatchedItems(headerMap(), userId = userId!!, parentId = key, includeItemType = type)
            }
            else -> {
                usersService.fetchItemQuery(
                        headerMap(),
                        userId = userId!!,
                        parentId = key,
                        genre = genre,
                        isPlayed = isPlayed,
                        includeItemType = type,
                        startIndex = startIndex,
                        limit = limit
                )
            }
        }

        val results = call.executeOrThrow()
        return JellyfinMediaContainerAdaptor().createVideoList(results.items)
    }

    override fun retrieveItemByCategories(key: String, category: String, secondaryCategory: String): IMediaContainer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveSeasons(key: String): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.fetchItemQuery(
                headerMap(),
                userId = userId!!,
                parentId = key,
                includeItemType = "Season",
                genre = null
        )

        val results = call.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createSeriesList(results.items)
    }

    override fun retrieveMusicMetaData(key: String): IMediaContainer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveEpisodes(key: String): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.fetchItemQuery(
                headerMap(),
                userId = userId!!,
                parentId = key,
                includeItemType = "Episode",
                genre = null
        )

        val results = call.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createVideoList(results.items)
    }

    override fun retrieveMovieMetaData(key: String): IMediaContainer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchMovies(key: String, query: String): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.search(headerMap(), userId!!, query)
        val results = call.executeOrThrow()
        val itemIds = mutableListOf<String>()

        for (searchHint in results.searchHints!!) {
            itemIds.add(searchHint.id!!)
        }

        val itemCall = usersService.fetchItemQuery(
                headerMap(),
                userId = userId!!,
                ids = itemIds.joinToString(separator = ","),
                genre = null,
                parentId = null
        )

        val itemResults = itemCall.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createVideoList(itemResults.items)
    }

    override fun searchEpisodes(key: String, query: String): IMediaContainer? {
        return null
    }

    override fun updateBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
        RetrofitUrlManager.getInstance().setGlobalDomain(baseUrl)
    }

    override fun baseURL(): String = baseUrl

    override fun watched(key: String): Boolean {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.played(headerMap(), userId!!, key)

        val result = call.execute()
        return result.isSuccessful
    }

    override fun unwatched(key: String): Boolean {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = usersService.unplayed(headerMap(), userId!!, key)

        val result = call.execute()
        return result.isSuccessful
    }

    override fun progress(key: String, offset: String): Boolean {
        if (userId == null) {
            userId = fetchUserId()
        }
        var position = offset.toLong()

        position = position.times(10000)

        val call = usersService.progress(headerMap(), userId!!, key, null, position)
        val result = call.execute()

        return result.isSuccessful
    }

    override fun createMediaTagURL(resourceType: String, resourceName: String, identifier: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSectionsURL(key: String, category: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSectionsURL(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSectionsUrl(key: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createMovieMetadataURL(key: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createEpisodesURL(key: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSeasonsURL(key: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createImageURL(url: String, width: Int, height: Int): String {
        return url
    }

    override fun createTranscodeUrl(id: String, offset: Int): String {
        val playSessionId = UUID.randomUUID().toString()
        var startOffset: Long = 0
        if (offset > 0) {
            startOffset = offset.toLong().times(10000)
        }

        return "${baseUrl}Videos/$id/stream.mkv?DeviceId=$deviceId&AudioCodec=aac&VideoCodec=h264&CopyTimeStamps=true&EnableAutoStreamCopy=true&StartTimeTicks=$startOffset&PlaySessionId=$playSessionId"
    }

    override fun reinitialize() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUserImageUrl(user: SerenityUser, width: Int, height: Int): String {
        return "$baseUrl/Users/${user.userId}/Images/Primary?Width=$width&Height=$height"
    }

    override fun startPlaying(itemId: String) {
        if (userId == null) {
            userId = fetchUserId()
        }

        val call = usersService.startPlaying(headerMap(), userId!!, itemId)

        call.execute()
    }

    override fun stopPlaying(itemId: String, offset: Long) {
        if (userId == null) {
            userId = fetchUserId()
        }
        val positionTicks = offset.times(10000)

        val call = usersService.stopPlaying(headerMap(), userId!!, itemId, null, positionTicks.toString())
        call.execute()
    }

    override fun retrieveSeriesById(key: String, categoryId: String): IMediaContainer {
        var genre: String? = null
        val isPlayed: Boolean? = null
        val itemType = "Series"

        genre = when (categoryId) {
            "all", "unwatched" -> null
            else -> categoryId
        }

        if (userId == null) {
            userId = fetchUserId()
        }

        val call = usersService.fetchItemQuery(
                headerMap(),
                userId = userId!!,
                parentId = key,
                genre = genre,
                isPlayed = isPlayed,
                includeItemType = itemType,
                limitCount = 5
        )

        val results = call.executeOrThrow()
        return JellyfinMediaContainerAdaptor().createSeriesList(results.items)
    }

    override fun retrieveSeriesCategoryById(key: String): IMediaContainer {
        if (userId == null) {
            userId = fetchUserId()
        }
        val call = filterService.availableFilters(headerMap(), userId!!, key)

        val queryResult = call.executeOrThrow()

        return JellyfinMediaContainerAdaptor().createCategory(queryResult.genres!!, true)
    }

    fun fetchUserId() = prefs.getString("userId", "")

    fun fetchAccessToken() = prefs.getString("jellyfinAccessToken", "")

    override fun supportsMultipleUsers(): Boolean = true

    private fun pseudoUniqueID(): String {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        var devIDShort = "35" + (Build.BOARD.length % 10) + (Build.BRAND.length % 10)

        devIDShort += if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (Build.SUPPORTED_ABIS[0].length % 10)
        } else {
            (Build.CPU_ABI.length % 10)
        }

        devIDShort +=
            (Build.DEVICE.length % 10) + (Build.MANUFACTURER.length % 10) + (Build.MODEL.length
                    % 10) + (Build.PRODUCT.length % 10)

        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        var serial: String
        try {
            serial = Build::class.java.getField("SERIAL")[null]?.toString() ?: ""

            // Go ahead and return the serial for api => 9
            return UUID(devIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (e: java.lang.Exception) {
            // String needs to be initialized
            Timber.e(JellyfinAPIClient::class.java.simpleName, "getPseudoUniqueID: ", e)
            serial = "ESYDV000" // some value
        }

        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return UUID(devIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

    private fun <T> Call<T>.executeOrThrow(): T {
        val response = execute()
        if (response.isSuccessful) {
            return response.body()
                ?: throw IOException("Response from Jellyfin was null. Response Code: ${response.code()} - ${response.message()}")
        }
        throw IOException("Request to Jellyfin failed with code ${response.code()}, message: ${response.message()}")
    }

}
