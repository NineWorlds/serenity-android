package us.nineworlds.serenity.jobs

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.lang3.RandomStringUtils
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class OnDeckRecommendationsJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  @Mock
  lateinit var mockIMediaContainer: IMediaContainer

  @Mock
  lateinit var mockDirectory: IDirectory

  lateinit var job: OnDeckRecommendationsJob

  private val expectedVideoId: String = RandomStringUtils.randomAlphanumeric(5)
  private val expectedCategory: String = RandomStringUtils.randomAlphabetic(10)

  @Before
  override fun setUp() {
    super.setUp()
    job = OnDeckRecommendationsJob()
    job.eventBus = mockEventBus
  }

  @Test
  fun onRunRetrievesItemsByCategory() {
    doReturn(mockIMediaContainer).whenever(mockClient).retrieveItemByCategories()

    job.onRun()

    verify(mockClient).retrieveItemByCategories();
  }

  @Test
  fun onRunShowsMovieRecommendations() {
    doReturn(mockIMediaContainer).whenever(mockClient).retrieveItemByCategories()
    doReturn(mutableListOf(mockDirectory)).whenever(mockIMediaContainer).directories
    doReturn("movie").whenever(mockDirectory).type
    doReturn("title").whenever(mockDirectory).title
    doReturn("1").whenever(mockDirectory).key

    doReturn(mockIMediaContainer).whenever(mockClient).retrieveItemByIdCategory(anyString(), eq("onDeck"))

    job.onRun()

    verify(mockClient).retrieveItemByIdCategory("1", "onDeck")
  }

  @Test
  fun onRunShowsTVShowRecommendations() {
    doReturn(mockIMediaContainer).whenever(mockClient).retrieveItemByCategories()
    doReturn(mutableListOf(mockDirectory)).whenever(mockIMediaContainer).directories
    doReturn("tvshows").whenever(mockDirectory).type
    doReturn("title").whenever(mockDirectory).title
    doReturn("1").whenever(mockDirectory).key

    doReturn(mockIMediaContainer).whenever(mockClient).retrieveItemByIdCategory(anyString(), eq("onDeck"))

    job.onRun()

    verify(mockClient).retrieveItemByIdCategory("1", "onDeck")
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}
