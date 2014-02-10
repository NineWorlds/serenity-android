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

import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

/**
 * @author dcarver
 * 
 */
public class SeasonsMediaContainer extends SeriesMediaContainer {

	/**
	 * @param mc
	 */
	public SeasonsMediaContainer(MediaContainer mc) {
		super(mc);
	}

	@Override
	protected void createSeriesInfo() {
		String baseUrl = factory.baseURL();
		List<Directory> shows = mc.getDirectories();
		if (shows == null) {
			return;
		}
		for (Directory show : shows) {
			SeriesContentInfo mpi = new TVShowSeriesInfo();
			mpi.setId(show.getRatingKey());
			if (mc.getTitle2() != null) {
				mpi.setParentTitle(mc.getTitle2());
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
			mpi.setImageURL(turl);
			mpi.setKey(show.getKey());

			mpi.setTitle(show.getTitle());

			mpi.setShowsWatched(show.getViewedLeafCount());
			int totalEpisodes = Integer.parseInt(show.getLeafCount());
			int unwatched = totalEpisodes
					- Integer.parseInt(show.getViewedLeafCount());
			mpi.setShowsUnwatched(Integer.toString(unwatched));

			videoList.add(mpi);
		}

	}

}
