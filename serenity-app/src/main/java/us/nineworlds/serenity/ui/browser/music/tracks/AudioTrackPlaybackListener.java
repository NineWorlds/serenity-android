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
import java.util.concurrent.TimeUnit;

import us.nineworlds.serenity.ui.video.player.MediaPlayerControl;

import android.app.Activity;
import android.media.AudioManager;
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
	private boolean instantSeeking = false;
	private AudioManager audioManager;
	private long duration;
	private SeekBar progressBar;
	private MediaPlayerControl mediaPlayer;
	private TextView currentTimeView, endTimeView;
	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int MILLISECONDS_PER_HOUR = 3600000;

	
	public AudioTrackPlaybackListener(MediaPlayerControl mp, AudioManager am, TextView ctv, TextView etv, SeekBar progress) {
		mediaPlayer = mp;
		audioManager = am;
		currentTimeView = ctv;
		endTimeView = ctv;
		progressBar = progress;
	}
	
	public void onStartTrackingTouch(SeekBar bar) {
		if (instantSeeking) {
			audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
		}
	}

	public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
		if (!fromuser) {
			return;
		}

		setProgress();

		long newposition = (duration * progress) / 1000;
		String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
				.format(new Date(newposition));
		if (instantSeeking) {
			mediaPlayer.seekTo(newposition);
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
				int percent = mediaPlayer.getBufferPercentage();
				progressBar.setSecondaryProgress(percent * 10);
			}

			this.duration = duration;

			if (endTimeView != null) {
				endTimeView.setText(formatDuration(duration));
			}

			if (currentTimeView != null) {
				currentTimeView.setText(formatDuration(position));
			}
		} catch (IllegalStateException ex) {
			Log.i(getClass().getName(),
					"Player has been either released or in an error state.");
		}

		return position;
	}
	
	/**
	 * Return a formated duration in hh:mm:ss format.
	 * 
	 * @param duration
	 *            number of milliseconds that have passed.
	 * @return formatted string
	 */
	protected String formatDuration(long duration) {
		long tempdur = duration;
		long hours = TimeUnit.MILLISECONDS.toHours(duration);

		tempdur = tempdur - (hours * MILLISECONDS_PER_HOUR);

		long minutes = tempdur / MILLISECONDS_PER_MINUTE;
		tempdur = tempdur - (minutes * MILLISECONDS_PER_MINUTE);

		long seconds = tempdur / 1000;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}	
	

	public void onStopTrackingTouch(SeekBar bar) {
		if (!instantSeeking) {
			mediaPlayer.seekTo((duration * bar.getProgress()) / 1000);
		}
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
	}

}
