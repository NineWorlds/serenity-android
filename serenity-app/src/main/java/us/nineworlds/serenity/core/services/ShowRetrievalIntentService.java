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

package us.nineworlds.serenity.core.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.Genre;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author dcarver
 * 
 */
public class ShowRetrievalIntentService extends AbstractPlexRESTIntentService {

	private List<TVShowSeriesInfo> tvShowList = null;
	protected String key;
	protected String category;

	public ShowRetrievalIntentService() {
		super("ShowRetrievalIntentService");
		tvShowList = new ArrayList<TVShowSeriesInfo>();
	}

	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = tvShowList;
			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e(getClass().getName(), "Unable to send message", ex);
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		key = intent.getExtras().getString("key");
		category = intent.getExtras().getString("category");
		createBanners();
		sendMessageResults(intent);
	}

	protected void createBanners() {
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			mc = retrieveVideos();
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
			Log.e(getClass().getName(), "Unable to talk to server: ", ex);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Oops.", e);
		}

		if (mc != null && mc.getSize() > 0) {
			String mediaTagId = Long.valueOf(mc.getMediaTagVersion()).toString();
			List<Directory> shows = mc.getDirectories();
			if (shows != null) {
				for (Directory show : shows) {
					TVShowSeriesInfo mpi = new TVShowSeriesInfo();
					mpi.setId(show.getRatingKey());
					mpi.setMediaTagIdentifier(mediaTagId);
					if (show.getSummary() != null) {
						mpi.setPlotSummary(show.getSummary());
					}
					
					mpi.setStudio(show.getStudio());

					String burl = factory.baseURL()
							+ ":/resources/show-fanart.jpg";
					if (show.getArt() != null) {
						burl = baseUrl + show.getArt().replaceFirst("/", "");
					}
					mpi.setBackgroundURL(burl);

					String turl = "";
					if (show.getBanner() != null) {
						turl = baseUrl + show.getBanner().replaceFirst("/", "");
					}
					mpi.setPosterURL(turl);

					String thumbURL = "";
					if (show.getThumb() != null) {
						thumbURL = baseUrl
								+ show.getThumb().replaceFirst("/", "");
					}
					mpi.setThumbNailURL(thumbURL);

					mpi.setTitle(show.getTitle());

					mpi.setContentRating(show.getContentRating());

					List<String> genres = processGeneres(show);
					mpi.setGeneres(genres);

					int totalEpisodes = 0;
					int viewedEpisodes = 0;
					if (show.getLeafCount() != null) {
						totalEpisodes = Integer.parseInt(show.getLeafCount());
					}
					if (show.getViewedLeafCount() != null) {
						viewedEpisodes = Integer.parseInt(show.getViewedLeafCount());
					}
					int unwatched = totalEpisodes - viewedEpisodes;
					mpi.setShowsUnwatched(Integer.toString(unwatched));
					mpi.setShowsWatched(Integer.toString(viewedEpisodes));

					mpi.setKey(show.getKey());

					tvShowList.add(mpi);
				}
			}
		}
	}

	protected MediaContainer retrieveVideos() throws Exception {
		if (category == null) {
			category = "all";
		}

		return factory.retrieveSections(key, category);
	}

	protected List<String> processGeneres(Directory show) {
		ArrayList<String> genres = new ArrayList<String>();
		if (show.getGenres() != null) {
			for (Genre genre : show.getGenres()) {
				genres.add(genre.getTag());
			}
		}
		return genres;
	}

}
