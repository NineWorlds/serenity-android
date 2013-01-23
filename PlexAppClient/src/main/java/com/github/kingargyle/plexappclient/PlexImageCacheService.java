package com.github.kingargyle.plexappclient;
import java.io.IOException;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.util.Log;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.novoda.imageloader.core.ImageManager;

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

/**
 * @author dcarver
 * 
 */
public class PlexImageCacheService {

	private final ExecutorService pool;
	

	public PlexImageCacheService(int poolSize) throws IOException {
		pool = Executors.newFixedThreadPool(poolSize);
	}
	
	public void execute() {
		pool.execute(new PlexImageFetcherHandler());
	}
	
	private class PlexImageFetcherHandler implements Runnable {

		private PlexappFactory factory;
		private ImageManager imageManager;
		
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				imageManager = SerenityApplication.getImageManager();
				
				factory = SerenityApplication.getPlexFactory();
				
				MediaContainer sections = factory.retrieveSections();
				List<Directory> dirs =  sections.getDirectories();
				for (Directory d : dirs) {
					if ("movie".equals(d.getType())) {
						String key = d.getKey();
						MediaContainer movieContainer = factory.retrieveSections(key, "unwatched");
						List<Video> movies = movieContainer.getVideos();
						
						for(Video movie : movies) {
							String baseUrl = factory.baseURL();
							 
							String movieBackgroundImageKey = movie.getBackgroundImageKey();
							if (movieBackgroundImageKey != null) {
								String imageURL = baseUrl + movieBackgroundImageKey.replaceFirst("/", "");
								imageManager.cacheImage(imageURL, 1280, 720);
							}
							
							if (movie.getThumbNailImageKey() != null) {
								String imageURL = baseUrl + movie.getThumbNailImageKey().replaceFirst("/", "");
								imageManager.cacheImage(imageURL, 200, 400);
							}
						}
					}
				}
			} catch (Exception e) {
				Log.w("Can't communicate with server.", e);
			}
			
		}
	}
	
}
