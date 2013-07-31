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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * @author dcarver
 *
 */
public class PlayButtonListener implements OnClickListener {

	MediaPlayer mediaPlayer;
	AudioTrackContentInfo currentPlaying = null;
	
	PlayButtonListener(MediaPlayer mp) {
		mediaPlayer = mp;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (mediaPlayer == null) {
			return;
		}
		
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			return;
		}
		
		
		if (mediaPlayer.getCurrentPosition() > 0 && mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration()) {
			mediaPlayer.start();
			return;
		}
		
		try {
			mediaPlayer.release();
			
			ListView lv = null;
			Activity activity = (Activity) v.getContext();
			lv = (ListView) activity.findViewById(R.id.audioTracksListview);
			AudioTrackContentInfo mt =  (AudioTrackContentInfo) lv.getSelectedItem();
			if (mt == null) {
				return;
			}
			mediaPlayer.setDataSource(mt.getDirectPlayUrl());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			Log.e("TrackPlayButton", "Unable to set Track to be played", e);
		} catch (IllegalStateException e) {
			Log.e("TrackPlayButton", "Unable to set Track to be played", e);
		} catch (IOException e) {
			Log.e("TrackPlayButton", "Unable to set Track to be played", e);
		}
	}

}
