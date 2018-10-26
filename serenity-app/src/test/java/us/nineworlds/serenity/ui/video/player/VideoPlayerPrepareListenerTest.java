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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class VideoPlayerPrepareListenerTest extends InjectingTest {

	protected VideoPlayerPrepareListener videoPlayerPrepareListener;

	@Mock
	MediaPlayer mockMediaPlayer;

	MediaController spyMediaController;

	@Mock
	Handler mockHandler;

	@Mock
	Runnable mockRunnable;

	@Mock
	MediaControllerDataObject mockMediaControllerDataObject;

	SurfaceView surfaceView;

	@Inject
	Resources resources;

	@Inject
	@ForVideoQueue
	LinkedList<VideoContentInfo> videoQueue;

	@Override
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		super.setUp();
		int resumeOffset = 0;

		surfaceView = new SurfaceView(application);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1920,
				1080);
		surfaceView.setLayoutParams(lp);

		doReturn(application).when(mockMediaControllerDataObject)
												 .getContext();

		spyMediaController = spy(new MediaController(
				mockMediaControllerDataObject));

		videoPlayerPrepareListener = new VideoPlayerPrepareListener(
				spyMediaController, surfaceView, resumeOffset, true, "1.77",
				mockHandler, mockRunnable);
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(application));
		modules.add(new TestModule());
		return modules;
	}

	@Test
	public void onPreparedSetsMediaControllerToEnabled() {
		doNothing().when(spyMediaController).setEnabled(anyBoolean());
		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);
		verify(spyMediaController).setEnabled(true);
	}

	@Test
	public void onPreparedStartsVideoPlayingWhenAutoResumeIsTrueAndPlayerIsNotPlaying() {
		doReturn(false).when(mockMediaPlayer).isPlaying();
		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);
		verify(mockMediaPlayer).start();
	}

	@Test
	public void onPreparedShowsResumeDialogWhenAutoResumeIsFalseAndResumeOffsetNotZero() {
		videoPlayerPrepareListener = new VideoPlayerPrepareListener(
				spyMediaController, surfaceView, 100, false, "1.77",
				mockHandler, mockRunnable);

		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);

		Dialog resumeDialog = ShadowDialog.getLatestDialog();
		assertThat(resumeDialog).isNotNull();
		ShadowDialog shadowDialog = shadowOf(resumeDialog);
		assertThat(shadowDialog.getTitle()).isEqualTo(
				resources.getText(R.string.resume_video));
	}

	@Test
	public void onPreparedResumeDialogIsSetToExpectedValue() {
		videoPlayerPrepareListener = new VideoPlayerPrepareListener(
				spyMediaController, surfaceView, 100, false, "1.77",
				mockHandler, mockRunnable);

		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);

		Dialog resumeDialog = ShadowDialog.getLatestDialog();
		ShadowDialog shadowDialog = shadowOf(resumeDialog);

		assertThat(shadowDialog.getTitle()).isEqualTo(
				resources.getText(R.string.resume_video));
	}

	@Test
	public void resumeDialogPostiveButtonSeeksToResumePositionWhenClicked() {
		videoPlayerPrepareListener = new VideoPlayerPrepareListener(
				spyMediaController, surfaceView, 100, false, "1.77",
				mockHandler, mockRunnable);

		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);

		AlertDialog resumeDialog = (AlertDialog) ShadowAlertDialog
				.getLatestDialog();
		Button resumeButton = resumeDialog
				.getButton(DialogInterface.BUTTON_POSITIVE);
		resumeButton.performClick();
		verify(mockMediaPlayer).seekTo(100);
	}

	@Test
	public void resumeDialogNegativeButtonStartsPlayingWhenClicked() {
		videoPlayerPrepareListener = new VideoPlayerPrepareListener(
				spyMediaController, surfaceView, 100, false, "1.77",
				mockHandler, mockRunnable);

		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);

		AlertDialog resumeDialog = (AlertDialog) ShadowAlertDialog
				.getLatestDialog();
		Button resumeButton = resumeDialog
				.getButton(DialogInterface.BUTTON_NEGATIVE);

		resumeButton.performClick();

		verify(mockMediaPlayer).start();
		verify(mockMediaPlayer, times(0)).seekTo(anyInt());
	}

	@Test
	public void startsVideoAutomaticalyWhenVideoQueueIsNotEmpty() {
		videoQueue.add(Mockito.mock(VideoContentInfo.class));
		videoPlayerPrepareListener = new VideoPlayerPrepareListener(
				spyMediaController, surfaceView, 0, false, "1.77", mockHandler,
				mockRunnable);

		videoPlayerPrepareListener.onPrepared(mockMediaPlayer);

		verify(mockMediaPlayer).start();
		verify(mockMediaPlayer, times(0)).seekTo(anyInt());
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, injects = {
		VideoPlayerPrepareListener.class,
		VideoPlayerPrepareListenerTest.class })
	public class TestModule {

	}

}
