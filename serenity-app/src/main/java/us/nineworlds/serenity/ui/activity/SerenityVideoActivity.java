/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package us.nineworlds.serenity.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.LinkedList;
import javax.inject.Inject;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ExternalPlayerResultHandler;
import us.nineworlds.serenity.ui.util.PlayerResultHandler;
import us.nineworlds.serenity.ui.video.player.ExoplayerVideoActivity;

public abstract class SerenityVideoActivity extends SerenityActivity {

  @Inject @ForVideoQueue LinkedList<VideoContentInfo> videoQueue;

  public abstract AbstractPosterImageGalleryAdapter getAdapter();

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    boolean externalPlayer = prefs.getBoolean("external_player", false);
    RecyclerView videoRecyclerView = findVideoRecyclerView();

    if (requestCode != SerenityConstants.BROWSER_RESULT_CODE) {
      return;
    }

    if (!(videoRecyclerView instanceof RecyclerView)) {
      return;
    }

    View selectedView;
    VideoContentInfo video;
    AbstractPosterImageGalleryAdapter adapter = (AbstractPosterImageGalleryAdapter) videoRecyclerView.getAdapter();
    if (adapter == null) {
      return;
    }

    selectedView = videoRecyclerView.getFocusedChild();
    int selectedPosition = videoRecyclerView.getChildAdapterPosition(selectedView);
    if (selectedPosition == -1) {
      return;
    }

    video = (VideoContentInfo) adapter.getItem(videoRecyclerView.getChildAdapterPosition(selectedView));

    if (data != null) {
      if (externalPlayer) {
        ExternalPlayerResultHandler externalPlayerHandler =
            new ExternalPlayerResultHandler(resultCode, data, this, adapter);
        externalPlayerHandler.updatePlaybackPosition(video, selectedView);
      } else {
        PlayerResultHandler playerResultHandler =

            new PlayerResultHandler(data, adapter);
        playerResultHandler.updateVideoPlaybackPosition(video, selectedView);
      }
    }

    if (requestCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {

      if (resultCode == SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY) {
        if (!videoQueue.isEmpty()) {
          showQueueNotEmptyMessage();
        }
        return;
      }

      if (!externalPlayer) {
        if (!videoQueue.isEmpty()) {
          Intent vpIntent = new Intent(this, ExoplayerVideoActivity.class);
          startActivityForResult(vpIntent, SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY);
        }
      }
    }
  }

  protected abstract RecyclerView findVideoRecyclerView();

  protected void showQueueNotEmptyMessage() {
    Toast.makeText(this, R.string.there_are_still_videos_int_the_queue_, Toast.LENGTH_LONG).show();
  }
}
