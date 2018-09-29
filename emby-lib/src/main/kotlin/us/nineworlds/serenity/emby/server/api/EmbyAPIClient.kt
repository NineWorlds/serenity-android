package us.nineworlds.serenity.emby.server.api

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import github.nisrulz.easydeviceinfo.base.EasyIdMod
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.emby.adapters.MediaContainerAdaptor
import us.nineworlds.serenity.emby.moshi.LocalDateJsonAdapter
import us.nineworlds.serenity.emby.server.model.AuthenticateUserByName
import us.nineworlds.serenity.emby.server.model.AuthenticationResult
import us.nineworlds.serenity.emby.server.model.PublicUserInfo
import us.nineworlds.serenity.emby.server.model.QueryFilters
import us.nineworlds.serenity.emby.server.model.QueryResult

class EmbyAPIClient(context: Context, baseUrl: String = "http://localhost:8096") : SerenityClient {

  val usersService: UsersService
  val filterService: FilterService

  var baseUrl: String
  var accessToken: String? = null
  lateinit var serverId: String
  lateinit var userId: String
  lateinit var deviceId: String
  lateinit var deviceName: String

  init {
    this.baseUrl = baseUrl
    val logger = HttpLoggingInterceptor()

    val okClient = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
    logger.level = HttpLoggingInterceptor.Level.BODY
    okClient.addInterceptor(logger)
    okClient.cache(null)

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(LocalDateTime::class.java, LocalDateJsonAdapter()).build()

    val builder = Retrofit.Builder()
    val embyRetrofit = builder.baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okClient.build())
        .build()

    usersService = embyRetrofit.create(UsersService::class.java)
    filterService = embyRetrofit.create(FilterService::class.java)

    val easyDeviceMod = EasyDeviceMod(context)

