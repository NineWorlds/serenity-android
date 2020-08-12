package us.nineworlds.serenity.jobs

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
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.events.TVCategorySecondaryEvent
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class TVCategorySecondaryJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  lateinit var job: TVCategorySecondaryJob

  private val expectedVideoId: String = RandomStringUtils.randomAlphanumeric(5)
  private val secondaryCategory: String = RandomStringUtils.randomAlphabetic(10)

  @Before
  override fun setUp() {
    super.setUp()
    job = TVCategorySecondaryJob(expectedVideoId, secondaryCategory)
    job.eventBus = mockEventBus
  }

  @Test
  fun onRunFetchesCategoriesForTheSpecifiedId() {
    job.onRun()

    verify(mockClient).retrieveItemByIdCategory(expectedVideoId, secondaryCategory)
  }

  @Test
  fun onRunFetchesCategoriesAndPostsMainCategoryEvent() {
    job.onRun()

    verify(mockClient).retrieveItemByIdCategory(expectedVideoId, secondaryCategory)
    verify(mockEventBus).post(any<TVCategorySecondaryEvent>())
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

}
