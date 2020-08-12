package us.nineworlds.serenity.jobs

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
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
import org.robolectric.annotation.LooperMode
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.events.SeasonsRetrievalEvent
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SeasonsRetrievalJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  lateinit var job: SeasonsRetrievalJob

  private val expectedId: String = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = SeasonsRetrievalJob(expectedId)
    job.eventBus = mockEventBus
  }

  @Test
  fun onRunFetchesCategoriesForTheSpecifiedId() {
    job.onRun()

    verify(mockClient).retrieveSeasons(expectedId)
  }

  @Test
  fun onRunFetchesCategoriesAndPostsMainCategoryEvent() {
    job.onRun()

    verify(mockClient).retrieveSeasons(expectedId)
    verify(mockEventBus).post(any<SeasonsRetrievalEvent>())
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

}
