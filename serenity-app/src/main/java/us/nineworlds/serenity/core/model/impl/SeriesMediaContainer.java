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

package us.nineworlds.serenity.core.model.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.Genre;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

/**
 * @author dcarver
 *
 */
public class SeriesMediaContainer extends AbstractMediaContainer {

	protected List<SeriesContentInfo> videoList;
	/**
	 * @param mc
	 */
	public SeriesMediaContainer(MediaContainer mc) {
		super(mc);
	}
	
	public List<SeriesContentInfo> createSeries() {
		videoList = new LinkedList<SeriesContentInfo>();
		createSeriesInfo();
		return videoList;
	}
	
	protected void createSeriesInfo() {
		String baseUrl = factory.baseURL();

		if (mc != null && mc.getSize() > 0) {
			String mediaTagId = Long.valueOf(mc.getMediaTagVersion()).toString();
			List<Directory> shows = mc.getDirectories();
			if (shows != null) {
				for (Directory show : shows) {
					TVShowSeriesInfo mpi = new TVShowSeriesInfo();
					mpi.setId(show.getRatingKey());
					mpi.setMediaTagIdentifier(mediaTagId);
					if (show.getSummary() != null) {
						mpi.setSummary(show.getSummary());
					}
					
					mpi.setStudio(show.getStudio());
					if (show.getRating() != null) {
						mpi.setRating(Double.parseDouble(show.getRating()));
					} else {
						mpi.setRating(0d);
					}

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
					mpi.setImageURL(turl);

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

					videoList.add(mpi);
				}
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
