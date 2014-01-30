// Copyright 2013 Google Inc. All Rights Reserved.

package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import com.jess.ui.TwoWayAdapterView;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.core.services.EpisodeRetrievalIntentService;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;

import java.util.LinkedList;
import java.util.List;

public class TVShowGridOnKeyListener implements View.OnKeyListener {
    final PlexappFactory factory = SerenityApplication.getPlexFactory();
    private final Activity activity;

    public TVShowGridOnKeyListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_MEDIA_PLAY) {
            return false;
        }

        final TwoWayAdapterView gridView = (TwoWayAdapterView) v;
        final SeriesContentInfo info = (SeriesContentInfo) gridView.getSelectedItem();
        if (Integer.valueOf(info.getShowsUnwatched()) == 0) {
            return true;
        }
        new Thread() {
            @Override
            public void run() {
                playFirstUnwatchedEpisode(info);
            }
        }.start();
        return true;
    }

    private void playFirstUnwatchedEpisode(SeriesContentInfo info) {
        try {
            final MediaContainer seasonContainer = factory.retrieveSeasons(info.getKey());
            final List<Directory> seasons = seasonContainer.getDirectories();
            for (Directory season : seasons) {
                final MediaContainer episodeContainer = factory.retrieveEpisodes(season.getKey());
                final List<Video> episodes = episodeContainer.getVideos();

                String baseUrl = factory.baseURL();
                String parentPosterURL = null;
                if (episodeContainer.getParentPosterURL() != null && !episodeContainer.getParentPosterURL().contains("show")) {
                    parentPosterURL = baseUrl + episodeContainer.getParentPosterURL().substring(1);
                }
                for (Video episode : episodes) {
                    final VideoContentInfo videoInfo = EpisodeRetrievalIntentService.createEpisodeContentInfo(
                            activity, factory, episodeContainer, baseUrl, parentPosterURL, episode);
                    if (videoInfo.isWatched()) {
                        continue;
                    }
                    final LinkedList<VideoContentInfo> videoPlaybackQueue = SerenityApplication.getVideoPlaybackQueue();
                    videoPlaybackQueue.clear();
                    videoPlaybackQueue.add(videoInfo);

                    Intent vpIntent = new Intent(activity,
                            SerenitySurfaceViewVideoActivity.class);

                    activity.startActivityForResult(vpIntent, SerenityConstants.BROWSER_RESULT_CODE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
