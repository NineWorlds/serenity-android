/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Media;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Part;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.VideoContentInfo;
import com.github.kingargyle.plexappclient.core.model.impl.MoviePosterInfo;
import com.github.kingargyle.plexappclient.ui.adapters.AbstractPosterImageGalleryAdapter;
import com.github.kingargyle.plexappclient.ui.views.SerenityPosterImageView;
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

/**
 * 
 * @author dcarver
 *
 */
public class MoviePosterImageGalleryAdapter extends AbstractPosterImageGalleryAdapter {
	
	public MoviePosterImageGalleryAdapter(Context c, String key, String category) {
		super(c, key, category);
	}
		
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		VideoContentInfo pi = posterList.get(position);
		SerenityPosterImageView mpiv = new SerenityPosterImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL(), context));
		} else {
			mpiv.setTag(imageTagFactory.build(factory.baseURL() + ":/resources/movie-fanart.jpg", context));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(200, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		
		imageManager.getLoader().load(mpiv);
		
		return mpiv;
	}



	/* (non-Javadoc)
	 * @see com.github.kingargyle.plexappclient.ui.adapters.AbstractPosterImageGalleryAdapter#createVideoContent(com.github.kingargyle.plexapp.model.impl.MediaContainer)
	 */
	@Override
	protected void createVideoContent(MediaContainer mc) {
		String baseUrl = factory.baseURL();
		List<Video> videos = mc.getVideos();
		for (Video movie : videos) {
			VideoContentInfo  mpi = new MoviePosterInfo();
			mpi.setPlotSummary(movie.getSummary());
			
			String burl = baseUrl + ":/resources/movie-fanart.jpg"; 
			if (movie.getBackgroundImageKey() != null) {
				burl = baseUrl + movie.getBackgroundImageKey().replaceFirst("/", "");
			}
			mpi.setBackgroundURL(burl);
			
			String turl = "";
			if (movie.getThumbNailImageKey() != null) {
				turl = baseUrl + movie.getThumbNailImageKey().replaceFirst("/", "");
			}
			
			mpi.setPosterURL(turl);
			mpi.setTitle(movie.getTitle());
			
			mpi.setContentRating(movie.getContentRating());
	
			List<Media> mediacont = movie.getMedias();
			if (mediacont != null && !mediacont.isEmpty()) {
				// We grab the first media container until we know more about why there can be multiples.
				Media media = mediacont.get(0);
				List<Part> parts = media.getVideoPart();
				Part part = parts.get(0);
				mpi.setAudioCodec(media.getAudioCodec());
				mpi.setVideoCodec(media.getVideoCodec());
				mpi.setVideoResolution(media.getVideoResolution());
				mpi.setAspectRatio(media.getAspectRatio());
				
				String directPlayUrl = factory.baseURL() + part.getKey().replaceFirst("/", "");
				mpi.setDirectPlayUrl(directPlayUrl);
				
			}
			
			String movieDetails = createVideoDetails(movie, mpi);	
			mpi.setCastInfo(movieDetails);				
		
			posterList.add(mpi);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.github.kingargyle.plexappclient.ui.adapters.AbstractPosterImageGalleryAdapter#retrieveVideos()
	 */
	@Override
	protected MediaContainer retrieveVideos() throws Exception {
		if (category == null) {
			category = "all";
		}
		return factory.retrieveSections(key, category);
	}

}
