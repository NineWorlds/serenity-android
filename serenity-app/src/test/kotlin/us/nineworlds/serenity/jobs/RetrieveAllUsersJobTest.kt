package us.nineworlds.serenity.jobs

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.events.users.AllUsersEvent
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class RetrieveAllUsersJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  lateinit var job: RetrieveAllUsersJob

  @Before
  override fun setUp() {
    super.setUp()
    job = RetrieveAllUsersJob()
    job.eventBus = mockEventBus
  }

  @Test
  fun onRunFetchesAllPublicUsersFromServer() {
    job.onRun()

    verify(mockClient).allAvailableUsers()
  }

  @Test
  fun onRunPostsAllUsersEvent() {
    job.onRun()

    verify(mockClient).allAvailableUsers()
    verify(mockEventBus).post(any<AllUsersEvent>())
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

}
