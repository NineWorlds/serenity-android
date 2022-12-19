package us.nineworlds.serenity.ui.video.player

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.birbit.android.jobqueue.JobManager
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.events.video.OnScreenDisplayEvent
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.jobs.video.UpdatePlaybackPostionJob
import us.nineworlds.serenity.jobs.video.WatchedStatusJob
import us.nineworlds.serenity.test.InjectingTest
import java.util.LinkedList
import java.util.Random
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class ExoplayerPresenterTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Inject
  lateinit var mockPlexFactory: SerenityClient
  @Inject
  lateinit var mockJobManager: JobManager
  @Inject
  lateinit var mockLogger: Logger

  private var mockVideoContentInfo = mock<VideoContentInfo>()
  private var mockVideoQueue = LinkedList<VideoContentInfo>()
  @Mock
  lateinit var mockView: ExoplayerContract.ExoplayerView
  @Mock
  lateinit var mockEventBus: EventBus
  @Mock
  lateinit var mockOnScreenDisplayEvent: OnScreenDisplayEvent

  @Mock
  lateinit var mockAndroidHelper: AndroidHelper

  private lateinit var presenter: ExoplayerPresenter

  @Before
  override fun setUp() {
    super.setUp()
    presenter = spy(ExoplayerPresenter())
    presenter.attachView(mockView)

    doReturn(mockView).whenever(presenter).viewState
  }

  @Test
  fun updateWatchedStatusAddsJobToJobmanager() {
    val expectedId = RandomStringUtils.randomNumeric(5)
    var videoContentInfo = MoviePosterInfo()
    videoContentInfo.setId(expectedId)
    presenter.video = videoContentInfo

    presenter.updateWatchedStatus()

    verify(mockJobManager).addJobInBackground(any(WatchedStatusJob::class.java))
  }

  @Test
  fun onScreenDisplayEventShowsControllerWhenHidden() {
    doReturn(false).whenever(mockOnScreenDisplayEvent).isShowing

    presenter.onOnScreenDisplayEvent(mockOnScreenDisplayEvent)

    verify(mockView, never()).hideController()
    verify(mockView).showController()
    verify(mockOnScreenDisplayEvent).isShowing
  }

  @Test
  fun onScreenDisplayEventHidesViewControlWhenShowing() {
    doReturn(true).whenever(mockOnScreenDisplayEvent).isShowing

    presenter.onOnScreenDisplayEvent(mockOnScreenDisplayEvent)

    verify(mockView).hideController()
    verify(mockView, never()).showController()
    verify(mockOnScreenDisplayEvent).isShowing
  }

  @Test
  fun detachViewUnregistersEventBus() {
    presenter.eventBus = mockEventBus
    presenter.detachView(mockView)

    verify(mockEventBus).unregister(presenter)
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
  fun updateServerPlaybackPositionSetsVideoOffestToExpectedPosition() {
    var videoContentInfo = MoviePosterInfo()
    val expectedPosition = Random().nextInt()

    presenter.video = videoContentInfo
    presenter.updateServerPlaybackPosition(expectedPosition.toLong())

    assertThat(videoContentInfo.resumeOffset).isEqualTo(expectedPosition)

    verify(mockJobManager).addJobInBackground(any(UpdatePlaybackPostionJob::class.java))
  }

  @Test
  fun playBackFromVideoQueueDoesNothingWhenEmpty() {
    presenter.playBackFromVideoQueue(true)

    assertThat(mockVideoQueue).isEmpty()
    verify(mockView, never()).initializePlayer(anyString(), eq(0))
  }

  @Test
  @Ignore
  fun playBackFromVideoQueuePopulatesVideoWhenPolled() {
    val expectedId = RandomStringUtils.randomNumeric(1)
    val expectedUrl = "http://www.example.com/start.mkv"

    doReturn("avi").whenever(mockVideoContentInfo).container
    doReturn(expectedId).whenever(mockVideoContentInfo).id()
    doReturn(expectedUrl).whenever(mockPlexFactory).createTranscodeUrl(anyString(), anyInt())

    presenter.playBackFromVideoQueue(true)

    assertThat(presenter.video as VideoContentInfo).isNotNull.isEqualTo(mockVideoContentInfo)
    //Truth.assertThat(presenter.video).isNotNull().isEqualTo(mockVideoContentInfo)
    verify(mockVideoContentInfo, atLeastOnce()).container
    verify(mockVideoContentInfo).id()
    verify(mockPlexFactory).createTranscodeUrl(expectedId, 0)
    verify(mockView).initializePlayer(expectedUrl, 0)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(LinkedList::class.java).withName(ForVideoQueue::class.java).toInstance(mockVideoQueue)
      bind(EventBus::class.java).toInstance(mockEventBus)
      bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
    }
  }
}