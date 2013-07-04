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

import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author dcarver
 * 
 */
public class SkipForwardOnClickListener implements OnClickListener {

	private MediaPlayer mediaPlayer;

	/**
	 * 
	 */
	public SkipForwardOnClickListener(MediaPlayer mp) {
		mediaPlayer = mp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		try {

			if (mediaPlayer == null || mediaPlayer.getDuration() == 0) {
				return;
			}

			int currentTime = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			if (currentTime < duration) {
				int newPosition = currentTime + 10000;
				if (newPosition > duration) {
					newPosition = duration;
				}
				mediaPlayer.seekTo(newPosition);
			}
		} catch (IllegalStateException ex) {

		}

	}

}
