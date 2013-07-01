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

import com.google.analytics.tracking.android.EasyTracker;
import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;

/**
 * @author dcarver
 * 
 */
public class MusicTracksActivity extends Activity {

	private String key;
	private boolean restarted_state = false;
	private MediaPlayer mediaPlayer;
	private ImageButton playBtn;
	private ImageButton nextBtn;
	private ImageButton prevBtn;
	private SeekBar seekBar;
	private MediaController mediaController;

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
		playBtn = (ImageButton) findViewById(R.id.audioPause);
		playBtn.setOnClickListener(new PlayButtonListener(mediaPlayer));
		seekBar = (SeekBar) findViewById(R.id.mediacontroller_seekbar);
		nextBtn = (ImageButton) findViewById(R.id.audioSkipForward);
		prevBtn = (ImageButton) findViewById(R.id.audioSkipBack);
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
	}
}
