package us.nineworlds.serenity.jobs.videos

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import dagger.Module
import dagger.Provides
import org.apache.commons.lang3.RandomStringUtils
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.events.users.AuthenticatedUserEvent
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.SerenityModule
import us.nineworlds.serenity.jobs.AuthenticateUserJob
import us.nineworlds.serenity.test.InjectingTest
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AuthenticateUserJobTest : InjectingTest() {

  @Rule @JvmField public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Mock lateinit var mockUser: SerenityUser

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  lateinit var job: AuthenticateUserJob

  val expectedVideoId = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = AuthenticateUserJob(mockUser)
  }

  @Test
  fun onRunAuthenticatesUser() {

    job.onRun()

    verify(mockClient).authenticateUser(mockUser)
  }

  @Test
  fun onRunPostsAuthenticationEvent() {
    job.onRun()

    verify(mockEventBus).post(any<AuthenticatedUserEvent>())
  }

  override fun getModules(): MutableList<Any> = mutableListOf(AndroidModule(RuntimeEnvironment.application),
      TestModule())

  @Module(injects = arrayOf(AuthenticateUserJobTest::class),
      includes = arrayOf(SerenityModule::class, TestingModule::class),
      library = true,
      overrides = true)
  inner class TestModule {

    @Provides fun providesEventBus(): EventBus = mockEventBus
  }
}
