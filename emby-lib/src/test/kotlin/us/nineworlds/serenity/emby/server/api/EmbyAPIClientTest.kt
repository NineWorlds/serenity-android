package us.nineworlds.serenity.emby.server.api

import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.fail
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.common.rest.impl.SerenityUser
import us.nineworlds.serenity.emby.BuildConfig
import us.nineworlds.serenity.emby.server.model.AuthenticationResult
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class EmbyAPIClientTest {
    private lateinit var client: EmbyAPIClient
    private lateinit var prefs: SharedPreferences
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        ShadowLog.stream = System.out

        mockWebServer = MockWebServer()
        mockWebServer.start(8096)

        val baseUrl = when (BuildConfig.TEST_MODE) {
            "LIVE", "CAPTURE" -> BuildConfig.SERVER_URL
            else -> mockWebServer.url("/").toString()
        }

        val interceptors = if (BuildConfig.TEST_MODE == "CAPTURE") {
            listOf(EmbyResponseCaptureInterceptor(File("src/test/resources/mock-data")))
        } else {
            emptyList()
        }

        client = EmbyAPIClient(
            context = ApplicationProvider.getApplicationContext(),
            baseUrl = baseUrl,
            additionalInterceptors = interceptors
        )
        prefs = client.prefs
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun enqueueResponse(fileName: String) {
        if (BuildConfig.TEST_MODE == "MOCK") {
            val inputStream = javaClass.classLoader?.getResourceAsStream("mock-data/$fileName")
                ?: throw IllegalArgumentException("Mock file $fileName not found")
            val json = inputStream.bufferedReader().use { it.readText() }
            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(json))
        }
    }

    @Test
    fun retrieveAllPublicUsers() {
        enqueueResponse("emby_Users_Public.json")

        val result = client.fetchAllPublicUsers()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun loginAdminUser() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")

        val authenticateResult = authenticateWithPassword()

        assertThat(authenticateResult).isNotNull()
        assertThat(authenticateResult.accesToken).isNotEmpty()
        assertThat(client.serverId).isNotEmpty()
        assertThat(client.accessToken).isNotNull()
        assertThat(client.userId).isNotNull().isNotEmpty()
    }

    @Test
    fun loginUserWithPassword() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")

        val users = client.allAvailableUsers()
        val user = users.find { user -> user.userName == BuildConfig.TEST_USER }

        authenticateWithPassword()

        val password = prefs.getString("emby_${user?.userId}_password", null)
        assertThat(password).isNotNull().isEqualTo(BuildConfig.TEST_PASSWORD)
    }

    @Test
    fun testCurrentUsersViews() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Views.json")

        authenticateWithPassword()

        val result = client.currentUserViews()
        assertThat(result.items).isNotEmpty()
    }

    @Test
    fun availableFiltersForCurrentUser() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Views.json")
        enqueueResponse("emby_Genres.json")

        authenticateWithPassword()

        val currentViews = client.currentUserViews()
        val id = currentViews.items[1].id
        val result = client.filters(itemId = id)
        assertThat(result).isNotNull()
    }

    @Test
    fun generateMainMenuForCurrentUser() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Views.json")

        authenticateWithPassword()

        val result = client.retrieveRootData()
        assertThat(result).isNotNull()
        assertThat(result.directories).isNotEmpty()
    }

    @Test
    fun createCategoriesForParentId() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Views.json")
        enqueueResponse("emby_Genres.json")

        authenticateWithPassword()

        val result = client.retrieveItemByCategories()
        val parentId = result.directories[1].key

        val categories = client.retrieveCategoriesById(parentId)

        assertThat(categories.directories).isNotEmpty()
    }

    @Test
    fun fetchAllMovies() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Views.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Items.json")

        authenticateWithPassword()

        val result = client.retrieveItemByCategories()
        val parentId = result.directories[1].key

        val movies = client.retrieveItemByIdCategory(parentId, "all", Types.EPISODE)

        assertThat(movies.videos).isNotEmpty()
    }

    @Test
    fun fetchAllLatestMovies() {
        enqueueResponse("emby_Users_Public.json")
        enqueueResponse("emby_Users_AuthenticateByName.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Views.json")
        enqueueResponse("emby_Users_a99be67f778f4ebd82d22ae04153520e_Items.json")

        authenticateWithPassword()

        val result = client.retrieveRootData()

        val key = result.directories[0].key

        val itemResult = client.retrieveItemByIdCategory(key, "recentlyAdded", Types.MOVIES)

        assertThat(itemResult.videos).isNotEmpty()
    }

    private fun authenticate(): AuthenticationResult {
        val users = client.allAvailableUsers()
        val user = users.first { !it.hasPassword() }
        val serenityUser =
            SerenityUser
                .builder()
                .userId(user.userId)
                .userName(user.userName)
                .hasPassword(user.hasPassword())
                .build()

        client.authenticateUser(serenityUser)
        return client.authenticate(user.userName, "")
    }

    private fun authenticateWithPassword(): AuthenticationResult {
        val users = client.allAvailableUsers()
        val user = users.find { user -> user.userName == BuildConfig.TEST_USER }
        if (user == null) {
            fail("Unable to find user")
        }

        return client.authenticate(user.userName, BuildConfig.TEST_PASSWORD)
    }
}
