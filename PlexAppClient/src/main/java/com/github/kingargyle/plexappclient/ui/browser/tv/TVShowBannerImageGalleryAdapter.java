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

package com.github.kingargyle.plexappclient.ui.browser.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Director;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.Genre;
import com.github.kingargyle.plexapp.model.impl.Media;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexapp.model.impl.Writer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.imagecache.PlexAppImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioGroup.LayoutParams;

/**
 * 
 * @author dcarver
 *
 */
public class TVShowBannerImageGalleryAdapter extends BaseAdapter {
	
	private List<TVShowBannerInfo> tvShowList = null;
	private Activity context;
	
	private PlexAppImageManager imageManager;
	private ImageTagFactory imageTagFactory;
	private static final int SIZE_HEIGHT = 200;
	private static final int SIZE_WIDTH = 400;
	private PlexappFactory factory;
	
	public TVShowBannerImageGalleryAdapter(Context c) {
		context = (Activity)c;
		
		tvShowList = new ArrayList<TVShowBannerInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.getInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
		
		createPosters();
	}
	
	/**
	 * This all needs to go into an AsyncTask and be done in the background.
	 */
	private void createPosters() {
		
		
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			factory = SerenityApplication.getPlexFactory();
			String key = context.getIntent().getExtras().getString("key");
			
			// For TV Shows we'll get a Directory container.
			mc = factory.retrieveSections(key, "all");
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
			Log.w("Unable to talk to server: ", ex);
		} catch (Exception e) {
			Log.w("Oops.", e);
		}
		
		if (mc.getSize() > 0) {
			List<Directory> shows = mc.getDirectories();
			for (Directory show : shows) {
				TVShowBannerInfo  mpi = new TVShowBannerInfo();
				mpi.setPlotSummary(show.getSummary());
				
				String burl = factory.baseURL() + ":/resources/show-fanart.jpg"; 
				if (show.getArt() != null) {
					burl = baseUrl + show.getArt().replaceFirst("/", "");
				}
				mpi.setBackgroundURL(burl);
				
				String turl = "";
				if (show.getBanner() != null) {
					turl = baseUrl + show.getBanner().replaceFirst("/", "");
				}
				mpi.setPosterURL(turl);
				
				String thumbURL = "";
				if (show.getBanner() != null) {
					thumbURL = baseUrl + show.getThumb().replaceFirst("/", "");
				}
				mpi.setThumbNailURL(thumbURL);
				
				mpi.setTitle(show.getTitle());
				
				mpi.setContentRating(show.getContentRating());
								
			
				tvShowList.add(mpi);
			}
		}		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		
		return tvShowList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		
		return tvShowList.get(position);
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
		
		TVShowBannerInfo pi = tvShowList.get(position);
		TVShowBannerImageView mpiv = new TVShowBannerImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL()));
		} else {
			mpiv.setTag(imageTagFactory.build(factory.baseURL() + ":/resources/show-fanart.jpg"));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = 768;
		int height = LayoutParams.MATCH_PARENT;
		mpiv.setLayoutParams(new Gallery.LayoutParams(width, height));
		
		imageManager.getLoader().load((ImageView) mpiv);
		return mpiv;
	}

}
