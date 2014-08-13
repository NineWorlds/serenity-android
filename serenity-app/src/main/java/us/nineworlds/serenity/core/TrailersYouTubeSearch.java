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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.util.Log;

public class TrailersYouTubeSearch {

	private static final String TRAILER = " Offical Trailer";

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
					+ encodedTitle
					+ "&key="
					+ SerenityConstants.YOUTUBE_SEARCH_API_KEY;
			return queryString;
		} catch (UnsupportedEncodingException ex) {
			Log.e(getClass().getName(), "Error encoding string.", ex);
		}
		return null;
	}

}
