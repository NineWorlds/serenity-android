package us.nineworlds.serenity.ui.video.player

import android.content.SharedPreferences
import android.view.KeyEvent
import com.google.android.exoplayer2.SimpleExoPlayer
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import toothpick.config.Module
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class VideoKeyCodeHandlerDelegateTest : InjectingTest() {

  @Mock
  internal lateinit var mockPreferences: SharedPreferences

  @Mock
  internal lateinit var mockMediaPlayer: SimpleExoPlayer

  @Mock
  internal lateinit var mockPresenter: ExoplayerContract.ExoplayerPresenter

  lateinit var keyCodeHandler: VideoKeyCodeHandlerDelegate

  @Mock
  lateinit var mockActivity: ExoplayerVideoActivity

  @Before
  @Throws(Exception::class)
  override fun setUp() {
    Robolectric.getBackgroundThreadScheduler().pause()
    Robolectric.getForegroundThreadScheduler().pause()

    initMocks(this)
    super.setUp()

    doReturn("5000").`when`(mockPreferences).getString("osd_display_time", "5000")

    keyCodeHandler = VideoKeyCodeHandlerDelegate(mockMediaPlayer, mockActivity, mockPresenter)
  }

  @After
  fun tearDown() {
    if (mockActivity != null) {
      mockActivity!!.finish()
    }
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
  fun HandlesKeyCodeInfoWhenGameControllerButtonYIsPressed() {
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_Y, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodePauseWhenMediaPlayerIsPlaying() {
    demandMediaPause()
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_PAUSE,
        null)
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
        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, null)
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
        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeMediaNextQueueEntryIsPlaying() {
    demandNextQueue()
    doReturn("queue").`when`(mockPreferences).getString(
        eq<String>("next_prev_behavior"), anyString())

    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)
    assertThat(result).isTrue()
    verify(mockPreferences).getString("next_prev_behavior", "queue")
  }

  @Test
  fun handlesKeyCodeMediaNextQueueEntryIsNotPlaying() {
    doReturn("queue").`when`(mockPreferences).getString(
        eq<String>("next_prev_behavior"), anyString())
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)
    assertThat(result).isTrue()
    verify(mockPreferences).getString("next_prev_behavior", "queue")
  }

  @Test
  fun handlesKeyCodeMediaNextByPercentatage() {
    demandByPercentage()
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

    assertThat(result).isTrue()
    verify(mockPreferences).getString("next_prev_behavior", "queue")
    verify(mockMediaPlayer).seekTo(20)
  }

  @Test
  fun handlesKeyCodeMediaNextBySecondsUnderDuration() {
    demandBySeconds("2000", 1000L)
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

    assertThat(result).isTrue()
    verify(mockPreferences).getString("next_prev_behavior", "queue")
    verify(mockMediaPlayer).seekTo(3000)
  }

  @Test
  fun handlesKeyCodeMediaNextBySecondsGreaterThanDuration() {
    demandBySeconds("11000", 1000L)
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

    assertThat(result).isTrue()
    verify(mockPreferences).getString("next_prev_behavior", "queue")
    verify(mockMediaPlayer).seekTo(9999)
  }

  @Test
  fun handlesKeyCodeMediaNextBySecondsLessThanZero() {
    demandBySeconds("-11000", 1000L)
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT, null)

    assertThat(result).isTrue()
    verify(mockPreferences).getString("next_prev_behavior", "queue")
    verify(mockMediaPlayer).seekTo(0)
  }

  @Test
  fun handlesKeyCodeMediaPreviousWhenUsingPercentage() {
    doReturn("10%").`when`(mockPreferences).getString(
        eq("next_prev_behavior"), anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    doReturn(10L).`when`(mockMediaPlayer).getCurrentPosition()
    doReturn(100L).`when`(mockMediaPlayer).getDuration()

    val result = keyCodeHandler.onKeyDown(
        KeyEvent.KEYCODE_MEDIA_PREVIOUS, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(0)
  }

  @Test
  fun handlesKeyCodeMediaPreviousWhenUsingDuration() {
    doReturn("2000").`when`(mockPreferences).getString(
        eq("next_prev_behavior"), anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    doReturn(9000L).`when`(mockMediaPlayer).getCurrentPosition()
    doReturn(10000L).`when`(mockMediaPlayer).getDuration()

    val result = keyCodeHandler.onKeyDown(
        KeyEvent.KEYCODE_MEDIA_PREVIOUS, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(7000)
  }

  @Test
  fun handlesKeyCodeMediaFastForwardReceived() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_forward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(
        KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeFReceivedSkipsForward() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_forward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_F, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeButtonR1ReceivedSkipsForward() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_forward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_R1, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeButtonR2ReceivedSkipsForward() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_forward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_R2, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeMediaRewindSkipsBack() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_backward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(
        KeyEvent.KEYCODE_MEDIA_REWIND, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeRSkipsBack() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_backward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_R, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeButtonL1SkipsBack() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_backward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_L1, null)
    assertThat(result).isTrue()
  }

  @Test
  fun handlesKeyCodeButtonL2SkipsBack() {
    doReturn("0").`when`(mockPreferences).getString(eq<String>("skip_backward_time"),
        anyString())
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
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
    doReturn(1000L).`when`(mockMediaPlayer).duration
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_1, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(100L)
  }

  @Test
  fun handlesKeyCode2ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).duration
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_2, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(200L)
  }

  @Test
  fun handlesKeyCode3ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).duration
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_3, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(300L)
  }

  @Test
  fun handlesKeyCode4ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).duration
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_4, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(400L)
  }

  @Test
  fun handlesKeyCode5ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).duration
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_5, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(500L)
  }

  @Test
  fun handlesKeyCode6ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).duration
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_6, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(600L)
  }

  @Test
  fun handlesKeyCode7ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).getDuration()
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_7, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(700L)
  }

  @Test
  fun handlesKeyCode8ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).getDuration()
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_8, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(800L)
  }

  @Test
  fun handlesKeyCode9ForSkipByPercentage() {
    doReturn(1000L).`when`(mockMediaPlayer).getDuration()
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_9, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(900L)
  }

  @Test
  fun handlesKeyCode0RestartsAtBeginning() {
    doReturn(1000L).`when`(mockMediaPlayer).getDuration()
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_0, null)
    assertThat(result).isTrue()
    verify(mockMediaPlayer).seekTo(0L)
  }

  @Test
  fun unhandledKeyCodeReturnsFalse() {
    val result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_AT, null)
    assertThat(result).isFalse()
  }

  private fun demandBySeconds(seconds: String, currentPosition: Long) {
    doReturn(seconds).`when`(mockPreferences).getString(
        eq<String>("next_prev_behavior"), anyString())
    doReturn(currentPosition).`when`(mockMediaPlayer).getCurrentPosition()
    doReturn(10000L).`when`(mockMediaPlayer).getDuration()
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
  }

  private fun demandByPercentage() {
    doReturn("10%").`when`(mockPreferences).getString(
        eq<String>("next_prev_behavior"), anyString())
    doReturn(10L).`when`(mockMediaPlayer).currentPosition
    doReturn(100L).`when`(mockMediaPlayer).duration
    doNothing().`when`(mockMediaPlayer).seekTo(anyLong())
  }

  private fun demandNextQueue() {
    doNothing().`when`(mockMediaPlayer).stop()
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
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {

    init {
      bind(SharedPreferences::class.java).toInstance(mockPreferences)
    }
  }

}

