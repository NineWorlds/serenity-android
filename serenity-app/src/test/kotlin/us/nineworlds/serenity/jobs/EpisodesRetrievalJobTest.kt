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
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.events.EpisodesRetrievalEvent
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class EpisodesRetrievalJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  lateinit var job: EpisodesRetrievalJob

  private val expectedId: String = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = EpisodesRetrievalJob(expectedId)
    job.eventBus = mockEventBus
  }

  @Test
  fun onRunFetchesEpisodesForTheSpecifiedId() {
    job.onRun()

    verify(mockClient).retrieveEpisodes(expectedId)
  }

  @Test
  fun onRunFetchesEpisodesAndPostsEpisodesEvent() {
    job.onRun()

    verify(mockClient).retrieveEpisodes(expectedId)
    verify(mockEventBus).post(any<EpisodesRetrievalEvent>())
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

}
