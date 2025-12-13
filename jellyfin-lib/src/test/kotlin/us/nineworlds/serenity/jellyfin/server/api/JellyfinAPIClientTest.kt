package us.nineworlds.serenity.jellyfin.server.api

import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.jellyfin.server.model.AuthenticationResult

@RunWith(RobolectricTestRunner::class)
class JellyfinAPIClientTest {

  private lateinit var client: JellyfinAPIClient

  @Before
  fun setUp() {
    ShadowLog.stream = System.out

    client = JellyfinAPIClient(context = ApplicationProvider.getApplicationContext())
    client.updateBaseUrl("http://192.168.68.95:8096/emby/")
  }

  @Test
  fun retrieveAllPublicUsers() {
    val result = client.fetchAllPublicUsers()
    assertThat(result).hasSize(2)
  }

  @Test fun loginAdminUser() {
    val authenticateResult = authenticate()

    assertThat(authenticateResult).isNotNull()
    assertThat(authenticateResult.accesToken).isNotEmpty()
    assertThat(client.serverId).isNotEmpty()
    assertThat(client.accessToken).isNotNull()
    assertThat(client.userId).isNotNull().isNotEmpty()
  }

  @Test fun testCurrentUsersViews() {
    authenticate()

    val result = client.currentUserViews()
    assertThat(result.items).isNotEmpty()
  }

  @Test fun availableFiltersForCurrentUser() {
    authenticate()

    val currentViews = client.currentUserViews()
    val id = currentViews.items[1].id
    val result = client.filters(itemId = id)
    assertThat(result).isNotNull()
  }

  @Test fun generateMainMenuForCurrentUser() {
    authenticate()

    val result = client.retrieveRootData()
    assertThat(result).isNotNull()
    assertThat(result.directories).isNotEmpty()
  }

  @Test fun createCategoriesForParentId() {
    authenticate()

    val result = client.retrieveItemByCategories()
    val parentId = result.directories[1].key
    val type = result.directories[1].type

    val categories = client.retrieveCategoriesById(parentId)

    assertThat(categories.directories).isNotEmpty()
  }

  @Test fun fetchAllMovies() {
    authenticate()

    val result = client.retrieveItemByCategories()
    val parentId = result.directories[2].key

    val movies = client.retrieveItemByIdCategory(parentId, "all", Types.EPISODE)

    assertThat(movies.videos).isNotEmpty()
  }

  @Test fun fetchAllLatestMovies() {
    authenticate()

    val result = client.retrieveRootData()

    val key = result.directories[0].key

    val itemResult = client.retrieveItemByIdCategory(key, "recentlyAdded", Types.MOVIES)

    assertThat(itemResult.videos).isNotEmpty()

  }

  private fun authenticate(): AuthenticationResult {
    val users = client.fetchAllPublicUsers()
    val user = users[0]

    return client.authenticate(user.name!!, "")
  }
}