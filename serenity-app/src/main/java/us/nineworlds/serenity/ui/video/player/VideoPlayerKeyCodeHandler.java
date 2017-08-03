/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver
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

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import javax.inject.Inject;
import us.nineworlds.serenity.injection.BaseInjector;

public class VideoPlayerKeyCodeHandler extends BaseInjector {

  @Inject protected SharedPreferences preferences;

  protected MediaPlayer mediaPlayer;
  protected MediaController mediaController;
  protected Handler progressReportinHandler;
  protected Runnable progressRunnable;
  protected View timeOfDayView;
  protected Activity videoPlayerActivity;

  protected int osdDelayTime;

  public VideoPlayerKeyCodeHandler(MediaPlayer mediaPlayer, MediaController mediaController,
      int osdDelayTime, Handler progressReportingHandler, Runnable progressRunnable, View timeOfDay,
      Activity videoPlayerActivity) {
    this.mediaPlayer = mediaPlayer;
    this.mediaController = mediaController;
    this.osdDelayTime = osdDelayTime;
    this.progressReportinHandler = progressReportingHandler;
    this.progressRunnable = progressRunnable;
    this.videoPlayerActivity = videoPlayerActivity;
    this.timeOfDayView = timeOfDay;
  }

  public boolean onKeyDown(int keyCode, KeyEvent event, boolean isMediaPlayerStateValid) {
    // All actions from here on require media to be in a valid state
    if (!isMediaPlayerStateValid) {
      return false;
    }

    final String nextPrevBehavior = preferences.getString("next_prev_behavior", "queue");

    if (isKeyCodeInfo(keyCode)) {
      if (mediaController.isShowing()) {
        mediaController.hide();
      } else {
        mediaController.show(osdDelayTime);
      }
      return true;
    }

    if (isKeyCodePlay(keyCode)) {
      if (mediaPlayer.isPlaying()) {
        if (mediaController.isShowing()) {
          mediaController.hide();
        } else {
          mediaController.show(osdDelayTime);
        }
      } else {
        mediaPlayer.start();
        mediaController.hide();
        progressReportinHandler.postDelayed(progressRunnable, 5000);
      }
      return true;
    }

    if (isKeyCodePauseResume(keyCode)) {
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.pause();
        mediaController.show(osdDelayTime);
        progressReportinHandler.removeCallbacks(progressRunnable);
      } else {
        mediaPlayer.start();
        mediaController.hide();
        progressReportinHandler.postDelayed(progressRunnable, 5000);
      }
      return true;
    }

    if (keyCode == KeyEvent.KEYCODE_T) {
      final boolean showTimeOfDay = !preferences.getBoolean("showTimeOfDay", false);
      timeOfDayView.setVisibility(showTimeOfDay ? View.VISIBLE : View.GONE);
      preferences.edit().putBoolean("showTimeOfDay", showTimeOfDay).apply();
      return true;
    }

