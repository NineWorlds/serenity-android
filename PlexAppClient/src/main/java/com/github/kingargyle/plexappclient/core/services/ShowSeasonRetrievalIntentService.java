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

import com.bugsense.trace.BugSenseHandler;
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
public class ShowSeasonRetrievalIntentService extends AbstractPlexRESTIntentService {

	private List<TVShowSeriesInfo> seriesList = null;
	protected String key;

	public ShowSeasonRetrievalIntentService() {
		super("ShowRetrievalIntentService");
		seriesList = new ArrayList<TVShowSeriesInfo>();
	}

	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = seriesList;
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
		createSeries();
		sendMessageResults(intent);
	}

	protected void createSeries() {
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			mc = factory.retrieveSeasons(key);
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
			Log.e(getClass().getName(),"Unable to talk to server: ", ex);
			BugSenseHandler.sendExceptionMessage(this.getClass().getName(), "createSeries", ex);			
		} catch (Exception e) {
			Log.e(getClass().getName(),"Oops.", e);
			BugSenseHandler.sendExceptionMessage(this.getClass().getName(), "createSeries", e);			
		}
		
		if (mc == null ) {
			return;
		}
		
		List<Directory> shows = mc.getDirectories();
		for (Directory show : shows) {
			TVShowSeriesInfo  mpi = new TVShowSeriesInfo();
			
			if (mc.getTitle2() != null) {
				mpi.setParentShowTitle(mc.getTitle2());
			}
			
			String burl = baseUrl + ":/resources/show-fanart.jpg"; 
			if (mc.getArt() != null) {
				burl = baseUrl + mc.getArt().replaceFirst("/", "");
			}
			mpi.setBackgroundURL(burl);
			
			String turl = "";
			if (show.getThumb() != null) {
				turl = baseUrl + show.getThumb().replaceFirst("/", "");
			}
			mpi.setPosterURL(turl);
			mpi.setKey(show.getKey());
							
			mpi.setTitle(show.getTitle());			
			
		    mpi.setShowsWatched(show.getViewedLeafCount());
		    int totalEpisodes = Integer.parseInt(show.getLeafCount());
		    int unwatched = totalEpisodes - Integer.parseInt(show.getViewedLeafCount());
		    mpi.setShowsUnwatched(Integer.toString(unwatched));
		    
			seriesList.add(mpi);
		}
	}
}
