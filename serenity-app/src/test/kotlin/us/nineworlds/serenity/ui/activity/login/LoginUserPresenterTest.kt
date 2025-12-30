package us.nineworlds.serenity.ui.activity.login

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.core.repository.LoginRepository
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginUserPresenterTest : InjectingTest() {

    private companion object {
        private val mockView: LoginUserContract.LoginUserView = mockk(relaxed = true)
        private val mockServer = mockk<Server>(relaxed = true)
        private val mockSerenityUser = mockk<SerenityUser>(relaxed = true)
        private val mockRepository = mockk<LoginRepository>(relaxed = true)

        private val mockSerenityClient = mockk<SerenityClient>(relaxed = true)
    }

    private lateinit var presenter: LoginUserPresenter

    @Before
    override fun setUp() {
        clearAllMocks()
        super.setUp()
        presenter = LoginUserPresenter()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init preseter updates client with port`() {
        every { mockServer.ipAddress } returns "192.168.0.1"

        presenter.initPresenter(mockServer)

        verify { mockSerenityClient.updateBaseUrl(any()) }
        verify { mockServer.ipAddress }
    }

    @Test
    fun `init presenter updates client without port`() {
        every { mockServer.port } returns "9999"
        every { mockServer.ipAddress } returns "192.168.0.1"

        presenter.initPresenter(mockServer)

        verify { mockSerenityClient.updateBaseUrl("http://192.168.0.1:9999/") }
        verify { mockServer.ipAddress }
        verify(atLeast = 1) { mockServer.port }
    }

    @Test
    fun `retrieve all users displays users on success`() = runTest {
        val expectedUsers = listOf(mockSerenityUser)
        coEvery { mockRepository.loadAllUsers() } returns Result.Success(expectedUsers)
        presenter.attachView(mockView)

        presenter.retrieveAllUsers()

        coVerify { mockRepository.loadAllUsers() }
        verify { mockView.displayUsers(expectedUsers) }
    }

    @Test
    fun `load user without password authenticates and launches next screen`() = runTest {
        coEvery { mockRepository.authenticateUser(any()) } returns Result.Success(mockSerenityUser)
        presenter.attachView(mockView)

        presenter.loadUser(mockSerenityUser)

        coVerify { mockRepository.authenticateUser(mockSerenityUser) }
        verify { mockView.launchNextScreen() }
    }

    @Test
    fun `load user with password authenticates and launches next screen`() = runTest {
        coEvery { mockRepository.authenticateUser(any(), any()) } returns Result.Success(mockSerenityUser)
        presenter.attachView(mockView)

        presenter.loadUser(mockSerenityUser, "password")

        coVerify { mockRepository.authenticateUser(mockSerenityUser, "password") }
        verify { mockView.launchNextScreen() }
    }

    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule(), TestModule())
    }

    class TestModule : Module() {
        init {
            bind(LoginRepository::class.java).toInstance(mockRepository)
            bind(SerenityClient::class.java).toInstance(mockSerenityClient)
        }
    }
}
