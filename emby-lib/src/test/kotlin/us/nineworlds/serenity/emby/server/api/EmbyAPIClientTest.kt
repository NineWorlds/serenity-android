package us.nineworlds.serenity.emby.server.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import us.nineworlds.serenity.emby.server.model.AuthenticationResult

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(27))
class EmbyAPIClientTest {

  lateinit var client: EmbyAPIClient

  @Before
  fun setUp() {
    ShadowLog.stream = System.out

    client = EmbyAPIClient(context = RuntimeEnvironment.application)
    client.updateBaseUrl("http://plexserver:8096")
  }

  @Test
  fun retrieveAllPublicUsers() {
    val result = client.fetchAllPublicUsers()
    assertThat(result).isNotEmpty.hasSize(2)
  }

  @Test fun loginAdminUser() {
    val authenticateResult = authenticate()

    assertThat(authenticateResult).isNotNull()
    assertThat(authenticateResult.accesToken).isNotBlank()
    assertThat(client.serverId).isNotBlank()
    assertThat(client.accessToken).isNotBlank()
    assertThat(client.userId).isNotBlank()
  }

  @Test fun testCurrentUsersViews() {
    val authenticate = authenticate()

    val result = client.currentUserViews()
    assertThat(result.items).isNotEmpty
  }

  @Test fun availableFiltersForCurrentUser() {
    authenticate()

    val currentViews = client.currentUserViews()
    val id = currentViews.items[2].id
    val result = client.filters(itemId = id)
    assertThat(result).isNotNull()
  }

  @Test fun generateMainMenuForCurrentUser() {
    authenticate()

    val result = client.retrieveRootData()
    assertThat(result).isNotNull
    assertThat(result!!.directories).isNotEmpty
  }

  @Test fun createCategoriesForParentId() {
    authenticate()

    val result = client.retrieveItemByCategories()
    val parentId = result.directories[1].key

    val categories = client.retrieveItemByIdCategory(parentId)

    assertThat(categories.directories).isNotEmpty
  }

  @Test fun fetchAllMovies() {
    authenticate()

    val result = client.retrieveItemByCategories()
    val parentId = result.directories[1].key

    val movies = client.retrieveItemByIdCategory(parentId, "all")

    assertThat(movies.videos).isNotEmpty
  }

  @Test fun fetchItemsForMoviesForUser() {
    authenticate()

    val result = client.retrieveRootData()

    val key = result.directories[1].key

    val itemResult = client.fetchItemQuery(key)

   // val itemResult = client.fetchItem("d385ceb98f6a84e4c9c8f3301a5c6207")

   // assertThat(itemResult.items).isNotEmpty
  }

  fun authenticate(): AuthenticationResult {
    val users = client.fetchAllPublicUsers()
    val user = users[0]

    val authenticateResult = client.authenticate(user.name!!, "")
    return authenticateResult
  }
}