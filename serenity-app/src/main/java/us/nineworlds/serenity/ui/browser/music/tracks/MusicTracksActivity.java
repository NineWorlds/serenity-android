/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package us.nineworlds.serenity.ui.browser.music.tracks;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.analytics.tracking.android.EasyTracker;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import us.nineworlds.serenity.ui.util.DisplayUtils;

/**
 * @author dcarver
 * 
 */
public class MusicTracksActivity extends Activity implements
		OnCompletionListener {

	private String key;
	private boolean restarted_state = false;
	private MediaPlayer mediaPlayer;
	private ImageButton playBtn, nextBtn, prevBtn, nextTrack, prevTrack;
	private SeekBar seekBar;
	private TextView currentTime, durationTime, playingTrack;
	private CheckBox randomPlay;
	public static int currentPlayingItem = 0;

	private MediaController mediaController;

	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int MILLISECONDS_PER_HOUR = 3600000;

	private Handler progressHandler = new Handler();
	private Runnable playbackRunnable = new Runnable() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {

					currentTime.setText(formatDuration(mediaPlayer
							.getCurrentPosition()));
					durationTime.setText(formatDuration(mediaPlayer
							.getDuration()));
					setProgress();
				}
			} catch (IllegalStateException ex) {

			}
			progressHandler.postDelayed(this, 1000);
		}

		protected String formatDuration(long duration) {
			long tempdur = duration;
			long hours = TimeUnit.MILLISECONDS.toHours(duration);

			tempdur = tempdur - (hours * MILLISECONDS_PER_HOUR);

			long minutes = tempdur / MILLISECONDS_PER_MINUTE;
			tempdur = tempdur - (minutes * MILLISECONDS_PER_MINUTE);

			long seconds = tempdur / 1000;

			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}

		private void setProgress() {
			if (mediaPlayer == null) {
				return;
			}

			long position = 0;

			try {
				position = mediaPlayer.getCurrentPosition();
				long duration = mediaPlayer.getDuration();
				if (seekBar != null) {
					if (duration > 0) {
						long pos = 1000L * position / duration;
						seekBar.setProgress((int) pos);
					}
					int percent = 0;
					seekBar.setSecondaryProgress(percent * 10);
				}
			} catch (IllegalStateException ex) {
				Log.i(getClass().getName(),
						"Player has been either released or in an error state.");
			}
		}
	};
	
	private PhoneStateListener phoneStateListener = new PhoneStateListener() {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	        if (state == TelephonyManager.CALL_STATE_RINGING ||
	        	state == TelephonyManager.CALL_STATE_OFFHOOK) {
	            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
	            	mediaPlayer.pause();
	            }
	        } 
	        super.onCallStateChanged(state, incomingNumber);
	    }
	};
	
	private TelephonyManager mgr;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		if (SerenityApplication.isRunningOnOUYA()) {
			setContentView(R.layout.activity_music_ouya_track);
		} else {
			setContentView(R.layout.activity_music_track);
		}

		DisplayUtils.overscanCompensation(this, findViewById(R.id.musicBrowserBackgroundLayout));

		init();
	}

	protected void init() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		
		initializeControls();

		progressHandler.postDelayed(playbackRunnable, 1000);
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		seekBar.setOnSeekBarChangeListener(new AudioTrackPlaybackListener(
				mediaPlayer, am, currentTime, durationTime, seekBar));
		
		mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if(mgr != null) {
		    mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	/**
	 * 
	 */
	protected void initializeControls() {
		playBtn = (ImageButton) findViewById(R.id.audioPause);
		playBtn.setOnClickListener(new PlayButtonListener(mediaPlayer));
		playBtn.setVisibility(View.INVISIBLE);
		seekBar = (SeekBar) findViewById(R.id.mediacontroller_seekbar);
		seekBar.setVisibility(View.INVISIBLE);
		nextBtn = (ImageButton) findViewById(R.id.audioSkipForward);
		nextBtn.setVisibility(View.INVISIBLE);
		nextBtn.setOnClickListener(new SkipForwardOnClickListener(mediaPlayer));
		prevBtn = (ImageButton) findViewById(R.id.audioSkipBack);
		prevBtn.setOnClickListener(new SkipBackOnClickListener(mediaPlayer));
		prevBtn.setVisibility(View.INVISIBLE);
		nextTrack = (ImageButton) findViewById(R.id.audioNextTrack);
		nextTrack.setOnClickListener(new NextTrackOnClickListener());
		nextTrack.setVisibility(View.INVISIBLE);
		prevTrack = (ImageButton) findViewById(R.id.audioPrevTrack);
		prevTrack.setOnClickListener(new PrevTrackOnClickListener());
		prevTrack.setVisibility(View.INVISIBLE);
		currentTime = (TextView) findViewById(R.id.mediacontroller_time_current);
		currentTime.setVisibility(View.INVISIBLE);
		durationTime = (TextView) findViewById(R.id.mediacontroller_time_total);
		durationTime.setVisibility(View.INVISIBLE);
		playingTrack = (TextView) findViewById(R.id.track_playing);
		playingTrack.setVisibility(View.INVISIBLE);
		randomPlay = (CheckBox) findViewById(R.id.audioRandomPlay);
		if (randomPlay != null) {
			randomPlay.setVisibility(View.INVISIBLE);
		}
		
		LinearLayout mediaProgressControl = (LinearLayout) findViewById(R.id.mediacontroller_progress_layout);
		mediaProgressControl.setVisibility(View.INVISIBLE);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			setupMusicAdapters();
		}
		restarted_state = false;
	}

	protected void setupMusicAdapters() {
		ListView lview = (ListView) findViewById(R.id.audioTracksListview);
		lview.setAdapter(new TracksAdapter(this, key));
		lview.setOnItemSelectedListener(new TracksOnItemSelectedListener());
		lview.setOnItemClickListener(new AudioTrackItemClickListener(
				mediaPlayer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();

		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
		progressHandler.removeCallbacks(playbackRunnable);
		if (mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (mediaPlayer != null) {
			try {
				if (keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
						|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
						|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
						|| keyCode == KeyEvent.KEYCODE_P
						|| keyCode == KeyEvent.KEYCODE_SPACE) {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					} else {
						mediaPlayer.start();
					}
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD ||
					keyCode == KeyEvent.KEYCODE_F) {
					nextBtn.performClick();
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND ||
					keyCode == KeyEvent.KEYCODE_R) {
					prevBtn.performClick();
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
					nextTrack.performClick();
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
					prevTrack.performClick();
					return true;
				}
			} catch (IllegalStateException ex) {
				Toast.makeText(this, "Media Player in illegal state.",
						Toast.LENGTH_LONG).show();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		ListView lview = (ListView) findViewById(R.id.audioTracksListview);
		int count = lview.getCount();
		int nextItem = 0;
		
		if (randomPlay != null && randomPlay.isChecked()) {
			nextItem = randomTrack(count);
		} else if (currentPlayingItem < count) {
			nextItem = currentPlayingItem + 1;
		} else {
			return;
		}

		if (nextItem >= count) {
			return;
		}
		playNextItem(lview, nextItem);
	}

	/**
	 * @param lview
	 * @param nextItem
	 */
	protected void playNextItem(ListView lview, int nextItem) {
		lview.setSelection(nextItem);
		AudioTrackContentInfo track = (AudioTrackContentInfo) lview
				.getItemAtPosition(nextItem);
		playingTrack.setText(track.getTitle());
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(track.getDirectPlayUrl());
			mediaPlayer.prepare();
			mediaPlayer.start();
			currentPlayingItem = nextItem;
		} catch (IllegalStateException ex) {

		} catch (IOException ex) {

		}
	}

	/**
	 * @param count
	 * @return
	 */
	protected int randomTrack(int count) {
		int nextItem;
		Random random = new Random(Calendar.getInstance().getTimeInMillis());
		nextItem = Math.abs(random.nextInt(count)) -1;
		if (nextItem < 0) {
			nextItem = 0;
		}
		return nextItem;
	}
	
	private class NextTrackOnClickListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
			ListView lview = (ListView) findViewById(R.id.audioTracksListview);
			int nextItem = 0;
			int count = lview.getAdapter().getCount();
			if (currentPlayingItem < count) {
				nextItem = currentPlayingItem + 1;				
			}
			
			if (nextItem >= count) {
				nextItem = 0;
			}
			
			playNextItem(lview, nextItem);
		}
	}
	
	private class PrevTrackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ListView lview = (ListView) findViewById(R.id.audioTracksListview);
			int nextItem = 0;
			int count = lview.getAdapter().getCount();
			if (currentPlayingItem > 0) {
				nextItem = currentPlayingItem - 1;				
			} else {
				nextItem = count - 1;
			}
						
			playNextItem(lview, nextItem);
		}
	}
	
}
