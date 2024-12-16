package us.nineworlds.serenity.ui.video.player

import android.net.Uri
import android.view.KeyEvent
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.injection.AppInjectionConstants
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.test.ShadowSubtitleView
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowSubtitleView::class])
open class ExoplayerVideoActivityTest : InjectingTest() {

  companion object {
    private val mockExoPlayerPresenter = mockk<ExoplayerPresenter>(relaxed = true)
    private val mockDataSourceFactory = mockk<DataSource.Factory>(relaxed = true)
    private val mockTrackSelector = mockk<TrackSelector>(relaxed = true)
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockPlayer = mockk<SimpleExoPlayer>(relaxed = true)
    private val mockTimeUtil = mockk<TimeUtil>(relaxed = true)
  }

  @Inject
  lateinit var mockAndroidHelper: AndroidHelper

  lateinit var activity: ExoplayerVideoActivity

  override fun openScope(): Scope {
    val scope = Toothpick.openScopes(InjectionConstants.APPLICATION_SCOPE, AppInjectionConstants.EXOPLAYER_SCOPE)
    return scope
  }

  @Before
  override fun setUp() {
    clearAllMocks()
    super.setUp()
    activity = Robolectric.buildActivity(ExoplayerVideoActivity::class.java).create().get()
    activity.player = mockPlayer
  }

  @Test
  fun bindsSimpleExoPlayerView() {
    assertThat(activity.playerView).isNotNull()
  }

  @Test
  fun onStartDoesNotCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
    activity.onStart()

    verify(exactly = 0) { mockExoPlayerPresenter.playBackFromVideoQueue(any())  }
  }

  @Test
  fun onPauseCallsReleasePlayser() {
    activity.onPause()

    verify { mockPlayer.stop() }
  }

  @Test
  fun onPauseDoesNotCallsReleasePlayser() {
    every { mockAndroidHelper.buildNumber() } returns 24

    activity.onPause()

    verify(exactly = 0) { mockPlayer.stop() }
  }

  @Test
  fun onStopDCallsReleasePlayser() {
    every { mockAndroidHelper.buildNumber() } returns 24
    val spy = spyk(activity)
    every { spy.releasePlayer() } just Runs
    spy.onStop()

    verify { spy.releasePlayer() }
  }

  @Test
  fun onStopDoesNotCallsReleasePlayser() {
    val spy = spyk(activity)
    every { spy.releasePlayer() } just Runs

    spy.onStop()

    verify(exactly = 0 ) { spy.releasePlayer() }
  }

  @Test
  fun buildMediaSourceReturnsNonNullSource() {
    assertThat(activity.buildMediaSource(Uri.parse("http://www.example.com/start.mkv"))).isNotNull()
  }

  @Test
  @Ignore
  fun initializePlayerSetABunchOfRequiredItems() {
    val spy = spyk(activity)
    every { spy.createSimpleExoplayer() } returns mockPlayer
    every { mockPlayer.currentTrackSelections } returns TrackSelectionArray()

    spy.initializePlayer("http://www.example.com/start.mkv", 0)

    assertThat(spy.player).isInstanceOf(SimpleExoPlayer::class.java)

    verify { spy.createSimpleExoplayer() }
    verify { mockPlayer.addListener(any()) }
    verify { mockPlayer.prepare(any())}
  }

  @Test
  fun releasePlayerReleasesWhenPlayerIsNotNull() {
    activity.releasePlayer()

    verify { mockPlayer.release() }
  }

  @Test
  fun createSimpleExoplayer() {
    assertThat(activity.createSimpleExoplayer()).isNotNull()
  }

  @Test
  fun onBackPressedStopsAndReleasesVideoPlayer() {
    every { mockPlayer.playbackState } returns Player.STATE_READY

    activity.onBackPressed()

    verify { mockPlayer.playWhenReady = false }
    verify { mockExoPlayerPresenter.stopPlaying(any()) }
    verify { mockPlayer.clearVideoSurface() }
    verify { mockPlayer.release() }
  }

  @Test
  fun onBackPressedStopsAndReleasesVideoPlayerWhenBuffering() {
    every { mockPlayer.playbackState } returns Player.STATE_BUFFERING

    activity.onBackPressed()

    verify { mockPlayer.playWhenReady = false }
    verify { mockExoPlayerPresenter.stopPlaying(any()) }
    verify { mockPlayer.clearVideoSurface() }
    verify { mockPlayer.release() }
  }

  @Test
  fun onKeyCodeDownHandlesHomeEvent() {
    every { mockPlayer.playbackState } returns Player.STATE_BUFFERING

    val result = activity.onKeyDown(KeyEvent.KEYCODE_HOME, null)

    assertThat(result).isTrue()

    verify { mockPlayer.playWhenReady = false }
    verify { mockExoPlayerPresenter.stopPlaying(any()) }
    verify { mockPlayer.clearVideoSurface() }
    verify { mockPlayer.release() }
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(ExoplayerPresenter::class.java).toInstance(mockExoPlayerPresenter)
      bind(DataSource.Factory::class.java).toInstance(mockDataSourceFactory)
      bind(TrackSelector::class.java).toInstance(mockTrackSelector)
      bind(Logger::class.java).toInstance(mockLogger)
      bind(TimeUtil::class.java).toInstance(mockTimeUtil)
    }
  }
}