package us.nineworlds.serenity.emby.server.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.common.rest.impl.SerenityUser.Builder
import us.nineworlds.serenity.emby.moshi.LocalDateJsonAdapter
import us.nineworlds.serenity.emby.server.model.*
import java.lang.IllegalStateException

class EmbyAPIClient(baseUrl: String = "http://localhost:8096"): SerenityClient {

  val usersService: UsersService
  val filterService: FilterService

  var baseUrl: String
  var accessToken: String? = null
  lateinit var serverId: String
  lateinit var userId: String

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
  }

  fun fetchAllPublicUsers() : List<PublicUserInfo> {
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

  private fun headerMap(): Map<String, String> {
    val headers = HashMap<String, String>()
    val authorizationValue = "MediaBrowser Client=\"Android\", Device=\"Samsung Galaxy SIII\", DeviceId=\"xxx\", Version=\"1.0.0.0\""
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
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveLibrary(): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveSections(): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveSections(key: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveSections(key: String?, category: String?): IMediaContainer {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveSections(key: String?, category: String?, secondaryCategory: String?): IMediaContainer {
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

  override fun watched(key: String?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun unwatched(key: String?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun progress(key: String?, offset: String?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createTranscodeUrl(id: String?, offset: Int): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun reinitialize() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun createUserImageUrl(user: SerenityUser, width: Int, height: Int): String {
    return "$baseUrl/Emby/Users/${user.userId}/Images/Primary?Width=$width&Height=$height"
  }
}