package us.nineworlds.serenity.ui.video.player

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import dagger.Module
import dagger.Provides
import org.assertj.android.api.Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
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
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness.LENIENT
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application
import org.robolectric.annotation.Config
import us.nineworlds.serenity.BuildConfig
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.SerenityModule
import us.nineworlds.serenity.injection.modules.VideoModule
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.test.ShadowSubtitleView

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(17), shadows = arrayOf(ShadowSubtitleView::class))
open class ExoplayerVideoActivityTest : InjectingTest() {

  @Rule
  @JvmField
  var mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(LENIENT)

  @Mock lateinit var mockExoPlayerPresenter: ExoplayerPresenter
  @Mock lateinit var mockPlayer: SimpleExoPlayer

  lateinit var activity: ExoplayerVideoActivity

  @TargetApi(VERSION_CODES.KITKAT)
  @Before
  override fun setUp() {
    initMocks(this)
    super.setUp()
    activity = Robolectric.buildActivity(ExoplayerVideoActivity::class.java).create().get();
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
  @Config(sdk = intArrayOf(24))
  fun onStartCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
    activity.onStart()

    verify(mockExoPlayerPresenter).playBackFromVideoQueue()
  }

  @Test
  fun onResumeCallsPlayBackFromVideoQueue() {
    activity.onResume()

    verify(mockExoPlayerPresenter).playBackFromVideoQueue()
  }

  @Test
  @Config(sdk = intArrayOf(24))
  fun onResumeDoesNotCallsPlayBackFromVideoQueue() {
    activity.player = mockPlayer
    activity.onResume()

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
  fun initializePlayerSetABunchOfRequiredItems() {
    val spy = spy(activity)
    doReturn(mockPlayer).`when`(spy).createSimpleExoplayer()
    doReturn(TrackSelectionArray()).`when`(mockPlayer).currentTrackSelections

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

  override fun getModules(): MutableList<Any> =
      mutableListOf(AndroidModule(application),
          SerenityModule(), VideoModule(),
          TestModule(), TestingModule())

  @Module(library = true,
      includes = arrayOf(AndroidModule::class),
      overrides = true,
      injects = arrayOf(ExoplayerVideoActivityTest::class, ExoplayerVideoActivity::class))
  inner class TestModule {

    @Provides
    fun providesExoPlayerPresenter(): ExoplayerPresenter = mockExoPlayerPresenter
  }
}