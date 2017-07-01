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

package us.nineworlds.serenity.ui.video.player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.OnDeckRecommendationAsyncTask;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.util.ExternalPlayerResultHandler;
import us.nineworlds.serenity.ui.util.PlayerResultHandler;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

/**
 * @author dcarver
 *
 */
public class RecommendationPlayerActivity extends SerenityActivity {

    VideoContentInfo video;

    @Inject
    protected VideoPlayerIntentUtils vpUtils;

    @Override
    protected void createSideMenu() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_playback);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        if (intent != null) {
            Object objVideo = intent.getExtras().getSerializable(
                    "serenity_video");
            if (objVideo != null) {
                video = (VideoContentInfo) objVideo;
                vpUtils.playVideo(this, video, true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean externalPlayer = prefs.getBoolean("external_player", false);

        if (data != null) {
            if (externalPlayer) {
                ExternalPlayerResultHandler externalPlayerHandler = new ExternalPlayerResultHandler(
                        resultCode, data, this, null);
                externalPlayerHandler.updateVideoPlaybackPosition(video);
            } else {
                PlayerResultHandler playerResultHandler = new PlayerResultHandler(
                        data, null);
                playerResultHandler.updateVideoPlaybackPosition(video);
            }
        }

        OnDeckRecommendationAsyncTask onDeckRecomendations = new OnDeckRecommendationAsyncTask(
                getApplicationContext());
        onDeckRecomendations.execute();

        finish();
    }
}
