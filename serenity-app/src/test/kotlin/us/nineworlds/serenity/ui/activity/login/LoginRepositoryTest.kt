package us.nineworlds.serenity.ui.activity.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
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
import java.io.IOException

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
        every { mockSerenityClient.allAvailableUsers() } returns expectedResult

        val result = repository.loadAllUsers()

        assertThat((result as Result.Success<List<SerenityUser>>).data).isEqualTo(expectedResult)

        verify { mockSerenityClient.allAvailableUsers() }
    }

    @Test
    fun `loadAllUsers returns error on client failure`() = runTest {
        every { mockSerenityClient.allAvailableUsers() } throws IOException("Connection failed")

        val result = repository.loadAllUsers()

        assertThat(result).isInstanceOf(Result.Error::class)
        assertThat((result as Result.Error).exception).isInstanceOf(IOException::class)
    }

    @Test
    fun `authenticate user without password returns success`() = runTest {
        val expectedResult = mockSerenityUser
        every { mockSerenityClient.authenticateUser(any(), null) } returns expectedResult

        val result = repository.authenticateUser(expectedResult, null)

        assertThat((result as Result.Success<SerenityUser>).data).isEqualTo(expectedResult)
        verify { mockSerenityClient.authenticateUser(expectedResult, null) }
    }

    @Test
    fun `authenticate user with password returns success`() = runTest {
        val expectedResult = mockSerenityUser
        every { mockSerenityClient.authenticateUser(any(), "password") } returns expectedResult

        val result = repository.authenticateUser(expectedResult, "password")

        assertThat((result as Result.Success<SerenityUser>).data).isEqualTo(expectedResult)
        verify { mockSerenityClient.authenticateUser(expectedResult, "password") }
    }

    @Test
    fun `authenticate user returns error on client failure`() = runTest {
        every { mockSerenityClient.authenticateUser(any(), any()) } throws IOException("Authentication failed")

        val result = repository.authenticateUser(mockSerenityUser, "password")

        assertThat(result).isInstanceOf(Result.Error::class)
        assertThat((result as Result.Error).exception).isInstanceOf(IOException::class)
    }
}
