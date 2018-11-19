package us.nineworlds.serenity.ui.video.player

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.android.api.Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.test.ShadowSubtitleView

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [17], shadows = [ShadowSubtitleView::class])
open class ExoplayerVideoActivityTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Mock
  lateinit var mockExoPlayerPresenter: ExoplayerPresenter

  @Mock
  lateinit var mockPlayer: SimpleExoPlayer

  lateinit var activity: ExoplayerVideoActivity

  @TargetApi(VERSION_CODES.KITKAT)
  @Before
  override fun setUp() {
    initMocks(this)
    super.setUp()
    activity = Robolectric.buildActivity(ExoplayerVideoActivity::class.java).create().get()
    activity.player = mockPlayer
    activity.mediaDataSourceFactory = mock<DataSource.Factory>()
    activity.trackSelector = mock<MappingTrackSelector>()

  }

  @Test
  fun bindsSimpleExoPlayerView() {
    Assertions.assertThat(activity.playerView).isNotNull
  }

  @Test
  fun onStartDoesNotCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
    activity.onStart()

    verify(mockExoPlayerPresenter, never()).playBackFromVideoQueue()
  }

  @Test
  fun onPauseCallsReleasePlayser() {
    val spy = spy(activity)
    doNothing().`when`(spy).releasePlayer()
    spy.onPause()

    verify(spy).releasePlayer()
  }

  @Test
  @Config(sdk = intArrayOf(24))
  fun onPauseDoesNotCallsReleasePlayser() {
    val spy = spy(activity)
    doNothing().`when`(spy).releasePlayer()
    spy.onPause()

    verify(spy, never()).releasePlayer()
  }

  @Test
  @Config(sdk = intArrayOf(24))
  fun onStopDCallsReleasePlayser() {
    val spy = spy(activity)
    doNothing().`when`(spy).releasePlayer()
    spy.onStop()

    verify(spy).releasePlayer()
  }

  @Test
  fun onStopDoesNotCallsReleasePlayser() {
    val spy = spy(activity)
    doNothing().`when`(spy).releasePlayer()
    spy.onStop()

    verify(spy, never()).releasePlayer()
  }

  @Test
  fun onNewIntentCallsRelease() {
    val spy = spy(activity)
    doNothing().`when`(spy).releasePlayer()
    spy.onNewIntent(Intent())

    verify(spy).releasePlayer()
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
    verify(mockPlayer).setAudioDebugListener(any(EventLogger::class.java))
    verify(mockPlayer).setVideoDebugListener(any(EventLogger::class.java))
    verify(mockPlayer).setMetadataOutput(any(EventLogger::class.java))
    verify(mockPlayer).prepare(any(MediaSource::class.java))
  }

  @Test
  fun releasePlayerReleasesWhenPlayerIsNotNull() {
    activity.player = mockPlayer

    activity.releasePlayer()

    verify(mockPlayer).release()
  }

  @Test
  fun createSimpleExoplayer() {
    assertThat(activity.createSimpleExoplayer()).isNotNull()
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(ExoplayerContract.ExoplayerPresenter::class.java).toInstance(mockExoPlayerPresenter)
    }
  }
}