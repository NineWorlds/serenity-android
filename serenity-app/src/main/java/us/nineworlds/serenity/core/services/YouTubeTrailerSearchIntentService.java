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
import java.net.MalformedURLException;
import java.net.URL;
import us.nineworlds.serenity.core.model.impl.YouTubeVideoContentInfo;

import com.google.gdata.client.Query;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.client.youtube.YouTubeQuery.OrderBy;
import com.google.gdata.data.Category;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.ServiceException;

import android.app.IntentService;
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
public class YouTubeTrailerSearchIntentService extends IntentService {

	private String videoTitle;
	private static final String TRAILER = " Offical Trailer";
	private boolean trailerCategory = true;
	private static final String YTFEED = "http://gdata.youtube.com/feeds/api/videos";
	protected YouTubeVideoContentInfo videoInfo = new YouTubeVideoContentInfo();
	
	/**
	 * @param name
	 */
	public YouTubeTrailerSearchIntentService() {
		super("YouTubeTrailerSearchIntentService");
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		String title = intent.getStringExtra("videoTitle");
		String year = intent.getStringExtra("year");
		String show = intent.getStringExtra("show");
		String season = intent.getStringExtra("season");
		String episodeNum = intent.getStringExtra("episodeNum");
		if (show == null) {
			videoTitle = "\"" + title + "\"" + TRAILER + " HD" + " " + year;
			trailerCategory = true;
		} else {
			trailerCategory = false;
			videoTitle = "\"" + show + "\" " + season + " " + episodeNum + " Promo";
		}
		searchForVideo();
		sendMessageResults(intent);
	}

	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = videoInfo;
			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e(getClass().getName(), "Unable to send message", ex);
			}
		}
		
	}
	
	protected void searchForVideo() {
		try {
			YouTubeService service = new YouTubeService("com.nineworlds.Serenity");
			YouTubeQuery query = new YouTubeQuery(new URL(YTFEED));
			query.setOrderBy(OrderBy.RELEVANCE);
			query.setFullTextQuery(videoTitle);
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
			query.setMaxResults(1);
			Query.CategoryFilter category = new Query.CategoryFilter();
			if (trailerCategory) {
				category.addCategory(new Category(YouTubeNamespace.KEYWORD_SCHEME, "trailer"));
				query.addCategoryFilter(category);
			}
						
			VideoFeed videoFeed = service.query(query, VideoFeed.class);
			if (!videoFeed.getEntries().isEmpty()) {
				VideoEntry entry = videoFeed.getEntries().get(0);
				videoInfo = new YouTubeVideoContentInfo();
				videoInfo.setId(entry.getMediaGroup().getVideoId());
			}
						 
		} catch (MalformedURLException e) {
			Log.e("YouTubeSearch", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("YouTubeSearch", e.getMessage(), e);
		} catch (ServiceException e) {
			Log.e("YouTubeSearch", e.getMessage(), e);
		}
	}

}
