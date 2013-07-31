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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import us.nineworlds.serenity.core.util.TimeUtil;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class AudioTrackPlaybackListener implements OnSeekBarChangeListener {
	private boolean dragging = false;
	private boolean instantSeeking = true;
	private AudioManager audioManager;
	private long duration;
	private SeekBar progressBar;
	private MediaPlayer mediaPlayer;
	private TextView currentTimeView, endTimeView;

	
	public AudioTrackPlaybackListener(MediaPlayer mp, AudioManager am, TextView ctv, TextView etv, SeekBar progress) {
		mediaPlayer = mp;
		audioManager = am;
		currentTimeView = ctv;
		endTimeView = ctv;
		progressBar = progress;
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar bar) {
		dragging = true;
		if (mediaPlayer != null) {
			duration = mediaPlayer.getDuration();
		}
		if (instantSeeking) {
			audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
		}
	}

	@Override
	public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
		if (!fromuser) {
			return;
		}

		setProgress();

		long newposition = ((duration * progress) / 1000);
		String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
				.format(new Date(Long.valueOf(newposition)));
		if (instantSeeking) {
			if (newposition >= Integer.MIN_VALUE && newposition <= Integer.MAX_VALUE) {
				mediaPlayer.seekTo((int) newposition);
			}
		}
		if (currentTimeView != null)
			currentTimeView.setText(time);
	}
	
	private long setProgress() {
		if (mediaPlayer == null || dragging) {
			return 0;
		}

		long position = 0;

		try {
			position = mediaPlayer.getCurrentPosition();
			long duration = mediaPlayer.getDuration();
			if (progressBar != null) {
				if (duration > 0) {
					long pos = 1000L * position / duration;
					progressBar.setProgress((int) pos);
				}
				int percent = 0;
				progressBar.setSecondaryProgress(percent * 10);
			}

			this.duration = duration;

			if (endTimeView != null) {
				endTimeView.setText(TimeUtil.formatDuration(duration));
			}

			if (currentTimeView != null) {
				currentTimeView.setText(TimeUtil.formatDuration(position));
			}
		} catch (IllegalStateException ex) {
			Log.i(getClass().getName(),
					"Player has been either released or in an error state.");
		}

		return position;
	}	

	@Override
	public void onStopTrackingTouch(SeekBar bar) {
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
	}

}
