<<<<<<< HEAD
// Copyright 2013 Google Inc. All Rights Reserved.
=======
/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver and others 
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
>>>>>>> 19347917c96684a26df34ef81aaad5d124fd42f9

package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
<<<<<<< HEAD
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.jess.ui.TwoWayAdapterView;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

import java.util.List;

public class TVShowGridOnKeyListener implements View.OnKeyListener {
    final PlexappFactory factory = SerenityApplication.getPlexFactory();
    private final Activity activity;

    public TVShowGridOnKeyListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_MEDIA_PLAY || event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }

        final TwoWayAdapterView gridView = (TwoWayAdapterView) v;
        final SeriesContentInfo info = (SeriesContentInfo) gridView.getSelectedItem();
        new AsyncTask<SeriesContentInfo, Void, EpisodePosterInfo>() {

            @Override
            protected EpisodePosterInfo doInBackground(SeriesContentInfo... infos) {
                return findFirstUnwatchedEpisode(infos[0]);
            }

            @Override
            protected void onPostExecute(EpisodePosterInfo info) {
                final String toast;
                if (info == null) {
                    toast = activity.getString(R.string.no_unwatched_episode_toast);
                } else {
                    toast = activity.getString(R.string.playing_episode_toast,
                            info.getSeason(),
                            info.getEpisodeNumber(),
                            info.getTitle());
                    VideoPlayerIntentUtils.playVideo(activity, info, true);
                }
                Toast.makeText(activity, toast, Toast.LENGTH_LONG).show();
            }
        }.execute(info);
        return true;
    }

    private EpisodePosterInfo findFirstUnwatchedEpisode(SeriesContentInfo info) {
        if (Integer.valueOf(info.getShowsUnwatched()) == 0) {
;
            return null;
        }
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
                    final EpisodePosterInfo videoInfo = EpisodeMediaContainer.createEpisodeContentInfo(
							activity, factory, episodeContainer, baseUrl, parentPosterURL, episode);
                    if (videoInfo.isWatched()) {
                        continue;
                    }
                    return videoInfo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
=======
import android.view.KeyEvent;
import android.view.View;
import com.jess.ui.TwoWayAdapterView;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

public class TVShowGridOnKeyListener implements View.OnKeyListener {
	final PlexappFactory factory = SerenityApplication.getPlexFactory();
	private final Activity activity;

	public TVShowGridOnKeyListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((keyCode != KeyEvent.KEYCODE_MEDIA_PLAY && keyCode != KeyEvent.KEYCODE_BUTTON_R1) || event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}

		final SeriesContentInfo info = getSelectedSeries(v);
		new FindUnwatchedAsyncTask(activity).execute(info);
		return true;
	}

	/**
	 * @param v
	 * @return
	 */
	protected SeriesContentInfo getSelectedSeries(View v) {
		final TwoWayAdapterView gridView = (TwoWayAdapterView) v;
		final SeriesContentInfo info = (SeriesContentInfo) gridView.getSelectedItem();
		return info;
	}

>>>>>>> 19347917c96684a26df34ef81aaad5d124fd42f9
}
