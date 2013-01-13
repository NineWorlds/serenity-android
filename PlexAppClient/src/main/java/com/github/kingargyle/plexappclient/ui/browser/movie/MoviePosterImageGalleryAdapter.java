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

package com.github.kingargyle.plexappclient.ui.browser.movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.Config;
import com.github.kingargyle.plexappclient.core.imagecache.PlexAppImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioGroup.LayoutParams;

/**
 * @author dcarver
 *
 */
public class MoviePosterImageGalleryAdapter extends BaseAdapter {
	
	private List<MoviePosterInfo> posterList = null;
	private Context context;
	
	private PlexAppImageManager imageManager;
	private ImageTagFactory imageTagFactory;
	private static final int SIZE = 400;
	private PlexappFactory factory;
	
	public MoviePosterImageGalleryAdapter(Context c) {
		context = c;
		
		posterList = new ArrayList<MoviePosterInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.getInstance(SIZE, SIZE, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
		
		createPosters();
	}
	
	private void createPosters() {
		
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			factory = PlexappFactory.getInstance(Config.getInstance());
			mc = factory.retrieveSections("4", "all");
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mc.getSize() > 0) {
			List<Video> videos = mc.getVideos();
			for (Video movie : videos) {
				MoviePosterInfo  mpi = new MoviePosterInfo();
				mpi.setPlotSummary(movie.getSummary());
				
				String burl = factory.baseURL() + ":/resources/movie-fanart.jpg"; 
				if (movie.getBackgroundImageKey() != null) {
					burl = baseUrl + movie.getBackgroundImageKey().replaceFirst("/", "");
				}
				mpi.setBackgroundURL(burl);
				
				String turl = "";
				if (movie.getThumbNailImageKey() != null) {
					turl = baseUrl + movie.getThumbNailImageKey().replaceFirst("/", "");
				}
				
				mpi.setPosterURL(turl);
				mpi.setCastInfo("To be completed");
				mpi.setTitle(movie.getTitle());
				posterList.add(mpi);
			}
		}		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		
		return posterList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		
		return posterList.get(position);
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
		
		MoviePosterInfo pi = posterList.get(position);
		MoviePosterImageView mpiv = new MoviePosterImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL()));
		} else {
			mpiv.setTag(imageTagFactory.build("http://192.168.0.108:32400/:/resources/movie-fanart.jpg"));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(150, LayoutParams.WRAP_CONTENT));
		
		imageManager.getLoader().load((ImageView) mpiv);
		return mpiv;
	}

}