    deviceId = EasyIdMod(context).pseudoUniqueID
    deviceName = "${easyDeviceMod.manufacturer} ${easyDeviceMod.model}"
    Log.d(this::class.java.simpleName, "Device Id: $deviceId")
    Log.d(this::class.java.simpleName, "Device Name : $deviceName")
  }

  fun fetchAllPublicUsers(): List<PublicUserInfo> {
    val allPublicUsers = usersService.allPublicUsers()
    return allPublicUsers.execute().body()!!
  }

  fun userImageUrl(userId: String): String {
    return "$baseUrl/Users/$userId/Images/Primary"
  }

  fun authenticate(userName: String, password: String = ""): AuthenticationResult {
    val authenticationResul = AuthenticateUserByName(userName, "", password, password)
    val call = usersService.authenticate(authenticationResul, headerMap())
    val response = call.execute()
    if (response.isSuccessful) {
      val body = response.body()
      accessToken = body!!.accesToken
      serverId = body.serverId
      userId = body.userInfo.id!!
      return response.body()!!
    }
    throw IllegalStateException("error logging user in to Emby Server")
  }

  fun currentUserViews(): QueryResult {
    val call = usersService.usersViews(headerMap(), userId)
    return call.execute().body()!!
  }

  fun filters(itemId: String? = null, tags: List<String>? = null): QueryFilters {
    val call = filterService.availableFilters(headerMap(), userId)
    return call.execute().body()!!
  }

  fun fetchItem(id: String): QueryResult {
    val call = usersService.fetchItem(headerMap(), userId, id)

    return call.execute().body()!!
  }

  fun fetchItemQuery(id: String): QueryResult {
    val call = usersService.fetchItemQuery(headerMap(), userId, id, genre = null)

    return call.execute().body()!!
  }

  private fun headerMap(): Map<String, String> {
    val headers = HashMap<String, String>()
    val authorizationValue = "MediaBrowser Client=\"Android\", Device=\"$deviceName\", DeviceId=\"$deviceId\", Version=\"1.0.0.0\""
    headers["X-Emby-Authorization"] = authorizationValue
    if (accessToken != null) {
      headers["X-Emby-Token"] = accessToken!!
    }
    return headers
  }

  override fun userInfo(userId: String?): SerenityUser {
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
    val call = usersService.usersViews(headerMap(), userId)

    val queryResult = call.execute().body()

    return MediaContainerAdaptor().createMainMenu(queryResult!!.items)
  }

  override fun retrieveLibrary(): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveItemByCategories(): IMediaContainer {
    val call = usersService.usersViews(headerMap(), userId)

    val queryResult = call.execute().body()

    return MediaContainerAdaptor().createMainMenu(queryResult!!.items)
  }

  override fun retrieveItemByIdCategory(key: String?): IMediaContainer {
    val call = filterService.availableFilters(headerMap(), userId, key)

    val queryResult = call.execute().body()

    return MediaContainerAdaptor().createCategory(queryResult!!.genres!!)
  }

  override fun retrieveItemByIdCategory(key: String, category: String): IMediaContainer {
    var genre: String? = null
    var isPlayed: Boolean? = null

    genre = when (category) {
      "all", "unwatched" -> null
      else -> category
    }

    val call = if (category == "ondeck") {
      usersService.resumableItems(headerMap(), userId = userId, parentId = key)
    } else if (category == "recentlyAdded") {
      usersService.latestItems(headerMap(), userId = userId, parentId = key)
    } else if (category == "unwatched") {
      usersService.unwatchedItems(headerMap(), userId = userId, parentId = key)
    } else {
      usersService.fetchItemQuery(headerMap(), userId = userId, parentId = key, genre = genre, isPlayed = isPlayed)
    }

    val results = call.execute().body()
    return MediaContainerAdaptor().createVideoList(results!!.items)
  }

  override fun retrieveItemByCategories(key: String?, category: String?, secondaryCategory: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveSeasons(key: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveMusicMetaData(key: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveEpisodes(key: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveMovieMetaData(key: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun searchMovies(key: String?, query: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun searchEpisodes(key: String?, query: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun updateBaseUrl(baseUrl: String) {
    this.baseUrl = baseUrl
    RetrofitUrlManager.getInstance().setGlobalDomain(baseUrl)
  }

  override fun baseURL(): String = baseUrl

  override fun watched(key: String): Boolean {
    val call = usersService.played(headerMap(), userId, key)

    val result = call.execute()
    return result.isSuccessful
  }

  override fun unwatched(key: String): Boolean {
    val call = usersService.unplayed(headerMap(), userId, key)

    val result = call.execute()
    return result.isSuccessful
  }

  override fun progress(key: String, offset: String): Boolean {
    var position = offset.toLong()

    position = position.times(10000)

    val call = usersService.progress(headerMap(), userId, key, null, position)
    val result = call.execute();

    return result.isSuccessful
  }

  override fun createMediaTagURL(resourceType: String?, resourceName: String?, identifier: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createSectionsURL(key: String?, category: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createSectionsURL(): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createSectionsUrl(key: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createMovieMetadataURL(key: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createEpisodesURL(key: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createSeasonsURL(key: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createImageURL(url: String?, width: Int, height: Int): String {
    return url!!
  }

  override fun createTranscodeUrl(id: String, offset: Int): String {
    var startOffset: Long = 0
    if (offset > 0) {
      startOffset = offset.toLong().times(10000)
    }

    return "${baseUrl}emby/Videos/$id/stream.mkv?AudioCodec=aac&VideoCodec=h264&EnableAutoStreamCopy=true&StartTimeTicks=$startOffset"
  }

  override fun reinitialize() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createUserImageUrl(user: SerenityUser, width: Int, height: Int): String {
    return "$baseUrl/Emby/Users/${user.userId}/Images/Primary?Width=$width&Height=$height"
  }

  override fun startPlaying(itemId: String) {
    val call = usersService.startPlaying(headerMap(), userId, itemId)

    call.execute()
  }

  override fun stopPlaying(itemId: String) {
    val call = usersService.stopPlaying(headerMap(), userId, itemId)
    call.execute()
  }
}