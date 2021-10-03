package us.nineworlds.serenity.ui.activity.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.common.repository.Result

class LoginRepositoryTest {

    private val mockSerenityClient = mock<SerenityClient>()
    private val mockSerenityUser = mock<SerenityUser>()

    private lateinit var repository: LoginRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = LoginRepository(mockSerenityClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadAllUsersWithSuccess() {
        val expectedResult = listOf(mockSerenityUser)
        doReturn(expectedResult).whenever(mockSerenityClient).allAvailableUsers()

        val result = runBlocking {
            repository.loadAllUsers()
        }

        assertThat((result as Result.Success<List<SerenityUser>>).data).isEqualTo(expectedResult)
        verify(mockSerenityClient).allAvailableUsers()
    }

    @Test
    fun testAuthenticateUser() {
        val expectedResult = mockSerenityUser
        doReturn(expectedResult).whenever(mockSerenityClient).authenticateUser(any())

        val result = runBlocking {
            repository.authenticateUser(expectedResult)
        }

        assertThat((result as Result.Success<SerenityUser>).data).isEqualTo(expectedResult)
        verify(mockSerenityClient).authenticateUser(mockSerenityUser)
    }

}