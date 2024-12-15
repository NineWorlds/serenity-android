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

package us.nineworlds.serenity.ui.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.LinkedList;

import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.TimeUtil;
import us.nineworlds.serenity.ui.video.player.ExoplayerVideoActivity;

public class VideoPlayerIntentUtils {
  protected LinkedList<VideoContentInfo> videoQueue;
  protected SharedPreferences prefs;
  protected TimeUtil timeUtil;

  public VideoPlayerIntentUtils(LinkedList<VideoContentInfo> videoQueue, SharedPreferences prefs, TimeUtil timeUtil) {
    this.videoQueue = videoQueue;
    this.prefs = prefs;
    this.timeUtil = timeUtil;
  }
  public void playVideo(Activity activity, VideoContentInfo videoInfo, boolean autoResume) {
    if (!videoQueue.isEmpty()) {
      Toast.makeText(activity, "Cleared video queue before playback.", Toast.LENGTH_LONG).show();
      videoQueue.clear();
    }

    launchInternalPlayer(videoInfo, activity, autoResume);
  }

  /**
   *
   * @param videoInfo
   * @return
   * @param autoResume
   */
  private void launchInternalPlayer(VideoContentInfo videoInfo, Activity activity, boolean autoResume) {

    videoQueue.add(videoInfo);

    //Intent vpIntent = new Intent(activity, SerenitySurfaceViewVideoActivity.class);
    Intent vpIntent = new Intent(activity, ExoplayerVideoActivity.class);
    vpIntent.putExtra("autoResume", autoResume);

    activity.startActivityForResult(vpIntent, SerenityConstants.BROWSER_RESULT_CODE);
  }
}
