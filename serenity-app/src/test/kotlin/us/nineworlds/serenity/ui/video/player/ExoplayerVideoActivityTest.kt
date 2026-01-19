package us.nineworlds.serenity.ui.video.player

import android.content.SharedPreferences
import android.net.Uri
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.media3.common.Format
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.TrackGroup
import androidx.media3.common.C
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelectionArray
import androidx.media3.exoplayer.trackselection.TrackSelector
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.google.common.collect.ImmutableList
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.spyk
import io.mockk.verify
import javax.inject.Inject
import org.junit.After
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
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.core.util.TimeUtil
import us.nineworlds.serenity.injection.AppInjectionConstants
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.test.ShadowSubtitleView

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowSubtitleView::class])
open class ExoplayerVideoActivityTest : InjectingTest() {

    companion object {
        private val mockExoPlayerPresenter = mockk<ExoplayerPresenter>(relaxed = true)
        private val mockDataSourceFactory = mockk<DataSource.Factory>(relaxed = true)
        private val mockTrackSelector = mockk<TrackSelector>(relaxed = true)
        private val mockLogger = mockk<Logger>(relaxed = true)
        private val mockPlayer = mockk<ExoPlayer>(relaxed = true)
        private val mockTimeUtil = mockk<TimeUtil>(relaxed = true)

        private val mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
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
        mockkConstructor(VideoKeyCodeHandlerDelegate::class)
        mockkConstructor(DefaultLoadControl.Builder::class)
        mockkConstructor(DefaultRenderersFactory::class)
        mockkConstructor(ExoPlayer.Builder::class)
        
        val mockBuilder = mockk<ExoPlayer.Builder>(relaxed = true)
        every { anyConstructed<ExoPlayer.Builder>().setRenderersFactory(any()) } returns mockBuilder
        every { mockBuilder.setTrackSelector(any()) } returns mockBuilder
        every { mockBuilder.setLoadControl(any()) } returns mockBuilder
        every { mockBuilder.setAudioAttributes(any(), any()) } returns mockBuilder
        every { mockBuilder.setUsePlatformDiagnostics(any()) } returns mockBuilder
        every { mockBuilder.build() } returns mockPlayer

        every { mockPlayer.applicationLooper } returns Looper.getMainLooper()
        activity = Robolectric.buildActivity(ExoplayerVideoActivity::class.java).create().get()
        activity.player = mockPlayer
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkConstructor(VideoKeyCodeHandlerDelegate::class)
        unmockkConstructor(DefaultLoadControl.Builder::class)
        unmockkConstructor(DefaultRenderersFactory::class)
        unmockkConstructor(ExoPlayer.Builder::class)
        Toothpick.reset()
    }

    @Test
    fun bindsExoPlayerView() {
        assertThat(activity.playerView).isNotNull()
    }

