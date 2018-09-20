package us.nineworlds.serenity.emby.server.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EmbyAPIClientTest {

  lateinit var client: EmbyAPIClient

  @Before
  fun setUp() {
    client = EmbyAPIClient("http://plexserver:8096/")
  }

  @Test
  fun retrieveAllPublicUsers() {
    val result = client.fetchAllPublicUsers()
    assertThat(result).isNotEmpty.hasSize(2)
  }

  @Test fun loginAdminUser() {
    val users = client.fetchAllPublicUsers()
    val user = users[0]

    val authenticateResult = client.authenticate(user.name!!, "")

    assertThat(authenticateResult).isNotNull()
    assertThat(authenticateResult.accesToken).isNotBlank()
    assertThat(client.serverId).isNotBlank()
    assertThat(client.accessToken).isNotBlank()
    assertThat(client.userId).isNotBlank()
  }
}