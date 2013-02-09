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

package com.github.kingargyle.plexappclient.ui.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Director;
import com.github.kingargyle.plexapp.model.impl.Genre;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexapp.model.impl.Writer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.VideoContentInfo;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 * An abstract class for handling the creation of video content
 * for use during browsing.  Implementations need to implement the
 * abstract methods to provide functionality for retrieval and 
 * display of video content when browsing the episodes.
 * 
 * @author dcarver
 *
 */
public abstract class AbstractPosterImageGalleryAdapter extends BaseAdapter {

	protected List<VideoContentInfo> posterList = null;
	protected Activity context;
	protected ImageManager imageManager;
	protected ImageTagFactory imageTagFactory;
	protected static final int SIZE_HEIGHT = 400;
	protected static final int SIZE_WIDTH = 200;
	protected PlexappFactory factory;
	protected String key;
	protected String category;
	
	
	public AbstractPosterImageGalleryAdapter(Context c, String key) {
		context = (Activity)c;
		this.key = key;
		
		posterList = new ArrayList<VideoContentInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.newInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
		
	    Toast.makeText(context, "Retrieving Episodes", Toast.LENGTH_SHORT).show();
				
		createPosters();
	}

	public AbstractPosterImageGalleryAdapter(Context c, String key, String category) {
		context = (Activity)c;
		this.key = key;
		this.category = category;
		
		posterList = new ArrayList<VideoContentInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.newInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
		
	    Toast.makeText(context, "Retrieving Episodes", Toast.LENGTH_SHORT).show();
				
		createPosters();
	}

	
	
	
	protected void createPosters() {
		MediaContainer mc = null;
		try {
			factory = SerenityApplication.getPlexFactory();
			mc = retrieveVideos();
		} catch (IOException ex) {
 		    Toast.makeText(context, "Unable to comminicate with server at " + factory.baseURL() + ". Reason: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("AbstractPosterImageGalleryAdapter","Unable to talk to server: ", ex);
		} catch (Exception e) {
 		    Toast.makeText(context, "Unable to comminicate with server at " + factory.baseURL() + ". Reason: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("AbstractPosterImageGalleryAdapter","Oops.", e);
		}
		
		if (mc != null && mc.getSize() > 0) {
			createVideoContent(mc);
		}		
		
	}
	
	/**
	 * Populates the video content List with video or episode information.
	 * This information is later used by the video browser.
	 * 
	 * @param mc
	 */
	protected abstract void createVideoContent(MediaContainer mc);
	
	/**
	 * The videos to be retrieved. This typically retrieves all videos.
	 * 
	 * @return The container with all videos
	 * 
	 * @throws Exception
	 */
	protected abstract MediaContainer retrieveVideos() throws Exception;
	
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


	/**
	 * @param video
	 * @param videoContentInfo
	 * @return
	 */
	protected String createVideoDetails(Video video, VideoContentInfo videoContentInfo) {
		String videoDetails = "";
		
		if (video.getYear() != null) {
			videoContentInfo.setYear(video.getYear());
			videoDetails = "Year: " + video.getYear();
			videoDetails = videoDetails + "\r\n";
		}
		
		if (video.getGenres() != null && video.getGenres().size() > 0) {
			ArrayList<String> g = new ArrayList<String>();
			for (Genre genre : video.getGenres()) {
				g.add(genre.getTag());
				videoDetails = videoDetails + genre.getTag() + "/";
			}
			videoContentInfo.setGenres(g);
			videoDetails = videoDetails.substring(0, videoDetails.lastIndexOf("/"));
			videoDetails = videoDetails + "\r\n";
		}
		
		
		if (video.getWriters() != null && video.getWriters().size() > 0) {
			videoDetails = videoDetails + "Writer(s): ";
			ArrayList<String> w = new ArrayList<String>();
			for (Writer writer : video.getWriters()) {
				w.add(writer.getTag());
				videoDetails = videoDetails + writer.getTag() + ", ";
			}
			videoContentInfo.setWriters(w);
			videoDetails = videoDetails.substring(0, videoDetails.lastIndexOf(","));
			videoDetails = videoDetails + "\r\n";
		}
		
		if (video.getDirectors() != null && video.getDirectors().size() > 0) {
			videoDetails = videoDetails + "Director(s): ";
			ArrayList<String> d = new ArrayList<String>();
			for (Director director : video.getDirectors()) {
				d.add(director.getTag());
				videoDetails = videoDetails + director.getTag() + ", ";
			}
			videoContentInfo.setDirectors(d);
			videoDetails = videoDetails.substring(0, videoDetails.lastIndexOf(","));
			videoDetails = videoDetails + "\r\n";
		}
		return videoDetails;
	}
	
}
