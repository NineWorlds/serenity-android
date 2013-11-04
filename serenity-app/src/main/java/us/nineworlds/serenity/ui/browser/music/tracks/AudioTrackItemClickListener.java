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

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class AudioTrackItemClickListener implements OnItemClickListener {

	MediaPlayer mediaPlayer;

	AudioTrackItemClickListener(MediaPlayer mp) {
		mediaPlayer = mp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		AudioTrackContentInfo mt = (AudioTrackContentInfo) parent
				.getItemAtPosition(position);
		Activity context = (Activity) parent.getContext();
		ListView lv = (ListView) context.findViewById(R.id.audioTracksListview);
		TextView ptv = (TextView) context.findViewById(R.id.track_playing);
		ptv.setText(mt.getTitle());
		lv.setSelection(position);
		MusicTracksActivity.currentPlayingItem = position;
		
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(mt.getDirectPlayUrl()));
		} else {
			try {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(view.getContext(), Uri.parse(mt.getDirectPlayUrl()));
				mediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				Log.e(getClass().getName(), "Unable to play Track", e);
			} catch (SecurityException e) {
				Log.e(getClass().getName(), "Unable to play Track", e);
			} catch (IllegalStateException e) {
				Log.e(getClass().getName(), "Unable to play Track", e);
			} catch (IOException e) {
				Log.e(getClass().getName(), "Unable to play Track", e);
			}
		}

		try {
			showControlls(context);
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			Log.e("TrackPlayButton", "Unable to play Track", e);
		} catch (IllegalStateException e) {
			Log.e("TrackPlayButton", "Unable to play Track", e);
		}

	}

	/**
	 * @param context
	 */
	protected void showControlls(Activity context) {
		ImageButton playBtn = (ImageButton) context.findViewById(R.id.audioPause);
		playBtn.setVisibility(View.VISIBLE);
		
		SeekBar seekBar = (SeekBar) context.findViewById(R.id.mediacontroller_seekbar);
		seekBar.setVisibility(View.VISIBLE);
		
		ImageButton nextBtn = (ImageButton) context.findViewById(R.id.audioSkipForward);
		nextBtn.setVisibility(View.VISIBLE);
		
		ImageButton prevBtn = (ImageButton) context.findViewById(R.id.audioSkipBack);
		prevBtn.setVisibility(View.VISIBLE);
		
		ImageButton nextTrack = (ImageButton) context.findViewById(R.id.audioNextTrack);
		nextTrack.setVisibility(View.VISIBLE);
		
		ImageButton prevTrack = (ImageButton) context.findViewById(R.id.audioPrevTrack);
		prevTrack.setVisibility(View.VISIBLE);
		
		TextView currentTime = (TextView) context.findViewById(R.id.mediacontroller_time_current);
		currentTime.setVisibility(View.VISIBLE);
		
		TextView durationTime = (TextView) context.findViewById(R.id.mediacontroller_time_total);
		durationTime.setVisibility(View.VISIBLE);
		
		TextView playingTrack = (TextView) context.findViewById(R.id.track_playing);
		playingTrack.setVisibility(View.VISIBLE);
		
		CheckBox randomPlay = (CheckBox) context.findViewById(R.id.audioRandomPlay);
		if (randomPlay != null) {
			randomPlay.setVisibility(View.VISIBLE);
		}
		
		LinearLayout mediaProgressControl = (LinearLayout) context.findViewById(R.id.mediacontroller_progress_layout);
		mediaProgressControl.setVisibility(View.VISIBLE);

	}
}
