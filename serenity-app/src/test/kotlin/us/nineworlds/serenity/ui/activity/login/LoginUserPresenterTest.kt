package us.nineworlds.serenity.ui.activity.login

import com.birbit.android.jobqueue.JobManager
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.greenrobot.eventbus.EventBus
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.events.users.AllUsersEvent
import us.nineworlds.serenity.events.users.AuthenticatedUserEvent
import us.nineworlds.serenity.jobs.AuthenticateUserJob
import us.nineworlds.serenity.jobs.RetrieveAllUsersJob
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import us.nineworlds.serenity.ui.activity.login.LoginRepository.*
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class LoginUserPresenterTest : InjectingTest() {

  @Rule
  @JvmField
  val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Mock
  lateinit var mockView: LoginUserContract.LoginUserView

  @Mock
  lateinit var mockServer: Server

  @Mock
  lateinit var mockSerenityUser: SerenityUser

  @Mock
  lateinit var mockRepository: LoginRepository

  @Inject
  lateinit var mockSerenityClient: SerenityClient

  @Inject
  lateinit var mockJobManager: JobManager

  lateinit var presenter: LoginUserPresenter

  @Before
  override fun setUp() {
    super.setUp()
    presenter = LoginUserPresenter()
    Dispatchers.setMain(Dispatchers.Unconfined)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun initPresenterUpdatesClientWithExpectedUrl() {
    doReturn("192.168.0.1").whenever(mockServer).ipAddress

    presenter.initPresenter(mockServer)

    verify(mockSerenityClient).updateBaseUrl("http://192.168.0.1:8096/")
    verify(mockServer).ipAddress
  }

  @Test
  fun initPresenterUpdatesClientWithExpectedUrlWhenServerPortIsNotNull() {
    doReturn("9999").whenever(mockServer).port
    doReturn("192.168.0.1").whenever(mockServer).ipAddress

    presenter.initPresenter(mockServer)

    verify(mockSerenityClient).updateBaseUrl("http://192.168.0.1:9999/")
    verify(mockServer).ipAddress
    verify(mockServer, atLeastOnce()).port
  }

  @Test
  fun allUsersJobIsCreatedAndAddedToJobManager() = runBlocking {
    val expectedUsers = listOf(mockSerenityUser)
    doReturn(Result.Success(expectedUsers)).whenever(mockRepository).loadAllUsers()
    presenter.attachView(mockView)

    presenter.retrieveAllUsers()

    verify(mockRepository).loadAllUsers()
    verify(mockView).displayUsers(expectedUsers)
  }

  @Test
  fun loadUserAddsJob() = runBlocking {
    doReturn(Result.Success(mockSerenityUser)).whenever(mockRepository).authenticateUser(any())
    presenter.attachView(mockView)

    presenter.loadUser(mockSerenityUser)

    verify(mockRepository).authenticateUser(mockSerenityUser)
    verify(mockView).launchNextScreen()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(LoginRepository::class.java).toInstance(mockRepository)
    }
  }

}