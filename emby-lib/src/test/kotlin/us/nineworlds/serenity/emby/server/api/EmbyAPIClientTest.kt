package us.nineworlds.serenity.emby.server.api

import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.common.rest.impl.SerenityUser
import us.nineworlds.serenity.emby.server.model.AuthenticationResult

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class EmbyAPIClientTest {
    private lateinit var client: EmbyAPIClient
    private lateinit var prefs: SharedPreferences

    @Before
    fun setUp() {
        ShadowLog.stream = System.out

        client = EmbyAPIClient(context = ApplicationProvider.getApplicationContext())
        // Update this for local testing of the client and make sure it works
        client.updateBaseUrl("http://192.168.68.97:8096")
        prefs = client.prefs
    }

    @Test
    fun retrieveAllPublicUsers() {
        val result = client.fetchAllPublicUsers()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun loginAdminUser() {
        val authenticateResult = authenticateWithPassword()

        assertThat(authenticateResult).isNotNull()
        assertThat(authenticateResult.accesToken).isNotEmpty()
        assertThat(client.serverId).isNotEmpty()
        assertThat(client.accessToken).isNotNull()
        assertThat(client.userId).isNotNull().isNotEmpty()
    }

    @Test
    fun loginUserWithPassword() {
        // This needs to be the url to the server you want to test against
        client.updateBaseUrl("http://yourserver:yourport")

        val users = client.allAvailableUsers()
        val user = users.first { it.hasPassword() }
        client.authenticateUser(user, "unknownpassword")

        val password = prefs.getString("emby_${user.userId}_password", null)
        assertThat(password).isNotNull().isEqualTo("unknownpassword")
    }

    @Test
    fun testCurrentUsersViews() {
        authenticateWithPassword()

        val result = client.currentUserViews()
        assertThat(result.items).isNotEmpty()
    }

    @Test
    fun availableFiltersForCurrentUser() {
        authenticateWithPassword()

        val currentViews = client.currentUserViews()
        val id = currentViews.items[1].id
        val result = client.filters(itemId = id)
        assertThat(result).isNotNull()
    }

    @Test
    fun generateMainMenuForCurrentUser() {
        authenticateWithPassword()

        val result = client.retrieveRootData()
        assertThat(result).isNotNull()
        assertThat(result.directories).isNotEmpty()
    }

    @Test
    fun createCategoriesForParentId() {
        authenticateWithPassword()

        val result = client.retrieveItemByCategories()
        val parentId = result.directories[1].key
        val type = result.directories[1].type

        val categories = client.retrieveCategoriesById(parentId)

        assertThat(categories.directories).isNotEmpty()
    }

    @Test
    fun fetchAllMovies() {
        authenticateWithPassword()

        val result = client.retrieveItemByCategories()
        val parentId = result.directories[1].key

        val movies = client.retrieveItemByIdCategory(parentId, "all", Types.EPISODE)

        assertThat(movies.videos).isNotEmpty()
    }

    @Test
    fun fetchAllLatestMovies() {
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

    // This test is not setup to run outside of a developers machine. It
    // needs an active emby server to connect to.  To authenticate update
    // the password, and the user selection code.
    private fun authenticateWithPassword(): AuthenticationResult {
        val users = client.allAvailableUsers()
        val user = users.first { it.hasPassword() }

        return client.authenticate(user.userName, "unknownpassword")
    }
}
