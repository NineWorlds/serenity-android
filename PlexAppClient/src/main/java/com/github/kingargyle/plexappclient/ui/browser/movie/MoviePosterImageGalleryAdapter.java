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
import com.github.kingargyle.plexapp.model.impl.Director;
import com.github.kingargyle.plexapp.model.impl.Genre;
import com.github.kingargyle.plexapp.model.impl.Media;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexapp.model.impl.Writer;
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
 * 
 * @author dcarver
 *
 */
public class MoviePosterImageGalleryAdapter extends BaseAdapter {
	
	private List<MoviePosterInfo> posterList = null;
	private Context context;
	
	private PlexAppImageManager imageManager;
	private ImageTagFactory imageTagFactory;
	private static final int SIZE_HEIGHT = 400;
	private static final int SIZE_WIDTH = 200;
	private PlexappFactory factory;
	
	public MoviePosterImageGalleryAdapter(Context c) {
		context = c;
		
		posterList = new ArrayList<MoviePosterInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.getInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
		//imageTagFactory.setUseOnlyCache(false);
		
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
				mpi.setTitle(movie.getTitle());
				
				mpi.setContentRating(movie.getContentRating());
				
				Media media = movie.getMedia();
				mpi.setAudioCodec(media.getAudioCodec());
				mpi.setVideoCodec(media.getVideoCodec());
				mpi.setVideoResolution(media.getVideoResolution());
				
				String movieDetails = "";
				
				if (movie.getYear() != null) {
					mpi.setYear(movie.getYear());
					movieDetails = "Year: " + movie.getYear();
					movieDetails = movieDetails + "\r\n";
				}
				
				if (movie.getGenres() != null && movie.getGenres().size() > 0) {
					ArrayList<String> g = new ArrayList<String>();
					for (Genre genre : movie.getGenres()) {
						g.add(genre.getTag());
						movieDetails = movieDetails + genre.getTag() + "/";
					}
					mpi.setGenres(g);
					movieDetails = movieDetails.substring(0, movieDetails.lastIndexOf("/"));
					movieDetails = movieDetails + "\r\n";
				}
				
				
				if (movie.getWriters() != null && movie.getWriters().size() > 0) {
					movieDetails = movieDetails + "Writer(s): ";
					ArrayList<String> w = new ArrayList<String>();
					for (Writer writer : movie.getWriters()) {
						w.add(writer.getTag());
						movieDetails = movieDetails + writer.getTag() + ", ";
					}
					mpi.setWriters(w);
					movieDetails = movieDetails.substring(0, movieDetails.lastIndexOf(","));
					movieDetails = movieDetails + "\r\n";
				}
				
				if (movie.getDirectors() != null && movie.getDirectors().size() > 0) {
					movieDetails = movieDetails + "Director(s): ";
					ArrayList<String> d = new ArrayList<String>();
					for (Director director : movie.getDirectors()) {
						d.add(director.getTag());
						movieDetails = movieDetails + director.getTag() + ", ";
					}
					mpi.setDirectors(d);
					movieDetails = movieDetails.substring(0, movieDetails.lastIndexOf(","));
					movieDetails = movieDetails + "\r\n";
				}
				
				mpi.setCastInfo(movieDetails);
			
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
			mpiv.setTag(imageTagFactory.build(factory.baseURL() + ":/resources/movie-fanart.jpg"));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(150, LayoutParams.WRAP_CONTENT));
		
		imageManager.getLoader().load((ImageView) mpiv);
		return mpiv;
	}

}
