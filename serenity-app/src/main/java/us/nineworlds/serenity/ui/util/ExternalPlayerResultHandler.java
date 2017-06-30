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
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayer;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayerFactory;
import us.nineworlds.serenity.core.externalplayer.MXPlayer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;

public class ExternalPlayerResultHandler extends PlayerResultHandler {

    Activity activity;
    String externalPlayerValue;
    boolean extPlayerVideoQueueEnabled;
    int resultCode;
    ExternalPlayer externalPlayer;

    @Inject
    protected VideoPlayerIntentUtils vpUtils;

    @Inject
    @ForVideoQueue
    protected LinkedList<VideoContentInfo> videoQueue;

    @Inject
    protected SharedPreferences preferences;

    public ExternalPlayerResultHandler(int resultCode, Intent data,
                                       Activity activity, AbstractPosterImageGalleryAdapter adapter) {
        super(data, adapter);
        this.activity = activity;
        externalPlayerValue = preferences.getString(
                "serenity_external_player_filter", "default");
        extPlayerVideoQueueEnabled = preferences.getBoolean(
                "external_player_continuous_playback", false);
        this.resultCode = resultCode;
    }

    public void updatePlaybackPosition(VideoContentInfo video, View selectedView) {
        ExternalPlayerFactory externalPlayerFactory = new ExternalPlayerFactory(
                video, activity);
        externalPlayer = externalPlayerFactory
                .createExternalPlayer(externalPlayerValue);

        boolean mxPlayerCompleted = updateMXPlayerCompletedNormally(video);
        if (shouldUpdatePlaybackPosition(mxPlayerCompleted)) {
            updateVideoPlaybackPosition(video, selectedView);
        }

        if (!extPlayerVideoQueueEnabled) {
            return;
        }

        playNextQueueEntry();
        return;
    }

    private void playNextQueueEntry() {
        if (videoQueue.isEmpty()) {
            return;
        }

        if (isVimuUserCancelResult(resultCode)) {
            showQueueNotEmptyMessage();
            return;
        }

        if (notMXPlayer(data)) {
            externalPlayerPlayNext();
            return;
        }

        String mxplayerEndedBy = data.getStringExtra("end_by");
        if ("user".equals(mxplayerEndedBy)) {
            showQueueNotEmptyMessage();
            return;
        }

        externalPlayerPlayNext();
    }

    private boolean shouldUpdatePlaybackPosition(boolean mxPlayerCompleted) {
        return !mxPlayerCompleted && externalPlayer.supportsPlaybackPosition()
                && data.hasExtra("position");
    }

    private boolean updateMXPlayerCompletedNormally(VideoContentInfo video) {
        if (externalPlayer instanceof MXPlayer
                && hasMXPlayerCompletedNormally(data)) {
            if (video != null) {
                video.setResumeOffset(video.getDuration());
                toggleWatched(video);
                adapter.notifyDataSetChanged();
            }
            return true;
        }
        return false;
    }

    protected boolean hasMXPlayerCompletedNormally(Intent data) {
        String mxplayerString = data.getStringExtra("end_by");
        return "playback_completion".equals(mxplayerString);
    }

    protected boolean notMXPlayer(Intent data) {
        return !data.hasExtra("end_by");
    }

    protected boolean isVimuUserCancelResult(int resultCode) {
        return resultCode == 0 || resultCode == 4;
    }

    protected void showQueueNotEmptyMessage() {
        Toast.makeText(activity,
                R.string.there_are_still_videos_int_the_queue_,
                Toast.LENGTH_LONG).show();
    }

    protected void externalPlayerPlayNext() {
        VideoContentInfo videoContentInfo = videoQueue.poll();
        vpUtils.launchExternalPlayer(videoContentInfo, activity, false);
    }

}
