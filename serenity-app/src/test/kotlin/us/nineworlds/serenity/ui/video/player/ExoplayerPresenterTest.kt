package us.nineworlds.serenity.ui.video.player

import android.view.View
import com.birbit.android.jobqueue.JobManager
import dagger.Module
import dagger.Provides
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import us.nineworlds.plex.rest.PlexappFactory
import us.nineworlds.serenity.BuildConfig
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.events.video.OnScreenDisplayEvent
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.jobs.video.UpdatePlaybackPostionJob
import us.nineworlds.serenity.jobs.video.WatchedStatusJob
import us.nineworlds.serenity.test.InjectingTest
import java.util.LinkedList
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ExoplayerPresenterTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Inject lateinit var mockPlexFactory: PlexappFactory
  @Inject lateinit var mockJobManager: JobManager
  @Inject lateinit var mockLogger: Logger

  @Mock lateinit var mockVideoContentInfo: VideoContentInfo
  @Mock lateinit var mockVideoQueue: LinkedList<VideoContentInfo>
  @Mock lateinit var mockView: ExoplayerContract.ExoplayerView
  @Mock lateinit var mockEventBus: EventBus
  @Mock lateinit var mockOnScreenDisplayEvent: OnScreenDisplayEvent

  private lateinit var presenter: ExoplayerPresenter

  @Before
  override fun setUp() {
    super.setUp()
    presenter = spy(ExoplayerPresenter())
    doReturn(mockView).`when`(presenter).viewState
  }

  @Test
  fun onPlayerStateChangedPlaysVideoWhenTrue() {
    presenter.onPlayerStateChanged(true, 0)

    verify(mockView).play()
    verify(mockView, never()).pause()
  }

  @Test
  fun onPlayerStateChangedPausesVideoWhenFalse() {
    presenter.onPlayerStateChanged(false, 0)

    verify(mockView).pause()
    verify(mockView, never()).play()
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
    doReturn(false).`when`(mockOnScreenDisplayEvent).isShowing

    presenter.onOnScreenDisplayEvent(mockOnScreenDisplayEvent)

    verify(mockView, never()).hideController()
    verify(mockView).showController()
    verify(mockOnScreenDisplayEvent).isShowing
  }

  @Test
  fun onScreenDisplayEventHidesViewControlWhenShowing() {
    doReturn(true).`when`(mockOnScreenDisplayEvent).isShowing

    presenter.onOnScreenDisplayEvent(mockOnScreenDisplayEvent)

    verify(mockView).hideController()
    verify(mockView, never()).showController()
    verify(mockOnScreenDisplayEvent).isShowing
  }

  @Test
  fun detachViewUnregistersEventBus() {
    presenter.detachView(mockView)

    verify(mockEventBus).unregister(presenter)
  }

  @Test
  fun attachViewRegistersEventBus() {

    presenter.attachView(mockView)

    verify(mockEventBus).register(presenter)
  }

  @Test
  fun isHudShowingReturnsTrue() {
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
    doReturn(true).`when`(mockVideoQueue).isEmpty()

    presenter.playBackFromVideoQueue()

    verify(mockVideoQueue, never()).poll()
    verify(mockVideoQueue).isEmpty()
    verify(mockView, never()).initializePlayer(anyString(), eq(0))
  }

  @Test
  fun playBackFromVideoQueuePopulatesVideoWhenPolled() {
    val expectedId = RandomStringUtils.randomNumeric(1)
    val expectedUrl = "http://www.example.com/start.mkv"

    doReturn(mockVideoContentInfo).`when`(mockVideoQueue).poll()
    doReturn(false).`when`(mockVideoQueue).isEmpty()
    doReturn("avi").`when`(mockVideoContentInfo).container
    doReturn(expectedId).`when`(mockVideoContentInfo).id()
    doReturn(expectedUrl).`when`(mockPlexFactory).getTranscodeUrl(anyString(), anyInt())

    presenter.playBackFromVideoQueue()

    assertThat(presenter.video).isNotNull().isEqualTo(mockVideoContentInfo)
    verify(mockVideoQueue).poll()
    verify(mockVideoContentInfo).container
    verify(mockVideoQueue).isEmpty()
    verify(mockVideoContentInfo).id()
    verify(mockPlexFactory).getTranscodeUrl(expectedId, 0)
    verify(mockView).initializePlayer(expectedUrl, 0)
  }

  override fun getModules(): MutableList<Any> =
      mutableListOf(AndroidModule(RuntimeEnvironment.application),
          TestModule(), TestingModule())

  @Module(library = true,
      includes = arrayOf(AndroidModule::class),
      overrides = true,
      injects = arrayOf(ExoplayerPresenterTest::class, ExoplayerPresenter::class))
  inner class TestModule {

    @Provides
    @Singleton
    @ForVideoQueue
    fun providesVideoQueue(): LinkedList<VideoContentInfo> = mockVideoQueue

    @Provides
    @Singleton
    fun providesEventBus(): EventBus = mockEventBus

  }
}