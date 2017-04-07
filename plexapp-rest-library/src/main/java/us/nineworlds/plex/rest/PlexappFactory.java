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

package us.nineworlds.plex.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.plex.rest.model.impl.MediaContainer;


/**
 * This class acts as a factory for retrieving items from Plex.
 * 
 * This is a singleton so only one of these will ever exist currently.
 * 
 * @author dcarver
 * 
 */
public class PlexappFactory {

	private static PlexappFactory instance = null;

	private PlexappClient client;

	private static ResourcePaths resourcePath;

	private PlexappFactory(IConfiguration config) {
		client = PlexappClient.getInstance(config);
	}

	public static PlexappFactory getInstance(IConfiguration config) {
		if (instance == null) {
			instance = new PlexappFactory(config);
		}
		resourcePath = new ResourcePaths(config);
		return instance;
	}

	/**
	 * Retrieve the root metadata from the Plex Media Server.
	 * 
	 * @return 
	 * @throws Exception
	 */
	public MediaContainer retrieveRootData() throws Exception {
		return client.retrieveRootData();
	}

	/**
	 * This retrieves the available libraries.  This can include such
	 * things as Movies, and TV shows.
	 * 
	 * @return MediaContainer the media container for the library
	 * @throws Exception
	 */
	public MediaContainer retrieveLibrary() throws Exception {
		return client.retrieveLibrary();
	}
	
	/**
	 * This retrieves the available libraries.  This can include such
	 * things as Movies, and TV shows.
	 * 
	 * @return MediaContainer the media container for the library
	 * @throws Exception
	 */
	public MediaContainer retrieveSections() throws Exception {
		return client.retrieveSections();
	}
	
	/**
	 * This retrieves the available libraries.  This can include such
	 * things as Movies, and TV shows.
	 * 
	 * @return MediaContainer the media container for the library
	 * @param key the section key
	 * @throws Exception
	 */
	public MediaContainer retrieveSections(String key) throws Exception {
		return client.retrieveSections(key);
	}
	
	/**
	 * For Movies this will return a MediaContainer with Videos.  For
	 * TV Shows this will return a MediaContainer with Directories.
	 * 
	 * @param key
	 * @param category
	 * @return MediaContainer
	 * @throws Exception
	 */
	public MediaContainer retrieveSections(String key, String category) throws Exception {
		return client.retrieveSections(key, category);
	}
	
	public MediaContainer retrieveSections(String key, String category, String secondaryCategory) throws Exception {
		return client.retrieveSections(key, category, secondaryCategory);
	}
	
	
	public MediaContainer retrieveSeasons(String key) throws Exception {
		return client.retrieveSeasons(key);
	}
	
	public MediaContainer retrieveMusicMetaData(String key) throws Exception {
		return client.retrieveMusicMetaData(key);
	}
	
	
	public MediaContainer retrieveEpisodes(String key) throws Exception {
		return client.retrieveEpisodes(key);
	}
	
	public MediaContainer retrieveMovieMetaData(String key) throws Exception {
		return client.retrieveMovieMetaData(key);
	}
		
	public MediaContainer searchMovies(String key, String query) throws Exception {
		return client.searchMovies(key, query);
	}
	
	public MediaContainer searchEpisodes(String key, String query) throws Exception {
		return client.searchEpisodes(key, query);
	}
	
	public String baseURL() {
		return client.baseURL();
	}

	/**
	 * Sets a video as watched. viewCount will be 1.
	 * @param key
	 * @return
	 */
	public boolean setWatched(String key)  {
		try {
			return client.setWatched(key);
		} catch (IOException e) {
		}
		return false;
	}
	
	/**
	 * Sets a vide as unwatched. viewCount will not be present.
	 * 
	 * @param key
	 * @return
	 */
	public boolean setUnWatched(String key) {
		try {
			return client.setUnWatched(key);
		} catch (IOException e) {
		}
		return false;
	}
	
	public boolean setProgress(String key, String offset) {
		try {
			return client.setProgress(key, offset);
		} catch (IOException e) {
		}
		return false;
	}
	

	public String getMediaTagURL(String resourceType, String resourceName, String identifier) {
		return client.getMediaTagURL(resourceType, resourceName, identifier);
	}
	
	public String getSectionsURL(String key, String category) {
		return client.getSeasonsURL(key);
	}

	public String getSectionsURL() {
		return resourcePath.getSectionsURL();
	}

	public String getSectionsUrl(String key) {
		return resourcePath.getSectionsURL(key);
	}

	public String getMovieMetadataURL(String key) {
		return resourcePath.getMovieMetaDataURL(key);
	}

	public String getEpisodesURL(String key) {
		return resourcePath.getEpisodesURL(key);
	}

	public String getSeasonsURL(String key) {
		return resourcePath.getSeasonsURL(key);
	}


	public String getImageURL(String url, int width, int height) {
        return client.getImageURL(url, width, height);
    }

}
