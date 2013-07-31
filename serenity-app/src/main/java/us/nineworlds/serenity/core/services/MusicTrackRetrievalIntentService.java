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

import us.nineworlds.plex.rest.model.impl.Media;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Part;
import us.nineworlds.plex.rest.model.impl.Track;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
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
public class MusicTrackRetrievalIntentService extends AbstractPlexRESTIntentService {

	private static final String MUSIC_RETRIEVAL_INTENT_SERVICE = "MusicTrackRetrievalIntentService";

	protected List<AudioTrackContentInfo> musicContentList = null;
	protected String key;
	protected String category;

	public MusicTrackRetrievalIntentService() {
		super(MUSIC_RETRIEVAL_INTENT_SERVICE);
		musicContentList = new ArrayList<AudioTrackContentInfo>();

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
		List<Track> tracks = mc.getTracks();
		String mediaTagId = Long.valueOf(mc.getMediaTagVersion()).toString();
		for (Track track : tracks) {
			AudioTrackContentInfo mpi = new AudioTrackContentInfo();
			mpi.setMediaTagIdentifier(mediaTagId);
			mpi.setId(track.getKey());
			mpi.setSummary(track.getSummary());
			mpi.setDuration(track.getDuration());
			List<Media> medias = track.getMedias();
			Media mediaTrack = medias.get(0);
			mpi.setAudioChannels(mediaTrack.getAudioChannels());
			List<Part> parts = mediaTrack.getVideoPart();
			Part part = parts.get(0);
			mpi.setDirectPlayUrl(baseUrl + part.getKey().substring(1));
			mpi.setTitle(track
					.getTitle());
			if (mc.getParentPosterURL() != null) {
				mpi.setParentPosterURL(baseUrl + mc.getParentPosterURL().substring(1));
				mpi.setImageURL(baseUrl + mc.getParentPosterURL().substring(1));
			}
			
			musicContentList.add(mpi);
		}

	}


	protected MediaContainer retrieveVideos() throws Exception {
		return factory.retrieveMusicMetaData(key);
	}

}
