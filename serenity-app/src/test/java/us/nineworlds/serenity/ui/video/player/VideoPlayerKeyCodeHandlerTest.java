/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.video.player;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import org.fest.assertions.api.ANDROID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VideoPlayerKeyCodeHandlerTest extends InjectingTest {

	private static final int OSD_DISPLAY_TIME = 5000;
	private static final int PLEX_UPDATE_TIME = 5000;

	@Mock
	MediaPlayer mockMediaPlayer;

	@Mock
	MediaControllerDataObject mockMediaControllerDataObject;

	MediaController spyMediaController;

	@Mock
	Handler mockProgressReportingHandler;

	@Mock
	Runnable mockProgressRunnable;

	@Mock
	SharedPreferences mockPreferences;

	@Mock
	Editor mockEditor;

	protected VideoPlayerKeyCodeHandler keyCodeHandler;

	protected SerenitySurfaceViewVideoActivity mockActivity;
	protected View timeOfDay;

	@Override
	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundThreadScheduler().pause();
		Robolectric.getForegroundThreadScheduler().pause();

		MockitoAnnotations.initMocks(this);
		super.setUp();

		doReturn("5000").when(mockPreferences).getString("osd_display_time",
				"5000");
		mockActivity = Robolectric
				.buildActivity(SerenitySurfaceViewVideoActivity.class).create()
				.get();

		doReturn(mockActivity).when(mockMediaControllerDataObject).getContext();
		spyMediaController = spy(new MediaController(
				mockMediaControllerDataObject));

		timeOfDay = new View(RuntimeEnvironment.application);

		keyCodeHandler = new VideoPlayerKeyCodeHandler(mockMediaPlayer,
				spyMediaController, OSD_DISPLAY_TIME,
				mockProgressReportingHandler, mockProgressRunnable, timeOfDay,
				mockActivity);
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(RuntimeEnvironment.application));
		modules.add(new TestModule());
		return modules;
	}

	@Test
	public void handlesKeyCodeInfo() {
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_INFO, null,
				true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeInfoWhenMenuKeyIsPressed() {
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MENU, null,
				true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeInfoWhenIIsPressed() {
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_I, null,
				true);
		assertThat(result).isTrue();
	}

	@Test
	public void HandlesKeyCodeInfoWhenGameControllerButtonYIsPressed() {
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_Y,
				null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodePauseWhenMediaPlayerIsPlaying() {
		demandMediaPause();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_PAUSE,
				null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
		verify(spyMediaController).show(OSD_DISPLAY_TIME);
		verify(mockProgressReportingHandler).removeCallbacks(
				mockProgressRunnable);
	}

	@Test
	public void handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeP() {
		demandMediaPause();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_P, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
		verify(spyMediaController).show(OSD_DISPLAY_TIME);
		verify(mockProgressReportingHandler).removeCallbacks(
				mockProgressRunnable);
	}

	@Test
	public void handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeSpace() {
		demandMediaPause();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_SPACE, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
		verify(spyMediaController).show(OSD_DISPLAY_TIME);
		verify(mockProgressReportingHandler).removeCallbacks(
				mockProgressRunnable);
	}

	@Test
	public void handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeButtonA() {
		demandMediaPause();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_A,
				null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
		verify(spyMediaController).show(OSD_DISPLAY_TIME);
		verify(mockProgressReportingHandler).removeCallbacks(
				mockProgressRunnable);
	}

	@Test
	public void handlesKeyCodePauseWhenMediaPlayerIsPlayingKeyCodeMediaPlayPause() {
		demandMediaPause();
		boolean result = keyCodeHandler.onKeyDown(
				KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
		verify(spyMediaController).show(OSD_DISPLAY_TIME);
		verify(mockProgressReportingHandler).removeCallbacks(
				mockProgressRunnable);
	}

	@Test
	public void handlesKeyCodePauseWhenMediaPlayerIsNotPlaying() {
		demandMediaPauseWhenNotPlaying();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_PAUSE,
				null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).start();
		verify(spyMediaController).hide();
		verify(mockProgressReportingHandler).postDelayed(mockProgressRunnable,
				PLEX_UPDATE_TIME);
	}

	@Test
	public void handlesKeyCodePlayPauseWhenMediaPlayerIsNotPlaying() {
		demandMediaPauseWhenNotPlaying();
		boolean result = keyCodeHandler.onKeyDown(
				KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).start();
		verify(spyMediaController).hide();
		verify(mockProgressReportingHandler).postDelayed(mockProgressRunnable,
				PLEX_UPDATE_TIME);
	}

	@Test
	public void handlesKeyCodeMediaNextQueueEntryIsPlaying() {
		demandNextQueue();
		doReturn("queue").when(mockPreferences).getString(
				eq("next_prev_behavior"), anyString());

		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT,
				null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).stop();
		ShadowActivity shadowActivity = shadowOf(mockActivity);
		assertThat(shadowActivity.isFinishing()).isTrue();
		verify(mockPreferences).getString("next_prev_behavior", "queue");
	}

	@Test
	public void handlesKeyCodeMediaNextQueueEntryIsNotPlaying() {
		doReturn(false).when(mockMediaPlayer).isPlaying();
		doReturn("queue").when(mockPreferences).getString(
				eq("next_prev_behavior"), anyString());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT,
				null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer, times(0)).stop();
		ShadowActivity shadowActivity = shadowOf(mockActivity);
		assertThat(shadowActivity.isFinishing()).isTrue();
		verify(mockPreferences).getString("next_prev_behavior", "queue");
	}

	@Test
	public void handlesKeyCodeMediaNextByPercentatage() {
		demandByPercentage();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT,
				null, true);

		assertThat(result).isTrue();
		verify(mockPreferences).getString("next_prev_behavior", "queue");
		verify(mockMediaPlayer).seekTo(20);
	}

	@Test
	public void handlesKeyCodeMediaNextBySecondsUnderDuration() {
		demandBySeconds("2000", 1000);
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT,
				null, true);

		assertThat(result).isTrue();
		verify(mockPreferences).getString("next_prev_behavior", "queue");
		verify(mockMediaPlayer).seekTo(3000);
	}

	@Test
	public void handlesKeyCodeMediaNextBySecondsGreaterThanDuration() {
		demandBySeconds("11000", 1000);
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT,
				null, true);

		assertThat(result).isTrue();
		verify(mockPreferences).getString("next_prev_behavior", "queue");
		verify(mockMediaPlayer).seekTo(9999);
	}

	@Test
	public void handlesKeyCodeMediaNextBySecondsLessThanZero() {
		demandBySeconds("-11000", 1000);
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_NEXT,
				null, true);

		assertThat(result).isTrue();
		verify(mockPreferences).getString("next_prev_behavior", "queue");
		verify(mockMediaPlayer).seekTo(0);
	}

	@Test
	public void handlesKeyCodeHidesTimeOfDayWhenKeyCodeT() {
		timeOfDay.setVisibility(View.VISIBLE);
		doReturn(mockEditor).when(mockPreferences).edit();
		doReturn(true).when(mockPreferences).getBoolean(eq("showTimeOfDay"),
				anyBoolean());
		doNothing().when(mockEditor).apply();
		doReturn(mockEditor).when(mockEditor).putBoolean(eq("showTimeOfDay"),
				anyBoolean());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_T, null,
				true);
		assertThat(result).isTrue();
		ANDROID.assertThat(timeOfDay).isGone();
	}

	@Test
	public void handlesKeyCodeShowsTimeOfDayWhenKeyCodeT() {
		timeOfDay.setVisibility(View.GONE);
		doReturn(mockEditor).when(mockPreferences).edit();
		doReturn(false).when(mockPreferences).getBoolean(eq("showTimeOfDay"),
				anyBoolean());
		doNothing().when(mockEditor).apply();
		doReturn(mockEditor).when(mockEditor).putBoolean(eq("showTimeOfDay"),
				anyBoolean());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_T, null,
				true);
		assertThat(result).isTrue();
		ANDROID.assertThat(timeOfDay).isVisible();
	}

	@Test
	public void handlesKeyCodeMediaPreviousWhenUsingPercentage() {
		doReturn("10%").when(mockPreferences).getString(
				eq("next_prev_behavior"), anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		doReturn(10).when(mockMediaPlayer).getCurrentPosition();
		doReturn(100).when(mockMediaPlayer).getDuration();

		boolean result = keyCodeHandler.onKeyDown(
				KeyEvent.KEYCODE_MEDIA_PREVIOUS, null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(0);
	}

	@Test
	public void handlesKeyCodeMediaPreviousWhenUsingDuration() {
		doReturn("2000").when(mockPreferences).getString(
				eq("next_prev_behavior"), anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		doReturn(9000).when(mockMediaPlayer).getCurrentPosition();
		doReturn(10000).when(mockMediaPlayer).getDuration();

		boolean result = keyCodeHandler.onKeyDown(
				KeyEvent.KEYCODE_MEDIA_PREVIOUS, null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(7000);
	}

	@Test
	public void handlesKeyCodeMediaFastForwardReceived() {
		doReturn("0").when(mockPreferences).getString(eq("skip_forward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(
				KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeFReceivedSkipsForward() {
		doReturn("0").when(mockPreferences).getString(eq("skip_forward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_F, null,
				true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeButtonR1ReceivedSkipsForward() {
		doReturn("0").when(mockPreferences).getString(eq("skip_forward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_R1,
				null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeButtonR2ReceivedSkipsForward() {
		doReturn("0").when(mockPreferences).getString(eq("skip_forward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_R2,
				null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeMediaRewindSkipsBack() {
		doReturn("0").when(mockPreferences).getString(eq("skip_backward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(
				KeyEvent.KEYCODE_MEDIA_REWIND, null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeRSkipsBack() {
		doReturn("0").when(mockPreferences).getString(eq("skip_backward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_R, null,
				true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeButtonL1SkipsBack() {
		doReturn("0").when(mockPreferences).getString(eq("skip_backward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_L1,
				null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeButtonL2SkipsBack() {
		doReturn("0").when(mockPreferences).getString(eq("skip_backward_time"),
				anyString());
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_BUTTON_L2,
				null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeMediaStopPausesCurrentlyPlayingVideo() {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).pause();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP,
				null, true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
	}

	@Test
	public void handlesKeyCodeMediaStopPausesCurrentlyPlayingVideoWhenMediaControllerNotShowingShowsMediaController() {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).pause();
		doReturn(false).when(spyMediaController).isShowing();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP,
				null, true);
		assertThat(result).isTrue();
		verify(spyMediaController).show(anyInt());
	}

	@Test
	public void handlesKeyCodeMediaStopWhenMediaControllerShowing() {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).pause();
		doReturn(true).when(spyMediaController).isShowing();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP,
				null, true);
		assertThat(result).isTrue();
		verify(spyMediaController, times(0)).show(anyInt());
	}

	@Test
	public void handlesKeyCodeMediaStopWhenMediaControllerIsNotPlaying() {
		doReturn(false).when(mockMediaPlayer).isPlaying();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_MEDIA_STOP,
				null, true);
		assertThat(result).isTrue();
	}

	@Test
	public void handlesKeyCodeSPausesCurrentlyPlayingVideo() {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).pause();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_S, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).pause();
	}

	@Test
	public void handlesKeyCode1ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_1, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(100);
	}

	@Test
	public void handlesKeyCode2ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_2, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(200);
	}

	@Test
	public void handlesKeyCode3ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_3, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(300);
	}

	@Test
	public void handlesKeyCode4ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_4, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(400);
	}

	@Test
	public void handlesKeyCode5ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_5, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(500);
	}

	@Test
	public void handlesKeyCode6ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_6, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(600);
	}

	@Test
	public void handlesKeyCode7ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_7, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(700);
	}

	@Test
	public void handlesKeyCode8ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_8, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(800);
	}

	@Test
	public void handlesKeyCode9ForSkipByPercentage() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_9, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(900);
	}

	@Test
	public void handlesKeyCode0RestartsAtBeginning() {
		doReturn(1000).when(mockMediaPlayer).getDuration();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_0, null,
				true);
		assertThat(result).isTrue();
		verify(mockMediaPlayer).seekTo(0);
	}

	@Test
	public void unhandledKeyCodeReturnsFalse() {
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_AT, null,
				true);
		assertThat(result).isFalse();
	}

	private void demandBySeconds(String seconds, int currentPosition) {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doReturn(seconds).when(mockPreferences).getString(
				eq("next_prev_behavior"), anyString());
		doReturn(currentPosition).when(mockMediaPlayer).getCurrentPosition();
		doReturn(10000).when(mockMediaPlayer).getDuration();
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
	}

	private void demandByPercentage() {
		doReturn(false).when(mockMediaPlayer).isPlaying();
		doReturn("10%").when(mockPreferences).getString(
				eq("next_prev_behavior"), anyString());
		doReturn(10).when(mockMediaPlayer).getCurrentPosition();
		doReturn(100).when(mockMediaPlayer).getDuration();
		doNothing().when(mockMediaPlayer).seekTo(anyInt());
	}

	private void demandNextQueue() {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).stop();
	}

	private void demandMediaPause() {
		doReturn(true).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).pause();
		doNothing().when(spyMediaController).show(anyInt());
		doNothing().when(mockProgressReportingHandler).removeCallbacks(
				any(Runnable.class));
	}

	private void demandMediaPauseWhenNotPlaying() {
		doReturn(false).when(mockMediaPlayer).isPlaying();
		doNothing().when(mockMediaPlayer).start();
		doNothing().when(spyMediaController).hide();
		doReturn(true).when(mockProgressReportingHandler).postDelayed(
				any(Runnable.class), anyInt());
	}

	@Test
	public void returnsFalseWhenKeyCodeInfoAndPlayerStateInvalid() {
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_INFO, null,
				false);
		assertThat(result).isFalse();
	}

	@Test
	public void handlesKeyCodeInfoWhenMediaPlayerControllerIsShowing() {
		doReturn(true).when(spyMediaController).isShowing();
		doNothing().when(spyMediaController).hide();
		boolean result = keyCodeHandler.onKeyDown(KeyEvent.KEYCODE_INFO, null,
				true);
		assertThat(result).isTrue();
		verify(spyMediaController).hide();
		verify(spyMediaController).isShowing();
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
		VideoPlayerKeyCodeHandlerTest.class,
		VideoPlayerKeyCodeHandler.class })
	public class TestModule {

		@Provides
		@Singleton
		SharedPreferences providesSharedPreferences() {
			return mockPreferences;
		}

	}

}
