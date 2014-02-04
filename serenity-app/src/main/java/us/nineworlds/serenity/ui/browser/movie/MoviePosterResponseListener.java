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

package us.nineworlds.serenity.ui.browser.movie;


import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.widgets.SerenityGallery;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Response;
import com.jess.ui.TwoWayGridView;

/**
 * @author dcarver
 *
 */
public class MoviePosterResponseListener implements Response.Listener<MediaContainer> {

	protected ProgressDialog pd;
	protected List<VideoContentInfo> posterList;
	protected AbstractPosterImageGalleryAdapter adapter;
	protected SerenityMultiViewVideoActivity movieContext;
	/**
	 * 
	 */
	public MoviePosterResponseListener(ProgressDialog pd, List<VideoContentInfo> videos, AbstractPosterImageGalleryAdapter adapter, SerenityMultiViewVideoActivity context) {
		this.pd = pd;
		this.posterList = videos;
		this.adapter = adapter;
		this.movieContext = context;
	}

	@Override
	public void onResponse(MediaContainer response) {
		try {
			MediaContainer mc = response;
			populatePosters(mc);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Error populating posters.", e);
		}
		if (pd.isShowing()) {
			pd.dismiss();
		}
	}

	/**
	 * @param mc
	 */
	protected void populatePosters(MediaContainer mc) {
		MovieMediaContainer movies = new MovieMediaContainer(mc);
		posterList = movies.createVideos();
		adapter.notifyDataSetChanged();						
		if (!movieContext.isGridViewActive()) {
			SerenityGallery posterGallery = (SerenityGallery) movieContext
					.findViewById(R.id.moviePosterGallery);
			posterGallery.requestFocusFromTouch();
		} else {
			TwoWayGridView gridView = (TwoWayGridView) movieContext
					.findViewById(R.id.movieGridView);
			gridView.requestFocusFromTouch();
		}
	}

}
