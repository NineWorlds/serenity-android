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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.YouTubeVideoContentInfo;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

/**
 * @author davidcarver
 *
 */
public class TrailersYouTubeSearch {

	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static JsonFactory JSON_FACTORY = new JacksonFactory();
	private static String API_KEY = "AIzaSyBaVoUqzjCUOwxaYAz7yQDXIJzUyBQSako";
	protected YouTubeVideoContentInfo videoInfo = new YouTubeVideoContentInfo();
	private static final String TRAILER = " Offical Trailer";

	private class DoNothingHttpRequestInitializer implements
			HttpRequestInitializer {

		@Override
		public void initialize(HttpRequest arg0) throws IOException {

		}

	}

	public YouTubeVideoContentInfo searchForVideo(String videoTitle) {
		try {
			YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					new DoNothingHttpRequestInitializer()).setApplicationName(
					"com.nineworlds.Serenity").build();

			YouTube.Search.List search = youtube.search().list("id,snippet");
			search.setKey(API_KEY);
			search.setType("videoes");
			search.setMaxResults(1l);
			search.setSafeSearch("none");
			search.setQ(videoTitle);
			search.setOrder("relevance");

			SearchListResponse searchListResponse = search.execute();
			List<SearchResult> searchResultList = searchListResponse.getItems();
			if (searchResultList != null && !searchResultList.isEmpty()) {
				SearchResult videoFeed = searchResultList.get(0);
				videoInfo = new YouTubeVideoContentInfo();
				ResourceId videoId = videoFeed.getId();

				videoInfo.setId(videoId.getVideoId());
				return videoInfo;
			}

		} catch (MalformedURLException e) {
			Log.e("YouTubeSearch", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("YouTubeSearch", e.getMessage(), e);
		}
		return null;
	}

	public String queryURL(VideoContentInfo video) {
		String title = video.getTitle();
		String year = video.getYear();
		String show = video.getSeriesTitle();
		String season = video.getSeason();
		String episodeNum = video.getEpisodeNumber();
		String videoTitle;
		if (show == null) {
			videoTitle = "\"" + title + "\"" + TRAILER + " HD" + " " + year;
		} else {
			videoTitle = "\"" + show + "\" " + season + " " + episodeNum
					+ " Promo";
		}

		try {
			String encodedTitle = URLEncoder.encode(videoTitle, "UTF-8");

			String queryString = "https://www.googleapis.com/youtube/v3/search?part=id%2Csnippet&maxResults=1&order=relevance&q="
					+ encodedTitle + "key=" + API_KEY;
			return queryString;
		} catch (UnsupportedEncodingException ex) {

		}
		return null;
	}

}