    if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
      if (nextPrevBehavior.equals("queue")) {
        mediaController.hide();
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.stop();
        }
        videoPlayerActivity.finish();
        return true;
      }
      final int skipTo;
      final int currentPosition = mediaPlayer.getCurrentPosition();
      final int duration = mediaPlayer.getDuration();

      if (nextPrevBehavior.endsWith("%")) {
        final Integer percent =
            Integer.valueOf(nextPrevBehavior.substring(0, nextPrevBehavior.length() - 1));
        skipTo = currentPosition + duration * percent / 100;
      } else {
        skipTo = currentPosition + Integer.valueOf(nextPrevBehavior);
      }
      skipToOffset(skipTo);
      return true;
    }

    try {
      final int currentPosition = mediaPlayer.getCurrentPosition();
      final int duration = mediaPlayer.getDuration();
      if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS && !nextPrevBehavior.equals("queue")) {
        final int skipTo;
        if (nextPrevBehavior.endsWith("%")) {
          final Integer percent =
              Integer.valueOf(nextPrevBehavior.substring(0, nextPrevBehavior.length() - 1));
          skipTo = currentPosition - duration * percent / 100;
        } else {
          skipTo = currentPosition - Integer.valueOf(nextPrevBehavior);
        }
        skipToOffset(skipTo);
        return true;
      }

      if (isKeyCodeSkipForward(keyCode)) {
        skipToOffset(
            currentPosition + Integer.valueOf(preferences.getString("skip_forward_time", "30000")));
        return true;
      }

      if (isKeyCodeSkipBack(keyCode)) {
        skipToOffset(currentPosition - Integer.valueOf(
            preferences.getString("skip_backward_time", "10000")));
        return true;
      }

      if (isKeyCodeStop(keyCode)) {
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.pause();
          if (!mediaController.isShowing()) {
            mediaController.show(osdDelayTime);
          }
        }
        return true;
      }

      if (isSkipByPercentage(keyCode)) {
        return true;
      }
    } catch (IllegalStateException e) {
      Log.e(this.getClass().getName(), "Media Player is in an illegalstate.", e);
    }

    return false;
  }

  protected boolean isKeyCodeStop(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_MEDIA_STOP || keyCode == KeyEvent.KEYCODE_S;
  }

  protected boolean isKeyCodeSkipBack(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
        || keyCode == KeyEvent.KEYCODE_R
        || keyCode == KeyEvent.KEYCODE_BUTTON_L1
        || keyCode == KeyEvent.KEYCODE_BUTTON_L2;
  }

  protected boolean isKeyCodeSkipForward(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
        || keyCode == KeyEvent.KEYCODE_F
        || keyCode == KeyEvent.KEYCODE_BUTTON_R1
        || keyCode == KeyEvent.KEYCODE_BUTTON_R2;
  }

  protected boolean isKeyCodePauseResume(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
        || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
        || keyCode == KeyEvent.KEYCODE_P
        || keyCode == KeyEvent.KEYCODE_SPACE
        || keyCode == KeyEvent.KEYCODE_BUTTON_A
        || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY;
  }

  protected boolean isKeyCodeInfo(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_INFO
        || keyCode == KeyEvent.KEYCODE_I
        || keyCode == KeyEvent.KEYCODE_MENU
        || keyCode == KeyEvent.KEYCODE_BUTTON_Y;
  }

  protected boolean isKeyCodePlay(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_MEDIA_PLAY;
  }

  protected boolean isSkipByPercentage(int keyCode) {
    int duration = mediaPlayer.getDuration();
    if (keyCode == KeyEvent.KEYCODE_1) {
      skipByPercentage(duration, 0.10f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_2) {
      skipByPercentage(duration, 0.20f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_3) {
      skipByPercentage(duration, 0.30f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_4) {
      skipByPercentage(duration, 0.40f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_5) {
      skipByPercentage(duration, 0.50f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_6) {
      skipByPercentage(duration, 0.60f);
      return true;
    }

    if (keyCode == KeyEvent.KEYCODE_7) {
      skipByPercentage(duration, 0.70f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_8) {
      skipByPercentage(duration, 0.80f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_9) {
      skipByPercentage(duration, 0.90f);
      return true;
    }
    if (keyCode == KeyEvent.KEYCODE_0) {
      skipToPercentage(0);
      return true;
    }
    return false;
  }

  protected void skipByPercentage(int duration, float percentage) {
    int newPos = Math.round(duration * percentage);
    skipToPercentage(newPos);
  }

  protected void skipToPercentage(int newPos) {
    mediaPlayer.seekTo(newPos);
    if (!mediaController.isShowing()) {
      mediaController.show(osdDelayTime);
    }
  }

  protected void skipToOffset(int skipOffset) {
    int duration = mediaPlayer.getDuration();
    if (skipOffset > duration) {
      skipOffset = duration - 1;
    } else if (skipOffset < 0) {
      skipOffset = 0;
    }
    if (!mediaController.isShowing()) {
      mediaController.show(osdDelayTime);
    }
    mediaPlayer.seekTo(skipOffset);
  }
}