    @Test
    fun onStartDoesNotCallsPresenterPlayBackFromVideoQueueWhenOnAPI19OrHigher() {
        activity.onStart()

        verify(exactly = 0) { mockExoPlayerPresenter.playBackFromVideoQueue(any()) }
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

        verify(exactly = 0) { spy.releasePlayer() }
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

        assertThat(spy.player).isInstanceOf(ExoPlayer::class.java)

        verify { spy.createSimpleExoplayer() }
        verify { mockPlayer.addListener(any()) }
        verify { mockPlayer.prepare(any()) }
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
    fun createSimpleExoplayerWithDefaultTrackSelector() {
        val mockDefaultTrackSelector = mockk<DefaultTrackSelector>(relaxed = true)
        activity.trackSelector = mockDefaultTrackSelector

        activity.createSimpleExoplayer()

        verify { mockDefaultTrackSelector.parameters = any() }
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

    @Test
    fun toggleDebugViewShowsLayoutWhenHidden() {
        val debugLayout = activity.findViewById<LinearLayout>(R.id.exo_debug_layout)
        debugLayout.visibility = View.GONE

        activity.toggleDebugView()

        assertThat(debugLayout.visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun toggleDebugViewHidesLayoutWhenVisible() {
        val debugLayout = activity.findViewById<LinearLayout>(R.id.exo_debug_layout)
        debugLayout.visibility = View.VISIBLE

        activity.toggleDebugView()

        assertThat(debugLayout.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun updateSerenityDebugInfoSetsCorrectTextForTranscoding() {
        val serenityDebugTextView = activity.findViewById<TextView>(R.id.serenity_debug_text_view)

        activity.updateSerenityDebugInfo(true, "h264", "ac3", 5000000)

        val expectedText = "Transcoding: MKV/AAC\nOriginal: h264/ac3\nBitrate: 5000 kbps"
        assertThat(serenityDebugTextView.text.toString()).isEqualTo(expectedText)
    }

    @Test
    fun updateSerenityDebugInfoSetsCorrectTextForDirectPlay() {
        val serenityDebugTextView = activity.findViewById<TextView>(R.id.serenity_debug_text_view)

        activity.updateSerenityDebugInfo(false, "h264", "ac3", 0)

        val expectedText = "Direct Play\nOriginal: h264/ac3\n"
        assertThat(serenityDebugTextView.text.toString()).isEqualTo(expectedText)
    }

    @Test
    fun playerListenerOnTracksChangedUpdatesDebugInfo() {
        val format = Format.Builder().setAverageBitrate(1000000).build()
        val trackGroup = TrackGroup(format)
        val trackGroupGroup = Tracks.Group(trackGroup, false, intArrayOf(C.FORMAT_HANDLED), booleanArrayOf(true))
        val tracks = Tracks(ImmutableList.of(trackGroupGroup))

        val listener = activity.PlayerListener()
        listener.onTracksChanged(tracks)

        verify { mockExoPlayerPresenter.video }
        val serenityDebugTextView = activity.findViewById<TextView>(R.id.serenity_debug_text_view)
        assertThat(serenityDebugTextView.text.toString()).isNotNull()
    }

    @Test
    fun initializePlayerSetsDebugToggleClickListener() {
        val spy = spyk(activity)
        every { spy.createSimpleExoplayer() } returns mockPlayer
        every { mockPlayer.currentTrackSelections } returns TrackSelectionArray()
        
        spy.initializePlayer("http://example.com", 0)
        
        val debugToggle = spy.playerView.findViewById<ImageButton>(R.id.exo_debug_toggle)
        assertThat(debugToggle).isNotNull()
        
        debugToggle.performClick()
        verify { mockExoPlayerPresenter.toggleDebugMode() }
    }

    @Test
    fun createSimpleExoplayerSetsRobustBufferDurationsForTCLWithTunneling() {
        every { mockAndroidHelper.enableTunneling() } returns true
        
        activity.createSimpleExoplayer()

        verify {
            anyConstructed<DefaultLoadControl.Builder>().setBufferDurationsMs(30000, 50000, 2500, 5000)
        }
    }

    @Test
    fun createSimpleExoplayerDisablesAudioPlaybackParametersForTCLWithTunneling() {
        every { mockAndroidHelper.enableTunneling() } returns true
        
        activity.createSimpleExoplayer()

        verify(exactly = 0) {
            anyConstructed<DefaultRenderersFactory>().setEnableAudioOutputPlaybackParameters(any())
        }
    }

    @Test
    fun createSimpleExoplayerDoesNotSetFixedBufferBytesForTCLWithTunneling() {
        every { mockAndroidHelper.enableTunneling() } returns true
        
        activity.createSimpleExoplayer()

        verify(exactly = 0) {
            anyConstructed<DefaultLoadControl.Builder>().setTargetBufferBytes(any())
        }
    }

    override fun installTestModules() {
        Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE).installTestModules(object : Module() {
            init {
                bind(SharedPreferences::class.java).toInstance(mockSharedPreferences)
            }
        })
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
