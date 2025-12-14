package us.nineworlds.serenity.ui.activity.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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

    private val mockSerenityClient = mockk<SerenityClient>(relaxed = true)
    private val mockSerenityUser = mockk<SerenityUser>(relaxed = true)

    private lateinit var repository: LoginRepository

    @Before
    fun setUp() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = LoginRepository(mockSerenityClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAllUsers returns success on client success`() = runTest {
        val expectedResult = listOf(mockSerenityUser)
        coEvery { mockSerenityClient.allAvailableUsers() } returns expectedResult

        val result = repository.loadAllUsers()

        assertThat((result as Result.Success<List<SerenityUser>>).data).isEqualTo(expectedResult)

        coVerify { mockSerenityClient.allAvailableUsers() }
    }

    @Test
    fun `authenticate user without password returns success`() = runTest {
        val expectedResult = mockSerenityUser
        coEvery { mockSerenityClient.authenticateUser(any(), null) } returns expectedResult

        val result = repository.authenticateUser(expectedResult, null)

        assertThat((result as Result.Success<SerenityUser>).data).isEqualTo(expectedResult)
        coVerify { mockSerenityClient.authenticateUser(expectedResult, null) }
    }

    @Test
    fun `authenticate user with password returns success`() = runTest {
        val expectedResult = mockSerenityUser
        coEvery { mockSerenityClient.authenticateUser(any(), "password") } returns expectedResult

        val result = repository.authenticateUser(expectedResult, "password")

        assertThat((result as Result.Success<SerenityUser>).data).isEqualTo(expectedResult)
        coVerify { mockSerenityClient.authenticateUser(expectedResult, "password") }
    }
}