package us.nineworlds.serenity.ui.video.player

import android.content.SharedPreferences
import android.view.KeyEvent
import androidx.media3.exoplayer.ExoPlayer
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class VideoKeyCodeHandlerDelegateTest : InjectingTest() {

    companion object {
        private val mockPreferences = mockk<SharedPreferences>(relaxed = true)
        private val mockMediaPlayer = mockk<ExoPlayer>(relaxed = true)
        private val mockPresenter = mockk<ExoplayerContract.ExoplayerPresenter>(relaxed = true)
        private val mockActivity = mockk<ExoplayerVideoActivity>(relaxed = true)
    }

    private lateinit var keyCodeHandler: VideoKeyCodeHandlerDelegate

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        clearAllMocks()

        super.setUp()

        every { mockPreferences.getString("osd_display_time", "5000") } returns "5000"

        keyCodeHandler = VideoKeyCodeHandlerDelegate(mockMediaPlayer, mockActivity, mockPresenter)
    }

    @After
    fun tearDown() {
        mockActivity.finish()
    }

    @Test
    fun handlesKeyCodeInfo() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_INFO, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeInfoWhenMenuKeyIsPressed() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MENU, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeInfoWhenIIsPressed() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_I, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeInfoWhenGameControllerButtonYIsPressed() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_Y, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePauseWhenMediaPlayerIsPlaying() {
        demandMediaPause()
        val result = keyCodeHandler.onKeyDown(
            KeyEvent.KEYCODE_MEDIA_PAUSE,
            null
        )
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeP() {
        demandMediaPause()
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_P, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeSpace() {
        demandMediaPause()
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_SPACE, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeButtonA() {
        demandMediaPause()
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_A, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeMediaPlayPause() {
        demandMediaPause()
        val result = keyCodeHandler.onKeyDown(
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, null
        )
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePauseWhenMediaPlayerIsNotPlaying() {
        demandMediaPauseWhenNotPlaying()
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_PAUSE, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodePlayPauseWhenMediaPlayerIsNotPlaying() {
        demandMediaPauseWhenNotPlaying()
        val result = keyCodeHandler.onKeyDown(
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, null
        )
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeMediaNextQueueEntryIsPlaying() {
        demandNextQueue()
        every { mockPreferences.getString("next_prev_behavior", any()) } returns "queue"

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

        assertThat(result).isTrue()
        verify { mockPreferences.getString("next_prev_behavior", "queue") }
    }

    @Test
    fun handlesKeyCodeMediaNextQueueEntryIsNotPlaying() {
        every { mockPreferences.getString("next_prev_behavior", any()) } returns "queue"

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

        assertThat(result).isTrue()
        verify { mockPreferences.getString("next_prev_behavior", "queue") }
    }

    @Test
    fun handlesKeyCodeMediaNextByPercentatage() {
        demandByPercentage()
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

        assertThat(result).isTrue()
        verify { mockPreferences.getString("next_prev_behavior", "queue") }
        verify { mockMediaPlayer.seekTo(20) }
    }

    @Test
    fun handlesKeyCodeMediaNextBySecondsUnderDuration() {
        demandBySeconds("2000", 1000L)
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

        assertThat(result).isTrue()

        verify { mockPreferences.getString("next_prev_behavior", "queue") }
        verify { mockMediaPlayer.seekTo(3000) }
    }

    @Test
    fun handlesKeyCodeMediaNextBySecondsGreaterThanDuration() {
        demandBySeconds("11000", 1000L)
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

        assertThat(result).isTrue()

        verify { mockPreferences.getString("next_prev_behavior", "queue") }
        verify { mockMediaPlayer.seekTo(9999) }
    }

    @Test
    fun handlesKeyCodeMediaNextBySecondsLessThanZero() {
        demandBySeconds("-11000", 1000L)
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

        assertThat(result).isTrue()

        verify { mockPreferences.getString("next_prev_behavior", "queue") }
        verify { mockMediaPlayer.seekTo(0) }
    }

    @Test
    fun handlesKeyCodeMediaPreviousWhenUsingPercentage() {
        every { mockPreferences.getString("next_prev_behavior", any()) } returns "10%"
        every { mockMediaPlayer.seekTo(any()) } just Runs
        every { mockMediaPlayer.currentPosition } returns 10L
        every { mockMediaPlayer.duration } returns 100L

        val result = keyCodeHandler.onKeyDown(
            KeyEvent.KEYCODE_MEDIA_PREVIOUS, null
        )
        assertThat(result).isTrue()

        verify { mockMediaPlayer.seekTo(0) }
    }

    @Test
    fun handlesKeyCodeMediaPreviousWhenUsingDuration() {
        every { mockPreferences.getString(eq("next_prev_behavior"), any()) } returns "2000"
        every { mockMediaPlayer.seekTo(any()) } just Runs
        every { mockMediaPlayer.currentPosition } returns 9000L
        every { mockMediaPlayer.duration } returns 10000L

        val result = keyCodeHandler.onKeyDown(
            KeyEvent.KEYCODE_MEDIA_PREVIOUS, null
        )
        assertThat(result).isTrue()

        verify { mockMediaPlayer.seekTo(7000) }
    }

    @Test
    fun handlesKeyCodeMediaFastForwardReceived() {
        every { mockPreferences.getString(eq("skip_forward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, null
        )
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeFReceivedSkipsForward() {
        every { mockPreferences.getString(eq("skip_forward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_F, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeButtonR1ReceivedSkipsForward() {
        every { mockPreferences.getString(eq("skip_forward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_R1, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeButtonR2ReceivedSkipsForward() {
        every { mockPreferences.getString(eq("skip_forward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_R2, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeMediaRewindSkipsBack() {
        every { mockPreferences.getString(eq("skip_backward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_REWIND, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeRSkipsBack() {
        every { mockPreferences.getString(eq("skip_backward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_R, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeButtonL1SkipsBack() {
        every { mockPreferences.getString(eq("skip_backward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_L1, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeButtonL2SkipsBack() {
        every { mockPreferences.getString(eq("skip_backward_time"), any()) } returns "0"
        every { mockMediaPlayer.seekTo(any()) } just Runs

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_L2, null)

        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeMediaStopPausesCurrentlyPlayingVideo() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeMediaStopPausesCurrentlyPlayingVideoWhenMediaControllerNotShowingShowsMediaController() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeMediaStopWhenMediaControllerShowing() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeMediaStopWhenMediaControllerIsNotPlaying() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCodeSPausesCurrentlyPlayingVideo() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_S, null)
        assertThat(result).isTrue()
    }

    @Test
    fun handlesKeyCode1ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_1, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(100L) }
    }

    @Test
    fun handlesKeyCode2ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_2, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(200L) }
    }

    @Test
    fun handlesKeyCode3ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_3, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(300L) }
    }

    @Test
    fun handlesKeyCode4ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_4, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(400L) }
    }

    @Test
    fun handlesKeyCode5ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_5, null)
        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(500L) }
    }

    @Test
    fun handlesKeyCode6ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_6, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(600L) }
    }

    @Test
    fun handlesKeyCode7ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_7, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(700L) }
    }

    @Test
    fun handlesKeyCode8ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_8, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(800L) }
    }

    @Test
    fun handlesKeyCode9ForSkipByPercentage() {
        every { mockMediaPlayer.duration } returns 1000L // Assuming getDuration() is a getter for duration property

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_9, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(900L) }
    }

    @Test
    fun handlesKeyCode0RestartsAtBeginning() {
        every { mockMediaPlayer.duration } returns 1000L // Assuming getDuration() is a getter for duration property

        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_0, null)

        assertThat(result).isTrue()
        verify { mockMediaPlayer.seekTo(0L) }
    }

    @Test
    fun unhandledKeyCodeReturnsFalse() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_AT, null)
        assertThat(result).isFalse()
    }

    private fun demandBySeconds(seconds: String, currentPosition: Long) {
        every { mockPreferences.getString(eq("next_prev_behavior"), any()) } returns seconds
        every { mockMediaPlayer.currentPosition } returns currentPosition
        every { mockMediaPlayer.duration } returns 10000L
        every { mockMediaPlayer.seekTo(any()) } just Runs
    }

    private fun demandByPercentage() {
        every { mockPreferences.getString(eq("next_prev_behavior"), any()) } returns "10%"
        every { mockMediaPlayer.currentPosition } returns 10L
        every { mockMediaPlayer.duration } returns 100L
        every { mockMediaPlayer.seekTo(any()) } just Runs
    }

    private fun demandNextQueue() {
        every { mockMediaPlayer.stop() } just Runs
    }

    private fun demandMediaPause() {
    }

    private fun demandMediaPauseWhenNotPlaying() {
    }

    @Test
    fun handlesKeyCodeInfoWhenMediaPlayerControllerIsShowing() {
        val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_INFO, null)
        assertThat(result).isTrue()
    }

    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule(), TestModule())
    }

    inner class TestModule : Module() {

        init {
            bind(SharedPreferences::class.java).toInstance(mockPreferences)
        }
    }

}

