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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.nineworlds.plex.rest.model.impl.Director;
import us.nineworlds.plex.rest.model.impl.Genre;
import us.nineworlds.plex.rest.model.impl.Media;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Part;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.plex.rest.model.impl.Writer;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;

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
public class EpisodeRetrievalIntentService extends AbstractPlexRESTIntentService {

	/**
	 * 
	 */
	private static final String SEASON_EPISODE_UNKNOWN = "Unknown";
	protected List<VideoContentInfo> posterList = null;
	protected String key;
	private static final Pattern season = Pattern.compile("S\\d+");
	private static final Pattern episode = Pattern.compile("E\\d+");
	
	

	public EpisodeRetrievalIntentService() {
		super("EpisodeRetrievalIntentService");
		posterList = new ArrayList<VideoContentInfo>();

	}

	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = posterList;
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
			Log.e(getClass().getName(),
					"Unable to talk to server: ", ex);
			
		} catch (Exception e) {
			Log.e(getClass().getName(), "Oops.", e);
		}

		if (mc != null && mc.getSize() > 0) {
			createVideoContent(mc);
		}

	}


	/**
	 * 
	 * @return A media container with episodes or videos
	 * 
	 * @throws Exception
	 */
	protected MediaContainer retrieveVideos() throws Exception {
		MediaContainer mc;
		mc = factory.retrieveEpisodes(key);
		return mc;
	}
	
	/**
	 * @param mc
	 * @param baseUrl
	 */
	protected void createVideoContent(MediaContainer mc) {
		String baseUrl = factory.baseURL();
		String parentPosterURL = null;
		if (mc.getParentPosterURL() != null) {
			parentPosterURL = baseUrl + mc.getParentPosterURL().substring(1);
		}
		List<Video> videos = mc.getVideos();
		for (Video episode : videos) {
			VideoContentInfo  epi = new EpisodePosterInfo();
			epi.setParentPosterURL(parentPosterURL);
			epi.setId(episode.getRatingKey());
			epi.setPlotSummary(episode.getSummary());
			epi.setViewCount(episode.getViewCount());
			epi.setResumeOffset(Long.valueOf(episode.getViewOffset()).intValue());
			epi.setOriginalAirDate(episode.getOriginallyAvailableDate());
			
			if (episode.getParentThumbNailImageKey() != null) {
				epi.setParentPosterURL(baseUrl + episode.getParentThumbNailImageKey().substring(1));
			}
			
			String burl = factory.baseURL() + ":/resources/show-fanart.jpg"; 
			if (mc.getArt() != null) {
				burl = baseUrl + mc.getArt().replaceFirst("/", "");
			}
			epi.setBackgroundURL(burl);
			
			String turl = "";
			if (episode.getThumbNailImageKey() != null) {
				turl = baseUrl + episode.getThumbNailImageKey().replaceFirst("/", "");
			}
			
			epi.setPosterURL(turl);
			epi.setTitle(episode.getTitle());
			
			epi.setContentRating(episode.getContentRating());
			
			List<Media> mediacont = episode.getMedias();
			if (mediacont != null && !mediacont.isEmpty()) {
				// We grab the first media container until we know more about why there can be multiples.
				Media media = mediacont.get(0);
				List<Part> parts = media.getVideoPart();
				Part part = parts.get(0);
				
				setSeasonEpisode(epi, part);
				
				epi.setAudioCodec(media.getAudioCodec());
				epi.setVideoCodec(media.getVideoCodec());
				epi.setVideoResolution(media.getVideoResolution());
				epi.setAspectRatio(media.getAspectRatio());
				epi.setAudioChannels(media.getAudioChannels());
				
				String directPlayUrl = factory.baseURL() + part.getKey().replaceFirst("/", "");
				epi.setDirectPlayUrl(directPlayUrl);
				
			}
			
			createVideoDetails(episode, epi);
			epi.setCastInfo("");				
		
			posterList.add(epi);
		}
	}
	
	protected void setSeasonEpisode(VideoContentInfo epi, Part part) {
		if (part == null) {
			return;
		}
		
		String filename = part.getFilename();
		if (filename == null) {
			epi.setSeason(SEASON_EPISODE_UNKNOWN);
			epi.setEpisodeNumber(SEASON_EPISODE_UNKNOWN);
		}
				
		Matcher mseason = season.matcher(filename);
		Matcher mepisode = episode.matcher(filename);
		
		if (mseason.find()) {
			String sn = mseason.group();
			String number = sn.substring(1);
			String season = "Season " + Integer.toString(Integer.parseInt(number));  
			epi.setSeason(season);
		}
		
		
		if (mepisode.find()) {
			String en = mepisode.group();
			String number = en.substring(1);
			String episode = "Episode " + Integer.toString(Integer.parseInt(number)) ;
			epi.setEpisodeNumber(episode);
		}
	}
	

	/**
	 * @param video
	 * @param videoContentInfo
	 * @return
	 */
	protected void createVideoDetails(Video video,
			VideoContentInfo videoContentInfo) {

		if (video.getYear() != null) {
			videoContentInfo.setYear(video.getYear());
		}

		if (video.getGenres() != null && video.getGenres().size() > 0) {
			ArrayList<String> g = new ArrayList<String>();
			for (Genre genre : video.getGenres()) {
				g.add(genre.getTag());
			}
			videoContentInfo.setGenres(g);
		}

		if (video.getWriters() != null && video.getWriters().size() > 0) {
			ArrayList<String> w = new ArrayList<String>();
			for (Writer writer : video.getWriters()) {
				w.add(writer.getTag());
			}
			videoContentInfo.setWriters(w);
		}

		if (video.getDirectors() != null && video.getDirectors().size() > 0) {
			ArrayList<String> d = new ArrayList<String>();
			for (Director director : video.getDirectors()) {
				d.add(director.getTag());
			}
			videoContentInfo.setDirectors(d);
		}
		
	}

}
