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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;

import org.mozilla.universalchardet.UniversalDetector;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.core.services.CompletedVideoRequest;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.core.subtitles.formats.Caption;
import us.nineworlds.serenity.core.subtitles.formats.FormatASS;
import us.nineworlds.serenity.core.subtitles.formats.FormatSRT;
import us.nineworlds.serenity.core.subtitles.formats.TimedTextObject;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

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
	static final int PROGRESS_UPDATE_DELAY = 5000;
	static final int SUBTITLE_DISPLAY_CHECK = 100;
	int playbackPos = 0;

	static final String TAG = "SerenitySurfaceViewVideoActivity";
	static final int CONTROLLER_DELAY = 16000; // Sixteen seconds

	private MediaPlayer mediaPlayer;
	private String videoURL;
	private SurfaceView surfaceView;
	private View videoActivityView;
	private MediaController mediaController;
	private View timeOfDayView;
	private String aspectRatio;
	private VideoContentInfo video;
	private String videoId;
	private int resumeOffset;
	private final boolean mediaplayer_error_state = false;
	private boolean mediaplayer_released = false;
	private String subtitleURL;
	private String subtitleType;
	private String mediaTagIdentifier;
	private TimedTextObject subtitleTimedText;
	private boolean subtitlesPlaybackEnabled = true;
	private String subtitleInputEncoding = null;
	private boolean autoResume;

	private final Handler subtitleDisplayHandler = new Handler();
	private final Runnable subtitle = new Runnable() {
		@Override
		public void run() {
			if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
				if (hasSubtitles()) {
					int currentPos = mediaPlayer.getCurrentPosition();
					Collection<Caption> subtitles = subtitleTimedText.captions
							.values();
					for (Caption caption : subtitles) {
						if (currentPos >= caption.start.getMilliseconds()
								&& currentPos <= caption.end.getMilliseconds()) {
							onTimedText(caption);
							break;
						} else if (currentPos > caption.end.getMilliseconds()) {
							onTimedText(null);
						}
					}
				} else {
					subtitlesPlaybackEnabled = false;
					Toast.makeText(
							getApplicationContext(),
							"Invalid or Missing Subtitle. Subtitle playback disabled.",
							Toast.LENGTH_LONG).show();
				}
			}
			if (subtitlesPlaybackEnabled) {
				subtitleDisplayHandler
						.postDelayed(this, SUBTITLE_DISPLAY_CHECK);
			}

		}

		/**
		 * @return
		 */
		protected boolean hasSubtitles() {
			return subtitleTimedText != null
					&& subtitleTimedText.captions != null;
		};
	};

	private final Handler progressReportinghandler = new Handler();
	private final Runnable progressRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
					float percentage = Float.valueOf(mediaPlayer
							.getCurrentPosition())
							/ Float.valueOf(mediaPlayer.getDuration());
					playbackPos = mediaPlayer.getCurrentPosition();
					if (percentage <= 90.f) {
						new UpdateProgressRequest().execute();
						progressReportinghandler.postDelayed(this,
								PROGRESS_UPDATE_DELAY); // Update progress every
														// 5
														// seconds
					} else {
						new WatchedVideoAsyncTask().execute(videoId);
					}
				}
			} catch (IllegalStateException ex) {
				Log.w(getClass().getName(),
						"Illegalstate exception occurred durring progress update. No further updates will occur.",
						ex);
			}
		};
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setDataSource(videoURL);
			mediaPlayer.setOnPreparedListener(new VideoPlayerPrepareListener(
					this, mediaPlayer, mediaController, surfaceView,
					resumeOffset, autoResume, aspectRatio,
					progressReportinghandler, progressRunnable));
			mediaPlayer
					.setOnCompletionListener(new VideoPlayerOnCompletionListener());
			mediaPlayer.prepareAsync();

		} catch (Exception ex) {
			Log.e(TAG, "Video Playback Error. ", ex);
		}

	}

	@Override
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
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnErrorListener(new SerenityOnErrorListener());
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		videoActivityView = findViewById(R.id.video_playeback);
		timeOfDayView = findViewById(R.id.time_of_day);

		DisplayUtils.overscanCompensation(this, videoActivityView);

		surfaceView.setKeepScreenOn(true);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setSizeFromLayout();

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final boolean showTimeOfDay = prefs.getBoolean("showTimeOfDay", false);
		timeOfDayView.setVisibility(showTimeOfDay ? View.VISIBLE : View.GONE);

		retrieveIntentExtras();
	}

	protected void retrieveIntentExtras() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			autoResume = extras.getBoolean("autoResume", false);
			extras.remove("autoResume");
		}

		if (extras == null || extras.isEmpty()) {
			playBackFromVideoQueue();
		} else {
			playbackFromIntent(extras);
		}

		new SubtitleAsyncTask().execute();
	}

	private void playbackFromIntent(Bundle extras) {
		videoURL = extras.getString("videoUrl");
		if (videoURL == null) {
			videoURL = extras.getString("encodedvideoUrl");
			if (videoURL != null) {
				videoURL = URLDecoder.decode(videoURL);
			}
		}
		video = null;
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
		subtitleURL = extras.getString("subtitleURL");
		subtitleType = extras.getString("subtitleFormat");
		mediaTagIdentifier = extras.getString("mediaTagId");
		initMediaController(summary, title, posterURL, videoFormat,
				videoResolution, audioFormat, audioChannels);
	}

	private void playBackFromVideoQueue() {
		LinkedList<VideoContentInfo> queue = SerenityApplication
				.getVideoPlaybackQueue();
		if (queue.isEmpty()) {
			return;
		}
		VideoContentInfo video = queue.poll();
		videoURL = video.getDirectPlayUrl();
		this.video = video;
		videoId = video.id();
		String summary = video.getSummary();
		String title = video.getTitle();
		String posterURL = video.getImageURL();
		;
		if (video instanceof EpisodePosterInfo) {
			if (video.getParentPosterURL() != null) {
				posterURL = video.getParentPosterURL();
			}
		}
		aspectRatio = video.getAspectRatio();
		String videoFormat = video.getVideoCodec();
		String videoResolution = video.getVideoResolution();
		String audioFormat = video.getAudioCodec();
		String audioChannels = video.getAudioChannels();
		resumeOffset = video.getResumeOffset();
		if (video.getSubtitle() != null
				&& !"none".equals(video.getSubtitle().getFormat())) {
			subtitleURL = video.getSubtitle().getKey();
			subtitleType = video.getSubtitle().getFormat();
		}
		mediaTagIdentifier = video.getMediaTagIdentifier();
		initMediaController(summary, title, posterURL, videoFormat,
				videoResolution, audioFormat, audioChannels);
	}

	/**
	 * @param summary
	 * @param title
	 * @param posterURL
	 * @param videoFormat
	 * @param videoResolution
	 * @param audioFormat
	 * @param audioChannels
	 */
	protected void initMediaController(String summary, String title,
			String posterURL, String videoFormat, String videoResolution,
			String audioFormat, String audioChannels) {

		mediaController = new MediaController(this, summary, title, posterURL,
				videoResolution, videoFormat, audioFormat, audioChannels,
				mediaTagIdentifier);
		mediaController.setAnchorView(videoActivityView);
		mediaController.setMediaPlayer(new SerenityMediaPlayerControl(
				mediaPlayer));
	}

	@Override
	public void finish() {
		subtitleDisplayHandler.removeCallbacks(subtitle);
		progressReportinghandler.removeCallbacks(progressRunnable);
		super.finish();
	}

	protected void setExitResultCode() {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("position", playbackPos);
		if (getParent() == null) {
			setResult(SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY, returnIntent);
		} else {
			getParent().setResult(SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY,
					returnIntent);
		}
	}

	protected void setExitResultCodeFinished() {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("position", playbackPos);
		if (getParent() == null) {
			setResult(Activity.RESULT_OK, returnIntent);
		} else {
			getParent().setResult(Activity.RESULT_OK, returnIntent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		if (isKeyCodeBack(keyCode)) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			}
			if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			setExitResultCode();
			finish();
			return true;
		}

		if (isKeyCodeInfo(keyCode)) {
			if (isMediaPlayerStateValid()) {
				if (mediaController.isShowing()) {
					mediaController.hide();
				} else {
					mediaController.show(CONTROLLER_DELAY);
				}
			}
			return true;
		}

		if (isKeyCodePauseResume(keyCode)) {
			if (isMediaPlayerStateValid()) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					mediaController.show(CONTROLLER_DELAY);
					progressReportinghandler.removeCallbacks(progressRunnable);
				} else {
					mediaPlayer.start();
					mediaController.hide();
					progressReportinghandler
							.postDelayed(progressRunnable, 5000);
				}
				return true;
			}
		}

		if (isKeyCodePlay(keyCode)) {
			if (isMediaPlayerStateValid()) {
				if (mediaPlayer.isPlaying()) {
					if (mediaController.isShowing()) {
						mediaController.hide();
					} else {
						mediaController.show(CONTROLLER_DELAY);
					}
				} else {
					mediaPlayer.start();
					mediaController.hide();
					progressReportinghandler
							.postDelayed(progressRunnable, 5000);
				}
				return true;
			}
		}

		final String nextPrevBehavior = prefs.getString("next_prev_behavior",
				"queue");

		if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
			if (nextPrevBehavior.equals("queue")) {
				mediaController.hide();
				if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				finish();
				return true;
			}
			final int skipTo;
			if (isMediaPlayerStateValid()) {
				final int currentPosition = mediaPlayer.getCurrentPosition();
				final int duration = mediaPlayer.getDuration();

				if (nextPrevBehavior.endsWith("%")) {
					final Integer percent = Integer.valueOf(nextPrevBehavior
							.substring(0, nextPrevBehavior.length() - 1));
					skipTo = currentPosition + duration * percent / 100;
				} else {
					skipTo = currentPosition
							+ Integer.valueOf(nextPrevBehavior);
				}
				skipToOffset(skipTo);
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_T) {
			final boolean showTimeOfDay = !prefs.getBoolean("showTimeOfDay",
					false);
			timeOfDayView.setVisibility(showTimeOfDay ? View.VISIBLE
					: View.GONE);
			prefs.edit().putBoolean("showTimeOfDay", showTimeOfDay).apply();
			return true;
		}

		// All actions from here on require media to be in a valid state
		if (!isMediaPlayerStateValid()) {
			return super.onKeyDown(keyCode, event);
		}

		try {
			final int currentPosition = mediaPlayer.getCurrentPosition();
			final int duration = mediaPlayer.getDuration();
			if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
					&& !nextPrevBehavior.equals("queue")) {
				final int skipTo;
				if (nextPrevBehavior.endsWith("%")) {
					final Integer percent = Integer.valueOf(nextPrevBehavior
							.substring(0, nextPrevBehavior.length() - 1));
					skipTo = currentPosition - duration * percent / 100;
				} else {
					skipTo = currentPosition
							- Integer.valueOf(nextPrevBehavior);
				}
				skipToOffset(skipTo);
				return true;
			}

			if (isKeyCodeSkipForward(keyCode)) {
				skipToOffset(currentPosition
						+ Integer.valueOf(prefs.getString("skip_forward_time",
								"30000")));
				return true;
			}

			if (isKeyCodeSkipBack(keyCode)) {
				skipToOffset(currentPosition
						- Integer.valueOf(prefs.getString("skip_backward_time",
								"10000")));
				return true;
			}

			if (isKeyCodeStop(keyCode)) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					if (!mediaController.isShowing()) {
						mediaController.show(CONTROLLER_DELAY);
					}
				}
				return true;
			}

			if (isSkipByPercentage(keyCode)) {
				return true;
			}
		} catch (IllegalStateException e) {
			Log.e(this.getClass().getName(),
					"Media Player is in an illegalstate.", e);
		}

		return super.onKeyDown(keyCode, event);
	}

	private void skipToOffset(int skipOffset) {
		int duration = mediaPlayer.getDuration();
		if (skipOffset > duration) {
			skipOffset = duration - 1;
		} else if (skipOffset < 0) {
			skipOffset = 0;
		}
		if (!mediaController.isShowing()) {
			mediaController.show(CONTROLLER_DELAY);
		}
		mediaPlayer.seekTo(skipOffset);
	}

	protected boolean isSkipByPercentage(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_1) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.10f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_2) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.20f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_3) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.30f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_4) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.40f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_5) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.50f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_6) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.60f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_8) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.80f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_9) {
			int duration = mediaPlayer.getDuration();
			int newPos = Math.round(duration * 0.90f);
			skipToPercentage(newPos);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_0) {
			skipToPercentage(0);
			return true;
		}
		return false;
	}

	/**
	 * @param newPos
	 */
	protected void skipToPercentage(int newPos) {
		mediaPlayer.seekTo(newPos);
		if (!mediaController.isShowing()) {
			mediaController.show(CONTROLLER_DELAY);
		}
	}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean isKeyCodeStop(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_MEDIA_STOP
				|| keyCode == KeyEvent.KEYCODE_S;
	}

	/**
	 * @param keyCode
	 * @return
	 */
	@Override
	protected boolean isKeyCodeSkipBack(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
				|| keyCode == KeyEvent.KEYCODE_R
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
				|| keyCode == KeyEvent.KEYCODE_BUTTON_L1
				|| keyCode == KeyEvent.KEYCODE_BUTTON_L2;
	}

	/**
	 * @param keyCode
	 * @return
	 */
	@Override
	protected boolean isKeyCodeSkipForward(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
				|| keyCode == KeyEvent.KEYCODE_F
				|| keyCode == KeyEvent.KEYCODE_BUTTON_R1
				|| keyCode == KeyEvent.KEYCODE_BUTTON_R2;
	}

	/**
	 * @param keyCode
	 * @return
	 */
	protected boolean isKeyCodePauseResume(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
				|| keyCode == KeyEvent.KEYCODE_P
				|| keyCode == KeyEvent.KEYCODE_SPACE
				|| keyCode == KeyEvent.KEYCODE_BUTTON_A;
	}

	protected boolean isKeyCodePlay(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
				|| keyCode == KeyEvent.KEYCODE_BUTTON_A;
	}

	protected boolean isKeyCodeInfo(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_INFO
				|| keyCode == KeyEvent.KEYCODE_I
				|| keyCode == KeyEvent.KEYCODE_BUTTON_Y;
	}

	protected boolean isKeyCodeBack(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_ESCAPE
				|| keyCode == KeyEvent.KEYCODE_BUTTON_B;
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
				if (video != null) {
					if (video.isWatched()) {
						factory.setWatched(videoId);
						factory.setProgress(videoId, "0");
					} else {
						factory.setProgress(videoId, offset);
					}
					video.setResumeOffset(Integer.valueOf(offset));
				} else {
					factory.setProgress(videoId, offset);
				}
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see us.nineworlds.serenity.ui.activity.SerenityActivity#createSideMenu()
	 */
	@Override
	protected void createSideMenu() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			} else {
				mediaController.show();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

	protected class VideoPlayerOnCompletionListener implements
			OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			new CompletedVideoRequest(videoId).execute();
			if (!mediaplayer_released) {
				if (isMediaPlayerStateValid()) {
					if (mediaController.isShowing()) {
						mediaController.hide();
					}
				}
				mp.release();
				mediaplayer_released = true;
			}
			setExitResultCodeFinished();
			finish();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.media.MediaPlayer.OnTimedTextListener#onTimedText(android.media
	 * .MediaPlayer, android.media.TimedText)
	 */
	public void onTimedText(Caption text) {
		TextView subtitles = (TextView) findViewById(R.id.txtSubtitles);
		if (text == null) {
			subtitles.setVisibility(View.INVISIBLE);
			return;
		}
		String subtitleText = convertCharSet(text.content);
		subtitles.setText(Html.fromHtml(subtitleText));
		subtitles.setVisibility(View.VISIBLE);
	}

	private String convertCharSet(String textToConvert) {
		String outputEncoding = "UTF-8";
		if (outputEncoding.equalsIgnoreCase(subtitleInputEncoding)) {
			return textToConvert;
		}
		Charset charsetOutput = Charset.forName(outputEncoding);
		Charset charsetInput = Charset.forName(subtitleInputEncoding);
		CharBuffer inputEncoded = charsetInput.decode(ByteBuffer
				.wrap(textToConvert.getBytes(Charset
						.forName(subtitleInputEncoding))));
		byte[] utfEncoded = charsetOutput.encode(inputEncoded).array();
		return new String(utfEncoded, Charset.forName("UTF-8"));
	}

	public class SubtitleAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (subtitleURL != null) {
				try {
					URL url = new URL(subtitleURL);
					getInputEncoding(url);
					if ("srt".equals(subtitleType)) {
						FormatSRT formatSRT = new FormatSRT();
						subtitleTimedText = formatSRT.parseFile(url
								.openStream());
					} else if ("ass".equals(subtitleType)) {
						FormatASS formatASS = new FormatASS();
						subtitleTimedText = formatASS.parseFile(url
								.openStream());
					}
					subtitleDisplayHandler.post(subtitle);
				} catch (Exception e) {
					Log.e(getClass().getName(), e.getMessage(), e);
				}
			}
			return null;
		}

		private void getInputEncoding(URL url) {
			InputStream is = null;
			try {
				byte[] buf = new byte[4096];
				is = url.openStream();
				UniversalDetector detector = new UniversalDetector(null);

				int nread;
				while ((nread = is.read(buf)) > 0 && !detector.isDone()) {
					detector.handleData(buf, 0, nread);
				}
				detector.dataEnd();

				subtitleInputEncoding = detector.getDetectedCharset();
				if (subtitleInputEncoding != null) {
					Log.d(getClass().getName(), "Detected encoding = "
							+ subtitleInputEncoding);
				}
				detector.reset();
			} catch (IOException ex) {

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}

		}

	}
}
