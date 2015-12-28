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

import javax.inject.Inject;

import org.mozilla.universalchardet.UniversalDetector;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.core.services.CompletedVideoRequest;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.core.subtitles.formats.Caption;
import us.nineworlds.serenity.core.subtitles.formats.FormatASS;
import us.nineworlds.serenity.core.subtitles.formats.FormatSRT;
import us.nineworlds.serenity.core.subtitles.formats.TimedTextObject;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A view that handles the internal video playback and representation of a movie
 * or tv show.
 *
 * @author dcarver
 *
 */
public class SerenitySurfaceViewVideoActivity extends SerenityActivity
		implements SurfaceHolder.Callback {

	@Inject
	@ForVideoQueue
	protected LinkedList<VideoContentInfo> videoQueue;

	@Inject
	protected PlexappFactory plexFactory;

	@Inject
	protected SharedPreferences prefs;

	@Inject
	protected MediaPlayer mediaPlayer;

	private final Handler subtitleDisplayHandler = new Handler();
	private final Runnable subtitle = new SubtitleRunnable();

	private final Handler progressReportinghandler = new Handler();
	private final Runnable progressRunnable = new ProgressRunnable();

	static final int PROGRESS_UPDATE_DELAY = 5000;
	static final int SUBTITLE_DISPLAY_CHECK = 100;
	int playbackPos = 0;

	static final String TAG = "SerenitySurfaceViewVideoActivity";
	private int osdDelayTime = 5000;
	private VideoPlayerKeyCodeHandler videoPlayerKeyCodeHandler;

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

	public boolean isMediaplayerReleased() {
		return mediaplayer_released;
	}

	public void setMediaplayerReleased(boolean mediaplayer_released) {
		this.mediaplayer_released = mediaplayer_released;
	}

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
					mediaController, surfaceView, resumeOffset, autoResume,
					aspectRatio, progressReportinghandler, progressRunnable));
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
		DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getSupportActionBar().hide();
		}
		init();

	}

	/**
	 * Initialize the mediaplayer and mediacontroller.
	 */
	protected void init() {
		osdDelayTime = Integer.parseInt(prefs.getString("osd_display_time",
				"5000"));

		mediaPlayer.setOnErrorListener(new SerenityOnErrorListener());
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		videoActivityView = findViewById(R.id.video_playeback);
		timeOfDayView = findViewById(R.id.time_of_day);

		surfaceView.setKeepScreenOn(true);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setSizeFromLayout();

		final boolean showTimeOfDay = prefs.getBoolean("showTimeOfDay", false);
		timeOfDayView.setVisibility(showTimeOfDay ? View.VISIBLE : View.GONE);

		retrieveIntentExtras();
		videoPlayerKeyCodeHandler = createVideoPlayerKeyCodeHandler();
	}

	protected VideoPlayerKeyCodeHandler createVideoPlayerKeyCodeHandler() {
		return new VideoPlayerKeyCodeHandler(mediaPlayer, mediaController,
				osdDelayTime, progressReportinghandler, progressRunnable,
				timeOfDayView, this);
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

	@Deprecated
	protected void playbackFromIntent(Bundle extras) {
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
		MediaControllerDataObject mediaMetaData = initMetaData(summary, title,
				posterURL, videoFormat, videoResolution, audioFormat,
				audioChannels);
		initMediaController(mediaMetaData);
	}

	protected void playBackFromVideoQueue() {
		if (videoQueue.isEmpty()) {
			return;
		}
		VideoContentInfo video = videoQueue.poll();
		videoURL = video.getDirectPlayUrl();
		this.video = video;
		videoId = video.id();
		String summary = video.getSummary();
		String title = video.getLongTitle();
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
		MediaControllerDataObject mediaMetaData = initMetaData(summary, title,
				posterURL, videoFormat, videoResolution, audioFormat,
				audioChannels);

		initMediaController(mediaMetaData);
	}

	protected void initMediaController(MediaControllerDataObject mediaMetaData) {
		mediaController = new MediaController(mediaMetaData);
		mediaController.setAnchorView(videoActivityView);
		mediaController.setMediaPlayer(new SerenityMediaPlayerControl(
				mediaPlayer));
		mediaController.setOSDDelayTime(osdDelayTime);

	}

	private MediaControllerDataObject initMetaData(String summary,
			String title, String posterURL, String videoFormat,
			String videoResolution, String audioFormat, String audioChannels) {
		MediaControllerDataObject mediaMetaData = new MediaControllerDataObject();
		mediaMetaData.setContext(this);
		mediaMetaData.setSummary(summary);
		mediaMetaData.setTitle(title);
		mediaMetaData.setPosterURL(posterURL);
		mediaMetaData.setResolution(videoResolution);
		mediaMetaData.setVideoFormat(videoFormat);
		mediaMetaData.setAudioFormat(audioFormat);
		mediaMetaData.setAudioChannels(audioChannels);
		return mediaMetaData;
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
	public void onBackPressed() {
		if (mediaController.isShowing()) {
			mediaController.hide();
		}
		if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		setExitResultCode();
		finish();
		super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}

		if (videoPlayerKeyCodeHandler.onKeyDown(keyCode, event,
				isMediaPlayerStateValid())) {
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
	protected void createSideMenu() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		visibleInBackground();
	}

	@Override
	public void onVisibleBehindCanceled() {
		super.onVisibleBehindCanceled();
		if (isMediaPlayerStateValid()) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
		}
	}

	protected void visibleInBackground() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			boolean success = requestVisibleBehind(true);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		visibleInBackground();
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

	protected class SubtitleRunnable implements Runnable {

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

		protected boolean hasSubtitles() {
			return subtitleTimedText != null
					&& subtitleTimedText.captions != null;
		}
	}

	protected class ProgressRunnable implements Runnable {

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
			if (isMediaPlayerStateValid() && mediaPlayer.isPlaying()) {
				String offset = Integer.valueOf(
						mediaPlayer.getCurrentPosition()).toString();
				if (video != null) {
					if (video.isWatched()) {
						plexFactory.setWatched(videoId);
						plexFactory.setProgress(videoId, "0");
					} else {
						plexFactory.setProgress(videoId, offset);
					}
					video.setResumeOffset(Integer.valueOf(offset));
				} else {
					plexFactory.setProgress(videoId, offset);
				}
			}
			return null;
		}
	}

	public void setVideoPlayerKeyCodeHandler(
			VideoPlayerKeyCodeHandler videoPlayerKeyCodeHandler) {
		this.videoPlayerKeyCodeHandler = videoPlayerKeyCodeHandler;
	}

	protected void setSerenityMediaController(MediaController mediaController) {
		this.mediaController = mediaController;
	}

	protected void setPlaybackPos(int playbackPos) {
		this.playbackPos = playbackPos;
	}

}
