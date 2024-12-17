@file:OptIn(ExperimentalCoroutinesApi::class)

package us.nineworlds.serenity.ui.video.player

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.birbit.android.jobqueue.JobManager
import io.mockk.clearAllMocks
import io.mockk.coVerify

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.apache.commons.lang3.RandomStringUtils
import org.greenrobot.eventbus.EventBus
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.events.video.OnScreenDisplayEvent
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.test.InjectingTest
import java.util.LinkedList
import java.util.Random
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class ExoplayerPresenterTest : InjectingTest() {

  private companion object {
    private val mockVideoContentInfo = mockk<VideoContentInfo>(relaxed = true)
    private val mockVideoQueue = LinkedList<VideoContentInfo>()
    private val mockView: ExoplayerContract.ExoplayerView = mockk(relaxed = true)
    private val mockEventBus: EventBus = mockk(relaxed = true)
    private val mockOnScreenDisplayEvent: OnScreenDisplayEvent = mockk(relaxed = true)
    private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)
    private val mockPlaybackRepository: PlaybackRepository = mockk(relaxed = true)
  }

  @Inject
  lateinit var mockSerenityClient: SerenityClient

  @Inject
  lateinit var mockLogger: Logger

  private lateinit var presenter: ExoplayerPresenter

  @Before
  override fun setUp() {
    Dispatchers.setMain(Dispatchers.Unconfined)
    clearAllMocks()
    super.setUp()
    presenter = spyk(ExoplayerPresenter())
    every { presenter.viewState } returns mockView

    presenter.attachView(mockView)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun updateWatchedStatusUsingPlaybackRepository() = runTest(UnconfinedTestDispatcher()) {
    val expectedId = RandomStringUtils.randomNumeric(5)
    val videoContentInfo = MoviePosterInfo()
    videoContentInfo.setId(expectedId)
    presenter.video = videoContentInfo

    presenter.updateWatchedStatus()

    coVerify { mockPlaybackRepository.watched(any()) }
  }

  @Test
  fun onScreenDisplayEventShowsControllerWhenHidden() {
    every { mockOnScreenDisplayEvent.isShowing } returns false

    presenter.onOnScreenDisplayEvent(mockOnScreenDisplayEvent)

    verify(exactly = 0) { mockView.hideController() }
    verify { mockView.showController() }
    verify { mockOnScreenDisplayEvent.isShowing }
  }

  @Test
  fun onScreenDisplayEventHidesViewControlWhenShowing() {

    presenter.onOnScreenDisplayEvent(OnScreenDisplayEvent(true))

    verify { mockView.hideController() }
    verify(exactly = 0) { mockView.showController() }
  }

  @Test
  fun detachViewUnregistersEventBus() {
    presenter.eventBus = mockEventBus
    presenter.detachView(mockView)

    verify { mockEventBus.unregister(presenter) }
  }

  @Test
  fun isHudShowingReturnsTrue() {
    presenter.logger = mockLogger
    presenter.onVisibilityChange(View.GONE)

    assertThat(presenter.isHudShowing()).isFalse()
  }

  @Test
  fun isHudShowingReturnsFalse() {
    presenter.onVisibilityChange(View.VISIBLE)

    assertThat(presenter.isHudShowing()).isTrue()
  }

  @Test
  fun updateServerPlaybackPositionSetsVideoOffsetToExpectedPosition() = runTest(UnconfinedTestDispatcher()){
    val videoContentInfo = MoviePosterInfo()
    val expectedPosition = Random().nextInt()

    presenter.video = videoContentInfo
    presenter.updateServerPlaybackPosition(expectedPosition.toLong())

    assertThat(videoContentInfo.resumeOffset).isEqualTo(expectedPosition)

    coVerify { mockPlaybackRepository.updatePlaybackPosition(any()) }
  }

  @Test
  fun playBackFromVideoQueueDoesNothingWhenEmpty() {
    presenter.playBackFromVideoQueue(true)

    assertThat(mockVideoQueue).isEmpty()
    verify (exactly = 0) { mockView.initializePlayer(any<String>(), 0) }
  }

  @Test
  @Ignore
  fun playBackFromVideoQueuePopulatesVideoWhenPolled() {
    val expectedId = RandomStringUtils.randomNumeric(1)
    val expectedUrl = "http://www.example.com/start.mkv"

    every { mockVideoContentInfo.container } returns "avi"
    every { mockVideoContentInfo.id() } returns expectedId
    every { mockVideoQueue.poll() } returns mockVideoContentInfo
    every { mockSerenityClient.createTranscodeUrl(any(), any()) } returns expectedUrl

    presenter.playBackFromVideoQueue(true)

    assertThat(presenter.video).isNotNull().isEqualTo(mockVideoContentInfo)

    verify(atLeast = 1) { mockVideoContentInfo.container }
    verify { mockVideoContentInfo.id() }
    verify { mockSerenityClient.createTranscodeUrl(expectedId, 0)}
    verify { mockView.initializePlayer(expectedUrl, 0) }
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(LinkedList::class.java).withName(ForVideoQueue::class.java).toInstance(mockVideoQueue)
      bind(EventBus::class.java).toInstance(mockEventBus)
      bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
      bind(JobManager::class.java).toInstance(mockk<JobManager>(relaxed = true))
      bind(PlaybackRepository::class.java).toInstance(mockPlaybackRepository)
    }
  }
}