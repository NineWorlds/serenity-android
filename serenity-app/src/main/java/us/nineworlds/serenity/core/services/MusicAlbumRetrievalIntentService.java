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
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.impl.MusicAlbumContentInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * A service that retrieves music information from the Plex Media Server.
 * 
 * @author dcarver
 * 
 */
public class MusicAlbumRetrievalIntentService extends
		AbstractPlexRESTIntentService {

	private static final String MUSIC_RETRIEVAL_INTENT_SERVICE = "MusicAlbumRetrievalIntentService";

	private static final String DEFAULT_CATEGORY = "all";

	protected List<MusicAlbumContentInfo> musicContentList = null;
	protected String key;
	protected String category;

	public MusicAlbumRetrievalIntentService() {
		super(MUSIC_RETRIEVAL_INTENT_SERVICE);
		musicContentList = new ArrayList<MusicAlbumContentInfo>();

	}

	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = musicContentList;
			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e(getClass().getName(), "Unable to send message", ex);
			}
		}

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		key = intent.getExtras().getString("key", "");
		createPosters();
		sendMessageResults(intent);
	}

	protected void createPosters() {
		MediaContainer mc = null;
		try {
			factory = SerenityApplication.getPlexFactory();
			mc = retrieveVideos();
		} catch (IOException ex) {
			Log.e("AbstractPosterImageGalleryAdapter",
					"Unable to talk to server: ", ex);
		} catch (Exception e) {
			Log.e("AbstractPosterImageGalleryAdapter", "Oops.", e);
		}

		if (mc != null && mc.getSize() > 0) {
			createVideoContent(mc);
		}

	}

	protected void createVideoContent(MediaContainer mc) {
		String baseUrl = factory.baseURL();
		List<Directory> videos = mc.getDirectories();
		String mediaTagId = Long.valueOf(mc.getMediaTagVersion()).toString();
		for (Directory music : videos) {
			MusicAlbumContentInfo mpi = new MusicAlbumContentInfo();
			mpi.setMediaTagIdentifier(mediaTagId);
			mpi.setId(music.getRatingKey());
			mpi.setSummary(music.getSummary());
			mpi.setKey(music.getKey());
			mpi.setYear(music.getYear());

			String turl = "";
			if (music.getThumb() != null) {
				turl = baseUrl + music.getThumb().replaceFirst("/", "");
				mpi.setImageURL(turl);
			} else if (mc.getParentPosterURL() != null) {
				turl = baseUrl + mc.getParentPosterURL().replaceFirst("/", "");
				mpi.setImageURL(turl);
			}

			mpi.setTitle(music.getTitle());

			musicContentList.add(mpi);
		}

	}

	protected MediaContainer retrieveVideos() throws Exception {
		if (category == null) {
			category = DEFAULT_CATEGORY;
		}

		String realKey = "";
		if (key.contains("/library")) {
			realKey = key;
		} else {
			realKey = "/library/metadata/" + key + "/children";
		}
		return factory.retrieveMusicMetaData(realKey);
	}

}
