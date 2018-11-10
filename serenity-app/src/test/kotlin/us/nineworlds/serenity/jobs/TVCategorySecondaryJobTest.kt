package us.nineworlds.serenity.jobs

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
import us.nineworlds.serenity.events.MovieSecondaryCategoryEvent
import us.nineworlds.serenity.events.TVCategorySecondaryEvent
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.SerenityModule
import us.nineworlds.serenity.test.InjectingTest
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class TVCategorySecondaryJobTest : InjectingTest() {

  @Rule @JvmField public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

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

  override fun getModules(): MutableList<Any> = mutableListOf(AndroidModule(RuntimeEnvironment.application),
      TestModule())

  @Module(injects = arrayOf(TVCategorySecondaryJobTest::class),
      includes = arrayOf(SerenityModule::class, TestingModule::class),
      library = true,
      overrides = true)
  inner class TestModule {
    @Provides
    fun providesEventBus(): EventBus = mockEventBus
  }
}
