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

import java.net.URLDecoder;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.video.player.MediaController.MediaPlayerControl;

import us.nineworlds.serenity.R;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

/**
 * A view that handles the internal video playback and representation of a movie
 * or tv show.
 * 
 * @author dcarver
 * 
 */
public class SerenitySurfaceViewVideoActivity extends SerenityActivity
		implements SurfaceHolder.Callback {

	/**
	 * 
	 */
	private static final int PROGRESS_UPDATE_DELAY = 5000;
	static final String TAG = "SerenitySurfaceViewVideoActivity";
	static final int CONTROLLER_DELAY = 16000; // Sixteen seconds
	private MediaPlayer mediaPlayer;
	private String videoURL;
	private SurfaceView surfaceView;
	private MediaController mediaController;
	private String aspectRatio;
	private String videoId;
	private int resumeOffset;
	private boolean mediaplayer_error_state = false;
	private boolean mediaplayer_released = false;

	private Handler progressReportinghandler = new Handler();
	private Runnable progressRunnable = new Runnable() {

		public void run() {
			try {
				if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
					new UpdateProgressRequest().execute();
					progressReportinghandler.postDelayed(this,
							PROGRESS_UPDATE_DELAY); // Update progress every 5
													// seconds
				}
			} catch (IllegalStateException ex) {
				Log.w(getClass().getName(),
						"Illegalstate exception occurred durring progress update. No further updates will occur.",
						ex);
			}
		};
	};

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setDataSource(videoURL);
			mediaPlayer.setOnPreparedListener(new VideoPlayerPrepareListener(
					this));
			mediaPlayer.setOnCompletionListener(new VideoPlayerOnCompletionListener());
			mediaPlayer.prepareAsync();

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

		if (videoHeight == 480 && videoWidth == 720) {
			aspectRatio = (float) 1.78;
		}

		if (preferPlexAspectRatio && this.aspectRatio != null) {
			aspectRatio = Float.parseFloat(this.aspectRatio);
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

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (!mediaplayer_released) {
			mediaPlayer.release();
			mediaplayer_released = true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_playback);
		init();
	}

	/**
	 * Initialize the mediaplayer and mediacontroller.
	 */
	protected void init() {
		Bundle extras = getIntent().getExtras();

		videoURL = extras.getString("videoUrl");
		if (videoURL == null) {
			videoURL = extras.getString("encodedvideoUrl");
			if (videoURL != null) {
				videoURL = URLDecoder.decode(videoURL);
			}
		}
		videoId = extras.getString("id");
		String summary = extras.getString("summary");
		String title = extras.getString("title");
		String posterURL = extras.getString("posterUrl");
		aspectRatio = extras.getString("aspectRatio");
		String videoFormat = extras.getString("videoFormat");
		String videoResolution = extras.getString("videoResolution");
		String audioFormat = extras.getString("audioFormat");
		String audioChannels = extras.getString("audioChannels");
		resumeOffset = extras.getInt("resumeOffset");

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnErrorListener(new SerenityOnErrorListener());
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceView.setKeepScreenOn(true);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setSizeFromLayout();

		mediaController = new MediaController(this, summary, title, posterURL,
				videoResolution, videoFormat, audioFormat, audioChannels);
		mediaController.setAnchorView(surfaceView);
		mediaController.setMediaPlayer(new SerenityMediaPlayerControl());
	}

	@Override
	public void finish() {
		super.finish();
		progressReportinghandler.removeCallbacks(progressRunnable);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mediaController.isShowing()) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				mediaController.hide();

				if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				return super.onKeyDown(keyCode, event);
			}
		} else {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				return super.onKeyDown(keyCode, event);
			}
		}

		if (keyCode == KeyEvent.KEYCODE_INFO) {
			if (isMediaPlayerStateValid()) {
				if (mediaController.isShowing()) {
					mediaController.hide();
				} else {
					mediaController.show(CONTROLLER_DELAY);
				}
			}
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
			if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				mediaController.show(CONTROLLER_DELAY);
				progressReportinghandler.removeCallbacks(progressRunnable);
			} else {
				mediaPlayer.start();
				mediaController.hide();
				progressReportinghandler.postDelayed(progressRunnable, 5000);
			}
			return true;
		}

		if ((keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT)
				&& isMediaPlayerStateValid()) {
			int skipOffset = 10000 + mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			if (skipOffset > duration) {
				skipOffset = duration - 1;
			}
			if (!mediaController.isShowing()) {
				mediaController.show(CONTROLLER_DELAY);
			}
			mediaPlayer.seekTo(skipOffset);
			return true;
		}

		if ((keyCode == KeyEvent.KEYCODE_MEDIA_REWIND || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS)
				&& isMediaPlayerStateValid()) {
			int skipOffset = mediaPlayer.getCurrentPosition() - 10000;
			if (skipOffset < 0) {
				skipOffset = 0;
			}
			if (!mediaController.isShowing()) {
				mediaController.show(CONTROLLER_DELAY);
			}
			mediaPlayer.seekTo(skipOffset);
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP && isMediaPlayerStateValid()) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				if (!mediaController.isShowing()) {
					mediaController.show(CONTROLLER_DELAY);
				}
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	protected boolean isMediaPlayerStateValid() {
		if (mediaPlayer != null && mediaplayer_error_state == false
				&& mediaplayer_released == false) {
			return true;
		}
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	/**
	 * A simple media player control. Handles the main events that can occur
	 * while using the player to play a video.
	 * 
	 * @author dcarver
	 * 
	 */
	protected class SerenityMediaPlayerControl implements MediaPlayerControl {
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
	}

	/**
	 * A task that updates the watched status of a video.
	 * 
	 * @author dcarver
	 * 
	 */
	protected class WatchedVideoRequest extends AsyncTask<Void, Void, Void> {

		protected String scrobbleKey;

		public WatchedVideoRequest(String key) {
			scrobbleKey = key;
		}

		@Override
		protected Void doInBackground(Void... params) {
			PlexappFactory factory = SerenityApplication.getPlexFactory();
			factory.setWatched(scrobbleKey);
			return null;
		}
	}

	/**
	 * A task that updates the progress position of a video while it is being
	 * played.
	 * 
	 * @author dcarver
	 * 
	 */
	protected class UpdateProgressRequest extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			PlexappFactory factory = SerenityApplication.getPlexFactory();
			if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
				String offset = Integer.valueOf(
						mediaPlayer.getCurrentPosition()).toString();
				factory.setProgress(videoId, offset);
			}
			return null;
		}
	}

	/**
	 * A prepare listener that handles how a video should start playing.
	 * 
	 * It checks to see if the video has been previously viewed and if so will
	 * present a dialog to allow resuming of a video from where it was
	 * previously last viewed. Otherwise it will start play back of the video.
	 * It also launches the Watched status update task and the progress update
	 * handler.
	 * 
	 * @author dcarver
	 * 
	 */
	protected class VideoPlayerPrepareListener implements OnPreparedListener {

		private Context context;

		public VideoPlayerPrepareListener(Context context) {
			this.context = context;
		}

		public void onPrepared(MediaPlayer mp) {
			android.view.ViewGroup.LayoutParams lp = setupAspectRatio();
			surfaceView.setLayoutParams(lp);
			mediaController.setEnabled(true);

			if (resumeOffset > 0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context, android.R.style.Theme_Holo_Dialog);

				alertDialogBuilder.setTitle("Resume Video");
				alertDialogBuilder
						.setMessage("Resume the video or start from beginning?")
						.setCancelable(false)
						.setPositiveButton("Resume",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										if (!mediaPlayer.isPlaying()) {
											mediaPlayer.start();
										}
										mediaPlayer.seekTo(resumeOffset);
										setMetaData();
									}
								})
						.setNegativeButton("Restart",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										mediaPlayer.start();
										setMetaData();
									}
								});

				alertDialogBuilder.create();
				alertDialogBuilder.show();
				return;
			} else {
				mediaPlayer.start();
				setMetaData();
			}
		}

		/**
		 * 
		 */
		protected void setMetaData() {
			new WatchedVideoRequest(videoId).execute();
			mediaController.show(CONTROLLER_DELAY);
			if (progressReportinghandler != null) {
				progressReportinghandler.postDelayed(progressRunnable, 5000);
			}
		}

	}

	protected class SerenityOnErrorListener implements OnErrorListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.media.MediaPlayer.OnErrorListener#onError(android.media.
		 * MediaPlayer, int, int)
		 */
		public boolean onError(MediaPlayer mp, int what, int extra) {

			mediaplayer_error_state = true;

			String error_msg = "What: " + what + "Extra: " + extra;
			if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
				error_msg = "Unknown Media Player Error. Extra Code: " + extra;
			}

			if (what == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
				error_msg = "Media not valid for progessive playback. Extra Code: "
						+ extra;
			}

			if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
				error_msg = "Server croaked. Extra Code: " + extra;
			}

			EasyTracker.getTracker().sendEvent("Video Error", "Playback",
					error_msg, (long) 0);

			Log.e(getClass().getName(), error_msg);
			return true;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			} else {
				mediaController.show();
			}
		}
		return super.onTouchEvent(event);
	}
	
	protected class VideoPlayerOnCompletionListener implements OnCompletionListener {

		public void onCompletion(MediaPlayer mp) {
			if (!mediaplayer_released) {
				mp.release();
				mediaplayer_released = true;
			}
			finish();
		}
		
	}
}
