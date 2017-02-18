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

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.inject.Singleton;
import org.fest.assertions.api.ANDROID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowSurfaceView;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity.VideoPlayerOnCompletionListener;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SerenitySurfaceViewVideoPlayerTest extends InjectingTest {

	@Mock
	KeyEvent keyEvent;

	@Mock
	MediaPlayer mockMediaPlayer;

	@Mock
	SurfaceHolder mockSurfaceHolder;

	@Mock
	ActionBar mockActionBar;

	@Mock
	Intent mockIntent;

	@Mock
	Bundle mockBundle;

	@Mock
	MediaControllerDataObject mockMediaControllerDataObject;

	MediaController spyMediaController;

	@Mock
	LinkedList<VideoContentInfo> videoQueue;

	@Mock
	VideoContentInfo mockVideoContentInfo;

	SerenitySurfaceViewVideoActivity activity;

	@Override
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		super.setUp();
		ShadowApplication shadowApplication = Shadows
				.shadowOf(application);
		shadowApplication
		.declareActionUnbindable("com.google.android.gms.analytics.service.START");

		doReturn(true).when(videoQueue).isEmpty();

		activity = Robolectric
				.buildActivity(SerenitySurfaceViewVideoActivity.class).create()
				.get();

		doReturn(activity).when(mockMediaControllerDataObject).getContext();
		spyMediaController = spy(new MediaController(
				mockMediaControllerDataObject));
	}

	@After
	public void tearDown() throws Exception {
		if (activity != null) {
			activity.finish();
		}

		videoQueue.clear();
	}

	@Test
	public void onBackPressedIsCalledWhenKeyCodeBackIsReceived() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);
		doNothing().when(spyActivity).onBackPressed();
		spyActivity.onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent);
		verify(spyActivity).onBackPressed();
	}

	@Test
	public void onKeyDownReturnsTrueWhenKeyCodeBackIsReceived() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);
		doNothing().when(spyActivity).onBackPressed();
		boolean result = spyActivity.onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent);
		assertThat(result).isTrue();
	}

	@Test
	public void surfaceCreatedSetsMediaPlayerOnPreparedListener() {
		doNothing().when(mockMediaPlayer).setOnPreparedListener(
				any(VideoPlayerPrepareListener.class));

		activity.surfaceCreated(mockSurfaceHolder);

		verify(mockMediaPlayer).setOnPreparedListener(
				any(VideoPlayerPrepareListener.class));
	}

	@Test
	public void surfaceCreatedSetsMediaPlayerOnCompletionListener() {
		doNothing().when(mockMediaPlayer).setOnCompletionListener(
				any(VideoPlayerOnCompletionListener.class));

		activity.surfaceCreated(mockSurfaceHolder);

		verify(mockMediaPlayer).setOnCompletionListener(
				any(VideoPlayerOnCompletionListener.class));
	}

	@Test
	public void surfaceCreatedCallsMediaPlayerPrepareAsync() {
		activity.surfaceCreated(mockSurfaceHolder);
		verify(mockMediaPlayer).prepareAsync();
	}

	@Test
	public void surfaceDestroyedReleasesMediaPlayerWhenNotInReleaseState() {
		activity.setMediaplayerReleased(false);
		activity.surfaceDestroyed(mockSurfaceHolder);
		assertThat(activity.isMediaplayerReleased()).isTrue();
		verify(mockMediaPlayer).release();
	}

	@Test
	public void surfaceDestroyedDoesNotReleaseMediaPlayerWhenInReleaseState() {
		activity.setMediaplayerReleased(true);
		activity.surfaceDestroyed(mockSurfaceHolder);
		assertThat(activity.isMediaplayerReleased()).isTrue();
		verify(mockMediaPlayer, times(0)).release();
	}

	@Test
	public void surfaceViewKeepScreenOnIsSetToTrueFromOnCreate() {
		SurfaceView surfaceView = (SurfaceView) activity
				.findViewById(R.id.surfaceView);
		ANDROID.assertThat(surfaceView).isKeepingScreenOn();
	}

	@Test
	public void surfaceViewHolderIsSetToAnInstanceOfSerenitySurfaceVideoPlayerActivity() {
		SurfaceView surfaceView = (SurfaceView) activity
				.findViewById(R.id.surfaceView);

		ShadowSurfaceView shadowSurfaceView = Shadows
				.shadowOf(surfaceView);
		Set callbacks = shadowSurfaceView.getFakeSurfaceHolder().getCallbacks();
		assertThat(callbacks).isNotEmpty();
	}

	@Test
	public void retrieveIntentExtrasWithIntentSetsAutoResumeFromBundleExtra() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);

		doReturn(mockBundle).when(mockIntent).getExtras();
		doReturn(true).when(mockBundle).getBoolean("autoResume", false);
		doReturn(mockIntent).when(spyActivity).getIntent();
		doNothing().when(spyActivity).playBackFromVideoQueue();
		doNothing().when(spyActivity).playbackFromIntent(any(Bundle.class));

		spyActivity.retrieveIntentExtras();

		verify(mockBundle).getBoolean("autoResume", false);
	}

	@Test
	public void onKeyDownCallsVideoPlayKeyCodeHandler() {
		VideoPlayerKeyCodeHandler spyKeyCodeHandler = spy(new VideoPlayerKeyCodeHandler(
				null, null, 0, null, null, null, null));
		activity.setVideoPlayerKeyCodeHandler(spyKeyCodeHandler);
		doReturn(true).when(spyKeyCodeHandler).onKeyDown(anyInt(),
				any(KeyEvent.class), anyBoolean());

		boolean result = activity.onKeyDown(0, Mockito.mock(KeyEvent.class));

		verify(spyKeyCodeHandler).onKeyDown(anyInt(), any(KeyEvent.class),
				anyBoolean());
		assertThat(result).isTrue();

	}

	@Test
	public void onKeyDownReturnsFalseWhenKeyCodeNotHandled() {
		VideoPlayerKeyCodeHandler spyKeyCodeHandler = spy(new VideoPlayerKeyCodeHandler(
				null, null, 0, null, null, null, null));
		activity.setVideoPlayerKeyCodeHandler(spyKeyCodeHandler);
		doReturn(false).when(spyKeyCodeHandler).onKeyDown(anyInt(),
				any(KeyEvent.class), anyBoolean());

		boolean result = activity.onKeyDown(0, Mockito.mock(KeyEvent.class));

		verify(spyKeyCodeHandler).onKeyDown(anyInt(), any(KeyEvent.class),
				anyBoolean());
		assertThat(result).isFalse();

	}

	@Test
	public void onResumeCallsVisibileInBackground() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);
		doNothing().when(spyActivity).visibleInBackground();
		spyActivity.onResume();
		verify(spyActivity).visibleInBackground();
	}

	@Test
	public void onPauseCallsVisibileInBackground() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);
		doNothing().when(spyActivity).visibleInBackground();
		spyActivity.onPause();
		verify(spyActivity).visibleInBackground();
	}

	@Test
	public void onBackPressedHidesMediaControllerIfItIsShowing() {
		doNothing().when(spyMediaController).hide();
		doReturn(true).when(spyMediaController).isShowing();
		activity.setSerenityMediaController(spyMediaController);

		activity.onBackPressed();

		verify(spyMediaController).hide();
		verify(spyMediaController).isShowing();
	}

	@Test
	public void onBackPressedStopsPlayingMediaWhenPlayingAndInAValidState() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);
		doReturn(true).when(spyMediaController).isShowing();
		doReturn(true).when(spyActivity).isMediaPlayerStateValid();
		doReturn(true).when(mockMediaPlayer).isPlaying();

		spyActivity.setSerenityMediaController(spyMediaController);

		spyActivity.onBackPressed();

		verify(mockMediaPlayer).stop();
		verify(mockMediaPlayer).isPlaying();
		verify(spyActivity).isMediaPlayerStateValid();
	}

	@Test
	public void playbackFromVideoQueueDoesNothingWhenVideoQueueIsEmpty() {
		activity.playBackFromVideoQueue();
		verify(videoQueue, never()).poll();
	}

	@Test
	public void playbackFromVideoQueueReturnsVideoContentInfoWhenPolled() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);

		doNothing().when(spyActivity).initMediaController(
				any(MediaControllerDataObject.class));
		doReturn(mockVideoContentInfo).when(videoQueue).poll();
		doReturn(false).when(videoQueue).isEmpty();

		spyActivity.playBackFromVideoQueue();

		verify(videoQueue).poll();
		verify(spyActivity).initMediaController(
				any(MediaControllerDataObject.class));
	}

	@Test
	public void playbackFromVideoQueueRetrievesParentPosterUrlWhenAnEpisodeIsFetchedFromTheQueue() {
		SerenitySurfaceViewVideoActivity spyActivity = spy(activity);
		EpisodePosterInfo videoContentInfo = Mockito
				.mock(EpisodePosterInfo.class);
		doNothing().when(spyActivity).initMediaController(
				any(MediaControllerDataObject.class));
		doReturn(videoContentInfo).when(videoQueue).poll();
		doReturn("http://plexserver/some/entry.gif").when(videoContentInfo)
				.getParentPosterURL();

		doReturn(false).when(videoQueue).isEmpty();

		spyActivity.playBackFromVideoQueue();

		verify(videoQueue).poll();
		verify(videoContentInfo, times(2)).getParentPosterURL();

		verify(spyActivity).initMediaController(
				any(MediaControllerDataObject.class));
	}

	@Test
	public void setExitResultCodeSetsLastPlayBackPosition() {
		activity.setPlaybackPos(100);
		activity.setExitResultCodeFinished();

		ShadowActivity shadowActivity = Shadows.shadowOf(activity);
		Intent resultIntent = shadowActivity.getResultIntent();
		int position = resultIntent.getExtras().getInt("position");
		assertThat(position).isEqualTo(100);
	}

	@Test
	public void onTouchEventMotionActionDownHidesMediaControllerWhenShowing() {
		MotionEvent mockMotionEvent = Mockito.mock(MotionEvent.class);
		doReturn(MotionEvent.ACTION_DOWN).when(mockMotionEvent).getAction();
		doReturn(true).when(spyMediaController).isShowing();
		activity.setSerenityMediaController(spyMediaController);

		activity.onTouchEvent(mockMotionEvent);

		verify(spyMediaController).isShowing();
		verify(spyMediaController).hide();
	}

	@Test
	public void onTouchEventMotionActionDownShowsMediaControllerWhenHidden() {
		MotionEvent mockMotionEvent = Mockito.mock(MotionEvent.class);
		doReturn(MotionEvent.ACTION_DOWN).when(mockMotionEvent).getAction();
		doReturn(false).when(spyMediaController).isShowing();
		activity.setSerenityMediaController(spyMediaController);

		activity.onTouchEvent(mockMotionEvent);

		verify(spyMediaController).isShowing();
		verify(spyMediaController).show();
	}

	@Test
	public void onTouchEventCallsSuperWhenEventNotHandled() {
		MotionEvent mockMotionEvent = Mockito.mock(MotionEvent.class);
		doReturn(MotionEvent.ACTION_UP).when(mockMotionEvent).getAction();
		activity.setSerenityMediaController(spyMediaController);

		activity.onTouchEvent(mockMotionEvent);

		verify(spyMediaController, never()).isShowing();
		verify(spyMediaController, never()).show();
		verify(spyMediaController, never()).hide();
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(RuntimeEnvironment.application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(addsTo = AndroidModule.class, includes = SerenityModule.class, overrides = true, injects = {
		SerenitySurfaceViewVideoActivity.class,
		SerenitySurfaceViewVideoPlayerTest.class })
	public class TestModule {

		@Provides
		@ForVideoQueue
		@Singleton
		LinkedList<VideoContentInfo> providesVideoQueue() {
			return videoQueue;
		}

		@Provides
		MediaPlayer providesMediaPlayer() {
			return mockMediaPlayer;
		}

	}

}
