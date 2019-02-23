package us.nineworlds.serenity.ui.browser.tv.seasons

import android.content.res.Resources
import com.birbit.android.jobqueue.JobManager
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.apache.commons.lang3.RandomStringUtils
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.events.EpisodesRetrievalEvent
import us.nineworlds.serenity.events.SeasonsRetrievalEvent
import us.nineworlds.serenity.jobs.EpisodesRetrievalJob
import us.nineworlds.serenity.jobs.SeasonsRetrievalJob
import us.nineworlds.serenity.test.InjectingTest
import javax.inject.Inject

class TVShowSeasonBrowserPresenterTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

  @Mock
  lateinit var mockEventBus: EventBus

  @Inject
  lateinit var mockJobManager: JobManager
  lateinit var presenter: TVShowSeasonBrowserPresenter

  @Mock
  lateinit var mockView: TVShowSeasonBrowserView

  @Mock
  lateinit var mockMediaContainer: IMediaContainer

  @Mock
  lateinit var mockResources: Resources

  @Before
  override fun setUp() {
    super.setUp()
    presenter = TVShowSeasonBrowserPresenter()
    presenter.eventBus = mockEventBus
  }

  @Test
  fun eventBusIsRegisteredWhenViewIsAttached() {
    presenter.attachView(mockView)

    verify(mockEventBus).register(presenter)
  }

  @Test
  fun eventBusIsUnregisteredWhenViewIsDettached() {
    presenter.detachView(mockView)

    verify(mockEventBus).unregister(presenter)
  }

  @Test
  fun retrieveSeasonAddsJobToQueue() {
    val expectedKey = RandomStringUtils.randomAlphabetic(10)

    presenter.retrieveSeasons(expectedKey)

    verify(mockJobManager).addJobInBackground(any<SeasonsRetrievalJob>())
  }

  @Test
  fun retrieveEpisodesAddsJobToQueue() {
    val expectedKey = RandomStringUtils.randomAlphabetic(10)

    presenter.retrieveEpisodes(expectedKey)

    verify(mockJobManager).addJobInBackground(any<EpisodesRetrievalJob>())
  }

  @Test
  fun whenEpisodeResponseEventIsReceievedUpdateTheEpisodesInTheView() {
    val episodeEvent = EpisodesRetrievalEvent(mockMediaContainer)
    presenter.attachView(mockView)

    presenter.onEpisodeResponse(episodeEvent)

    verify(mockView).updateEpisodes(any<List<VideoContentInfo>>())
  }

  @Test
  fun whenSeasonRetrievalResponseEventIsReceivedPopulateTheSeasonList() {
    val seasonRetrievalEvent = SeasonsRetrievalEvent(mockMediaContainer)
    presenter.attachView(mockView)

    presenter.onSeasonsRetrievalResponse(seasonRetrievalEvent)

    verify(mockView).populateSeasons(any<List<SeriesContentInfo>>())
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(Resources::class.java).toInstance(mockResources)
    }
  }
}