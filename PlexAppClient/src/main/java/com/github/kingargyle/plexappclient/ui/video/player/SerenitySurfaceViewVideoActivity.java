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

package com.github.kingargyle.plexappclient.ui.video.player;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.ui.video.player.MediaController.MediaPlayerControl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

/**
 * @author dcarver
 * 
 */
public class SerenitySurfaceViewVideoActivity extends Activity implements
		SurfaceHolder.Callback {

	static final String TAG = "SerenitySurfaceViewVideoActivity";
	static final int CONTROLLER_DELAY = 16000; // Six seconds
	private MediaPlayer mediaPlayer;
	private String videoURL;
	private SurfaceView surfaceView;
	private MediaController mediaController;
	private String aspectRatio;
	private String videoId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mediaController != null) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			}
			mediaController.show(CONTROLLER_DELAY);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setDataSource(videoURL);
			mediaPlayer.prepare();

			android.view.ViewGroup.LayoutParams lp = setupAspectRatio();

			surfaceView.setLayoutParams(lp);

			mediaController.setEnabled(true);

			mediaPlayer.start();
			
			new WatchedVideoRequest(videoId).execute();
			
			if (mediaPlayer.isPlaying()) {
				mediaController.show(CONTROLLER_DELAY);
			}

		} catch (Exception ex) {
			Log.e(TAG, "Video Playback Error. ", ex);
		}

	}

	/**
	 * Setup the aspect ratio for the SurfaceView
	 * 
	 * @return
	 */
	protected android.view.ViewGroup.LayoutParams setupAspectRatio() {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) surfaceView
				.getLayoutParams();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean preferPlexAspectRatio = prefs.getBoolean("plex_aspect_ratio",
				false);

		int surfaceViewHeight = surfaceView.getHeight();
		int surfaceViewWidth = surfaceView.getWidth();

		float videoWidth = mediaPlayer.getVideoWidth();
		float videoHeight = mediaPlayer.getVideoHeight();

		float ratioWidth = surfaceViewWidth / videoWidth;
		float ratioHeight = surfaceViewHeight / videoHeight;
		float aspectRatio = videoWidth / videoHeight;

		if (preferPlexAspectRatio && this.aspectRatio != null) {
			aspectRatio = Float.parseFloat(this.aspectRatio);
		}

		if (videoHeight == 480 && videoWidth == 720) {
			aspectRatio = (float) 1.78;
		}

		if (ratioWidth > ratioHeight) {
			lp.width = (int) (surfaceViewHeight * aspectRatio);
			lp.height = surfaceViewHeight;
		} else {
			lp.width = surfaceViewWidth;
			lp.height = (int) (surfaceViewWidth / aspectRatio);
		}

		if (lp.width > surfaceViewWidth) {
			lp.width = surfaceViewWidth;
		}

		if (lp.height > surfaceViewHeight) {
			lp.height = surfaceViewHeight;
		}

		return lp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
	 * SurfaceHolder)
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		mediaPlayer.release();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_playback);

		videoURL = getIntent().getExtras().getString("videoUrl");
		videoId = getIntent().getExtras().getString("id");
		String summary = getIntent().getExtras().getString("summary");
		String title = getIntent().getExtras().getString("title");
		aspectRatio = getIntent().getExtras().getString("aspectRatio");

		mediaPlayer = new MediaPlayer();
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceView.setKeepScreenOn(true);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setSizeFromLayout();

		mediaController = new MediaController(this, summary, title);
		mediaController.setAnchorView(surfaceView);
		mediaController.setMediaPlayer(new MediaPlayerControl() {

			public void start() {
				mediaPlayer.start();
			}

			public void seekTo(long pos) {
				mediaPlayer.seekTo((int) pos);
			}

			public void pause() {
				mediaPlayer.pause();

			}

			public boolean isPlaying() {
				return mediaPlayer.isPlaying();
			}

			public long getDuration() {
				return mediaPlayer.getDuration();
			}

			public long getCurrentPosition() {
				return mediaPlayer.getCurrentPosition();
			}

			public int getBufferPercentage() {
				return 0;
			}

			public boolean canSeekForward() {
				return true;
			}

			public boolean canSeekBackward() {
				return true;
			}

			public boolean canPause() {
				return true;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mediaController.isShowing()) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				mediaController.hide();
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				return super.onKeyDown(keyCode, event);
			}
		} else {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				return super.onKeyDown(keyCode, event);
			}
		}

		if (keyCode == KeyEvent.KEYCODE_INFO) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			} else {
				mediaController.show(CONTROLLER_DELAY);
			}
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				mediaController.show(CONTROLLER_DELAY);
			} else {
				mediaPlayer.start();
				mediaController.hide();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	protected class WatchedVideoRequest extends AsyncTask<Void, Void, Void> {

		protected String scrobbleKey;

		public WatchedVideoRequest(String key) {
			scrobbleKey = key;
		}

		@Override
		protected Void doInBackground(Void... params) {
			PlexappFactory factory = SerenityApplication.getPlexFactory();
			boolean result = factory.setWatched(scrobbleKey);
			return null;
		}
	}
}
