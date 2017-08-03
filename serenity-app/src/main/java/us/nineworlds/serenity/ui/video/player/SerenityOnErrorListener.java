/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2012-2013 David Carver
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.video.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class SerenityOnErrorListener implements OnErrorListener {

  @Override public boolean onError(MediaPlayer mp, int what, int extra) {
    String error_msg = "What: " + what + "Extra: " + extra;
    if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
      error_msg = "Unknown Media Player Error. Extra Code: " + extra;
    }

    if (what == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
      error_msg = "Media not valid for progessive playback. Extra Code: " + extra;
    }

    if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
      error_msg = "Server croaked. Extra Code: " + extra;
    }

    Log.e(getClass().getName(), error_msg);
    return true;
  }
}
