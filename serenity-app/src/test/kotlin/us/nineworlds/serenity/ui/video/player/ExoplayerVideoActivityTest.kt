package us.nineworlds.serenity.ui.video.player

import android.net.Uri
import android.view.KeyEvent
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.android.api.Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
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

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Mock
  lateinit var mockExoPlayerPresenter: ExoplayerPresenter

  @Mock
  lateinit var mockDefaultBandwidthMeter: DefaultBandwidthMeter

  @Mock
  lateinit var mockDataSourceFactory: DataSource.Factory

  @Mock
  lateinit var mockMappingTrackSelector: TrackSelector

  @Mock
  lateinit var mockLogger: Logger

  @Mock
  lateinit var mockPlayer: SimpleExoPlayer

  @Mock
  lateinit var mockTimeUtil: TimeUtil

  @Inject
  lateinit var mockAndroidHelper: AndroidHelper

  lateinit var activity: ExoplayerVideoActivity

  override fun openScope(): Scope {
    val scope = Toothpick.openScopes(InjectionConstants.APPLICATION_SCOPE, AppInjectionConstants.EXOPLAYER_SCOPE)
    return scope
  }

  @Before
  override fun setUp() {
    super.setUp()
    activity = Robolectric.buildActivity(ExoplayerVideoActivity::class.java).create().get()
    activity.player = mockPlayer
  }

  @Test
  fun bindsSimpleExoPlayerView() {
    Assertions.assertThat(activity.playerView).isNotNull
  }

  @Test
  fun onStartDoesNotCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
    activity.onStart()

    verify(mockExoPlayerPresenter, never()).playBackFromVideoQueue(anyOrNull())
  }

  @Test
  fun onPauseCallsReleasePlayser() {
    val spy = spy(activity)
    doNothing().whenever(spy).releasePlayer()
    spy.onPause()

    verify(spy).releasePlayer()
  }

  @Test
  fun onPauseDoesNotCallsReleasePlayser() {
    doReturn(24).whenever(mockAndroidHelper).buildNumber()
    val spy = spy(activity)
    doNothing().whenever(spy).releasePlayer()
    spy.onPause()

    verify(spy, never()).releasePlayer()
  }

  @Test
  fun onStopDCallsReleasePlayser() {
    doReturn(24).whenever(mockAndroidHelper).buildNumber()
    val spy = spy(activity)
    doNothing().whenever(spy).releasePlayer()
    spy.onStop()

    verify(spy).releasePlayer()
  }

  @Test
  fun onStopDoesNotCallsReleasePlayser() {
    val spy = spy(activity)
    doNothing().whenever(spy).releasePlayer()
    spy.onStop()

    verify(spy, never()).releasePlayer()
  }

  @Test
  fun buildMediaSourceReturnsNonNullSource() {
    assertThat(activity.buildMediaSource(Uri.parse("http://www.example.com/start.mkv"))).isNotNull()
  }

  @Test
  @Ignore
  fun initializePlayerSetABunchOfRequiredItems() {
    val spy = spy(activity)
    doReturn(mockPlayer).whenever(spy).createSimpleExoplayer()
    doReturn(TrackSelectionArray()).whenever(mockPlayer).getCurrentTrackSelections()

    spy.initializePlayer("http://www.example.com/start.mkv", 0)

    assertThat(spy.player).isInstanceOf(SimpleExoPlayer::class.java)

    verify(spy).createSimpleExoplayer()
    verify(mockPlayer).addListener(any(EventLogger::class.java))
    verify(mockPlayer).prepare(any(MediaSource::class.java))
  }

  @Test
  fun releasePlayerReleasesWhenPlayerIsNotNull() {
    activity.releasePlayer()

    verify(mockPlayer).release()
  }

  @Test
  fun createSimpleExoplayer() {
    assertThat(activity.createSimpleExoplayer()).isNotNull()
  }

  @Test
  fun onBackPressedStopsAndReleasesVideoPlayer() {
    doReturn(Player.STATE_READY).whenever(mockPlayer).playbackState

    activity.onBackPressed()

    verify(mockPlayer).playWhenReady = false
    verify(mockExoPlayerPresenter).stopPlaying(anyOrNull())
    verify(mockPlayer).clearVideoSurface()
    verify(mockPlayer).release()
  }

  @Test
  fun onBackPressedStopsAndReleasesVideoPlayerWhenBuffering() {
    doReturn(Player.STATE_BUFFERING).whenever(mockPlayer).playbackState

    activity.onBackPressed()

    verify(mockPlayer).playWhenReady = false
    verify(mockExoPlayerPresenter).stopPlaying(anyOrNull())
    verify(mockPlayer).clearVideoSurface()
    verify(mockPlayer).release()
  }

  @Test
  fun onKeyCodeDownHandlesHomeEvent() {
    doReturn(Player.STATE_BUFFERING).whenever(mockPlayer).playbackState

    val result = activity.onKeyDown(KeyEvent.KEYCODE_HOME, null)

    assertThat(result).isTrue()
    verify(mockPlayer).playWhenReady = false
    verify(mockExoPlayerPresenter).stopPlaying(anyOrNull())
    verify(mockPlayer).clearVideoSurface()
    verify(mockPlayer).release()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(ExoplayerPresenter::class.java).toInstance(mockExoPlayerPresenter)
      bind(DataSource.Factory::class.java).toInstance(mockDataSourceFactory)
      bind(TrackSelector::class.java).toInstance(mockMappingTrackSelector)
      bind(Logger::class.java).toInstance(mockLogger)
      bind(TimeUtil::class.java).toInstance(mockTimeUtil)
    }
  }
}