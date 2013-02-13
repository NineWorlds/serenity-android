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

package com.github.kingargyle.plexappclient.core.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.model.impl.Director;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.Genre;
import com.github.kingargyle.plexapp.model.impl.Media;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Part;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexapp.model.impl.Writer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.VideoContentInfo;
import com.github.kingargyle.plexappclient.core.model.impl.MoviePosterInfo;
import com.github.kingargyle.plexappclient.core.model.impl.TVShowSeriesInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author dcarver
 * 
 */
public class ShowRetrievalIntentService extends AbstractPlexRESTIntentService {

	private List<TVShowSeriesInfo> tvShowList = null;
	protected String key;

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
		createBanners();
		sendMessageResults(intent);
	}

	protected void createBanners() {
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			mc = factory.retrieveSections(key, "all");
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
			Log.e(getClass().getName(),"Unable to talk to server: ", ex);
		} catch (Exception e) {
			Log.e(getClass().getName(),"Oops.", e);
		}
		
		if (mc != null && mc.getSize() > 0) {			
			List<Directory> shows = mc.getDirectories();
			for (Directory show : shows) {
				TVShowSeriesInfo  mpi = new TVShowSeriesInfo();
				if (show.getSummary() != null) {
					mpi.setPlotSummary(show.getSummary());
				}
				
				String burl = factory.baseURL() + ":/resources/show-fanart.jpg"; 
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
					thumbURL = baseUrl + show.getThumb().replaceFirst("/", "");
				}
				mpi.setThumbNailURL(thumbURL);
				
				mpi.setTitle(show.getTitle());
				
				mpi.setContentRating(show.getContentRating());
				
				List<String> genres = processGeneres(show);
				mpi.setGeneres(genres);
				
			    mpi.setShowsWatched(show.getViewedLeafCount());
			    int totalEpisodes = Integer.parseInt(show.getLeafCount());
			    int unwatched = totalEpisodes - Integer.parseInt(show.getViewedLeafCount());
			    mpi.setShowsUnwatched(Integer.toString(unwatched));
			    
			    mpi.setKey(show.getKey());
			    
				tvShowList.add(mpi);
			}
		}		
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
