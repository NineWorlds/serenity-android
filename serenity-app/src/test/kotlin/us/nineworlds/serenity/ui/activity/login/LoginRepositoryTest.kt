package us.nineworlds.serenity.ui.activity.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.core.repository.LoginRepository

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryTest {

    private companion object {
        private val mockSerenityClient = mockk<SerenityClient>()
        private val mockSerenityUser = mockk<SerenityUser>()
    }

    private lateinit var repository: LoginRepository

    @Before
    fun setUp() {
        clearAllMocks()
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = LoginRepository(mockSerenityClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadAllUsersWithSuccess() = runTest(UnconfinedTestDispatcher()) {
        val expectedResult = listOf(mockSerenityUser)
        every { mockSerenityClient.allAvailableUsers() } returns expectedResult

        val result = repository.loadAllUsers()

        assertThat((result as Result.Success<List<SerenityUser>>).data).isEqualTo(expectedResult)

        verify { mockSerenityClient.allAvailableUsers() }
    }

    @Test
    fun testAuthenticateUser() = runTest(UnconfinedTestDispatcher()) {
        val expectedResult = mockSerenityUser
        every { mockSerenityClient.authenticateUser(any()) } returns expectedResult

        val result = repository.authenticateUser(expectedResult)

        assertThat((result as Result.Success<SerenityUser>).data).isEqualTo(expectedResult)
        verify { mockSerenityClient.authenticateUser(expectedResult) }
    }

}