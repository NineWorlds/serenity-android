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
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;

/**
 * This class acts as a factory for retrieving items from Plex.
 *
 * This is a singleton so only one of these will ever exist currently.
 *
 * @author dcarver
 */
public class PlexappFactory implements SerenityClient {

  private static SerenityClient instance = null;

  private PlexappClient client;

  private static ResourcePaths resourcePath;

  private PlexappFactory(IConfiguration config) {
    client = PlexappClient.getInstance(config);
  }

  public static SerenityClient getInstance(IConfiguration config) {
    if (instance == null) {
      instance = new PlexappFactory(config);
    }
    resourcePath = new ResourcePaths(config);
    return instance;
  }

  /**
   * Retrieve the root metadata from the Plex Media Server.
   *
   * @throws Exception
   */
  @Override public IMediaContainer retrieveRootData() throws Exception {
    return client.retrieveRootData();
  }

  /**
   * This retrieves the available libraries.  This can include such
   * things as Movies, and TV shows.
   *
   * @return MediaContainer the media container for the library
   * @throws Exception
   */
  @Override public IMediaContainer retrieveLibrary() throws Exception {
    return client.retrieveLibrary();
  }

  /**
   * This retrieves the available libraries.  This can include such
   * things as Movies, and TV shows.
   *
   * @return MediaContainer the media container for the library
   * @throws Exception
   */
  @Override public IMediaContainer retrieveSections() throws Exception {
    return client.retrieveSections();
  }

  /**
   * This retrieves the available libraries.  This can include such
   * things as Movies, and TV shows.
   *
   * @param key the section key
   * @return MediaContainer the media container for the library
   * @throws Exception
   */
  @Override public IMediaContainer retrieveSections(String key) throws Exception {
    return client.retrieveSections(key);
  }

  /**
   * For Movies this will return a MediaContainer with Videos.  For
   * TV Shows this will return a MediaContainer with Directories.
   *
   * @return MediaContainer
   * @throws Exception
   */
  @Override public IMediaContainer retrieveSections(String key, String category) throws Exception {
    return client.retrieveSections(key, category);
  }

  @Override public IMediaContainer retrieveSections(String key, String category, String secondaryCategory)
      throws Exception {
    return client.retrieveSections(key, category, secondaryCategory);
  }

  @Override public IMediaContainer retrieveSeasons(String key) throws Exception {
    return client.retrieveSeasons(key);
  }

  @Override public IMediaContainer retrieveMusicMetaData(String key) throws Exception {
    return client.retrieveMusicMetaData(key);
  }

  @Override public IMediaContainer retrieveEpisodes(String key) throws Exception {
    return client.retrieveEpisodes(key);
  }

  @Override public IMediaContainer retrieveMovieMetaData(String key) throws Exception {
    return client.retrieveMovieMetaData(key);
  }

  @Override public IMediaContainer searchMovies(String key, String query) throws Exception {
    return client.searchMovies(key, query);
  }

  @Override public IMediaContainer searchEpisodes(String key, String query) throws Exception {
    return client.searchEpisodes(key, query);
  }

  @Override public String baseURL() {
    return client.baseURL();
  }

  /**
   * Sets a video as watched. viewCount will be 1.
   */
  @Override public boolean setWatched(String key) {
    try {
      return client.setWatched(key);
    } catch (IOException e) {
    }
    return false;
  }

  /**
   * Sets a vide as unwatched. viewCount will not be present.
   */
  @Override public boolean setUnWatched(String key) {
    try {
      return client.setUnWatched(key);
    } catch (IOException e) {
    }
    return false;
  }

  @Override public boolean setProgress(String key, String offset) {
    try {
      return client.setProgress(key, offset);
    } catch (IOException e) {
    }
    return false;
  }

  @Override public String getMediaTagURL(String resourceType, String resourceName, String identifier) {
    return client.getMediaTagURL(resourceType, resourceName, identifier);
  }

  @Override public String getSectionsURL(String key, String category) {
    return client.getSeasonsURL(key);
  }

  @Override public String getSectionsURL() {
    return resourcePath.getSectionsURL();
  }

  @Override public String getSectionsUrl(String key) {
    return resourcePath.getSectionsURL(key);
  }

  @Override public String getMovieMetadataURL(String key) {
    return resourcePath.getMovieMetaDataURL(key);
  }

  @Override public String getEpisodesURL(String key) {
    return resourcePath.getEpisodesURL(key);
  }

  @Override public String getSeasonsURL(String key) {
    return resourcePath.getSeasonsURL(key);
  }

  @Override public String getImageURL(String url, int width, int height) {
    return client.getImageURL(url, width, height);
  }

  @Override public String getTranscodeUrl(String id, int offset) {
    return resourcePath.getTranscoderVideoUrl(id, offset);
  }
}
