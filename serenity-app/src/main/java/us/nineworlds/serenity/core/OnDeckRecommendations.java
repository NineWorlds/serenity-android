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

package us.nineworlds.serenity.core;

import java.io.IOException;
import java.util.List;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayer;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayerFactory;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

public class OnDeckRecommendations {

	Context context;
	RequestQueue queue;
	final PlexappFactory factory = SerenityApplication.getPlexFactory();
	NotificationManager notificationManager;

	protected List<MenuItem> menuItems;

	public OnDeckRecommendations(Context context) {
		this.context = context;
		queue = VolleyUtils.getRequestQueueInstance(context);
	}

	public void recommend(String url) {

	}

	public void recommend() {
		String sectionsURL = factory.getSectionsURL();
		VolleyUtils.volleyXmlGetRequest(sectionsURL,
				new LibraryResponseListener(),
				new DefaultLoggingVolleyErrorListener());
	}

	protected class LibraryResponseListener implements
			Response.Listener<MediaContainer> {

		@Override
		public void onResponse(MediaContainer mc) {
			menuItems = new MenuMediaContainer(mc, context).createMenuItems();
			if (menuItems.isEmpty()) {
				return;
			}

			for (MenuItem library : menuItems) {
				if ("movie".equals(library.getType())) {
					String section = library.getSection();
					String onDeckURL = factory
							.getSectionsURL(section, "onDeck");
					VolleyUtils.volleyXmlGetRequest(onDeckURL,
							new MovieOnDeckResponseListener(),
							new DefaultLoggingVolleyErrorListener());
				}

				if ("show".equals(library.getType())) {
					String section = library.getSection();
					String onDeckUrl = factory
							.getSectionsURL(section, "onDeck");
					VolleyUtils.volleyXmlGetRequest(onDeckUrl,
							new TVOnDeckResponseListener(),
							new DefaultLoggingVolleyErrorListener());
				}
			}
		}
	}

	protected class MovieOnDeckResponseListener implements
			Response.Listener<MediaContainer> {

		@Override
		public void onResponse(MediaContainer mc) {
			MovieMediaContainer movieContainer = new MovieMediaContainer(mc);
			List<VideoContentInfo> movies = movieContainer.createVideos();
			for (VideoContentInfo movie : movies) {
				new RecommendAsyncTask(movie, context).execute();
			}
		}
	}

	protected class TVOnDeckResponseListener implements
			Response.Listener<MediaContainer> {

		@Override
		public void onResponse(MediaContainer mc) {
			EpisodeMediaContainer episodeContainer = new EpisodeMediaContainer(
					mc, context);
			List<VideoContentInfo> episodes = episodeContainer.createVideos();
			for (VideoContentInfo episode : episodes) {
				new RecommendAsyncTask(episode, context).execute();
			}
		}

	}

	protected class RecommendAsyncTask extends AsyncTask {
		private final VideoContentInfo video;
		private final Context context;

		public RecommendAsyncTask(VideoContentInfo video, Context context) {
			this.video = video;
			this.context = context;
		}

		@Override
		protected Object doInBackground(Object... params) {
			RecommendationBuilder builder = new RecommendationBuilder();
			try {

				final SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);
				boolean externalPlayer = prefs.getBoolean("external_player",
						false);

				builder = builder.setContext(context)
						.setBackground(video.getBackgroundURL())
						.setTitle(video.getTitle())
						.setImage(video.getImageURL())
						.setId(Integer.parseInt(video.id()))
						.setDescription(video.getSummary())
						.setSmallIcon(R.drawable.androidtv_icon_mono);
				if (externalPlayer) {
					PendingIntent intent = buildPendingIntent(video);
					builder = builder.setIntent(intent);
				}

				builder.build();
			} catch (IOException ex) {
				Log.e("OnDeckRecommendation", "Error building recommendation: "
						+ builder.toString(), ex);
			}
			return null;
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		private PendingIntent buildPendingIntent(VideoContentInfo movie) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String externalPlayerValue = preferences.getString(
					"serenity_external_player_filter", "default");
			ExternalPlayerFactory factory = new ExternalPlayerFactory(movie,
					context);
			ExternalPlayer extplay = factory
					.createExternalPlayer(externalPlayerValue);
			Intent intent = extplay.createIntent();

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addNextIntent(intent);
			// Ensure a unique PendingIntents, otherwise all recommendations end
			// up with the same
			// PendingIntent
			intent.setAction(movie.id());

			PendingIntent pintent = stackBuilder.getPendingIntent(0,
					PendingIntent.FLAG_UPDATE_CURRENT);
			return pintent;
		}

	}
}
