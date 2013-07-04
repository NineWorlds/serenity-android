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

package us.nineworlds.serenity.ui.browser.music.tracks;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.analytics.tracking.android.EasyTracker;
import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class MusicTracksActivity extends Activity implements OnCompletionListener {

	private String key;
	private boolean restarted_state = false;
	private MediaPlayer mediaPlayer;
	private ImageButton playBtn;
	private ImageButton nextBtn;
	private ImageButton prevBtn;
	private SeekBar seekBar;
	private  TextView currentTime, durationTime;
	public static int currentPlayingItem = 0;

	private MediaController mediaController;
	
	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int MILLISECONDS_PER_HOUR = 3600000;
	
	private Handler progressHandler = new Handler();
	private Runnable playbackRunnable = new Runnable() {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					
					currentTime.setText(formatDuration(mediaPlayer.getCurrentPosition()));
					durationTime.setText(formatDuration(mediaPlayer.getDuration()));
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
		setContentView(R.layout.activity_music_track);
		init();
	}
	
	protected void init() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		playBtn = (ImageButton) findViewById(R.id.audioPause);
		playBtn.setOnClickListener(new PlayButtonListener(mediaPlayer));
		seekBar = (SeekBar) findViewById(R.id.mediacontroller_seekbar);
		nextBtn = (ImageButton) findViewById(R.id.audioSkipForward);
		nextBtn.setOnClickListener(new SkipForwardOnClickListener(mediaPlayer));
		prevBtn = (ImageButton) findViewById(R.id.audioSkipBack);
		prevBtn.setOnClickListener(new SkipBackOnClickListener(mediaPlayer));
		currentTime = (TextView)findViewById(R.id.mediacontroller_time_current);
		durationTime = (TextView)findViewById(R.id.mediacontroller_time_total);
		
		progressHandler.postDelayed(playbackRunnable, 1000);
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		seekBar.setOnSeekBarChangeListener(new AudioTrackPlaybackListener(mediaPlayer, am, currentTime, durationTime, seekBar));
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
		lview.setOnItemClickListener(new AudioTrackItemClickListener(mediaPlayer));
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();
		
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
		progressHandler.removeCallbacks(playbackRunnable);
	}

	/* (non-Javadoc)
	 * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer)
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		ListView lview = (ListView) findViewById(R.id.audioTracksListview);
		int  count = lview.getCount();
		int nextItem = 0;
		if (currentPlayingItem < count) {
			nextItem = currentPlayingItem + 1;
		} else {
			return;
		}
		
		if (nextItem >= count) {
			return;
		}
		lview.setSelection(nextItem);
		AudioTrackContentInfo track = (AudioTrackContentInfo) lview.getItemAtPosition(nextItem);
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
}
