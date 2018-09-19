package us.nineworlds.serenity.emby.server.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient

class EmbyAPIClient(baseUrl: String): SerenityClient {

  val usersService: UsersService

  init {
    val logger = HttpLoggingInterceptor()
    val okClient = OkHttpClient.Builder()
    logger.level = HttpLoggingInterceptor.Level.HEADERS
    okClient.addInterceptor(logger)
    okClient.cache(null)

    val builder = Retrofit.Builder()
    val embyRetrofit = builder.baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .client(okClient.build())
        .build()

    usersService = embyRetrofit.create(UsersService::class.java)
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

  override fun baseURL(): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

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

}