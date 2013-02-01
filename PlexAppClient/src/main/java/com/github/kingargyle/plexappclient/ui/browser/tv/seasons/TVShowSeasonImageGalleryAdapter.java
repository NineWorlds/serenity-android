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

package com.github.kingargyle.plexappclient.ui.browser.tv.seasons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.SeriesContentInfo;
import com.github.kingargyle.plexappclient.core.model.impl.TVShowSeriesInfo;
import com.github.kingargyle.plexappclient.ui.views.TVShowSeasonImageView;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

/**
 * 
 * @author dcarver
 *
 */
public class TVShowSeasonImageGalleryAdapter extends BaseAdapter {
	
	private List<TVShowSeriesInfo> seasonList = null;
	private Activity context;
	
	private ImageManager imageManager;
	private ImageTagFactory imageTagFactory;
	private static final int SIZE_HEIGHT = 200;
	private static final int SIZE_WIDTH = 400;
	private PlexappFactory factory;
	
	public TVShowSeasonImageGalleryAdapter(Context c) {
		context = (Activity)c;
		
		seasonList = new ArrayList<TVShowSeriesInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.newInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
	    Toast.makeText(context, "Retrieving Seasons Information", Toast.LENGTH_SHORT).show();
		
		createImages();
	}
	
	/**
	 * This all needs to go into an AsyncTask and be done in the background.
	 */
	private void createImages() {
		
		
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			factory = SerenityApplication.getPlexFactory();
			String key = context.getIntent().getExtras().getString("key");
			
			// For TV Shows we'll get a Directory container.
			mc = factory.retrieveSeasons(key);
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
 		    Toast.makeText(context, "Unable to comminicate with server at " + factory.baseURL(), Toast.LENGTH_SHORT).show();
			Log.w("Unable to talk to server: ", ex);
		} catch (Exception e) {
 		    Toast.makeText(context, "Unable to comminicate with server at " + factory.baseURL(), Toast.LENGTH_SHORT).show();			
			Log.w("Oops.", e);
		}
		
		if (mc != null && mc.getSize() > 0) {
			TextView itemCount = (TextView) context.findViewById(R.id.tvShowSeasonsItemCount);
			itemCount.setText(Integer.toString(mc.getSize()) + " Item(s)");

			TextView titleView = (TextView) context.findViewById(R.id.tvShowSeasonsDetailText);
			
			if (mc.getTitle2() != null) {
				titleView.setText(mc.getTitle2());
			}
			
			List<Directory> shows = mc.getDirectories();
			for (Directory show : shows) {
				TVShowSeriesInfo  mpi = new TVShowSeriesInfo();
				
				String burl = factory.baseURL() + ":/resources/show-fanart.jpg"; 
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
			    
				seasonList.add(mpi);
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		
		return seasonList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		
		return seasonList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SeriesContentInfo pi = seasonList.get(position);
		TVShowSeasonImageView mpiv = new TVShowSeasonImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL(), context));
		} else {
			mpiv.setTag(imageTagFactory.build(factory.baseURL() + ":/resources/show-fanart.jpg", context));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = 200;
		int height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		mpiv.setLayoutParams(new Gallery.LayoutParams(width, height));
		
		imageManager.getLoader().load(mpiv);
		return mpiv;
	}
	
	

}
