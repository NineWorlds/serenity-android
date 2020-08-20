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
import java.util.List;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.common.rest.SerenityUser;

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
  @Override public IMediaContainer retrieveItemByCategories() throws Exception {
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
  @Override public IMediaContainer retrieveItemByIdCategory(String key) throws Exception {
    return client.retrieveSections(key);
  }

  /**
   * For Movies this will return a MediaContainer with Videos.  For
   * TV Shows this will return a MediaContainer with Directories.
   *
   * @return MediaContainer
   * @throws Exception
   */
  @Override public IMediaContainer retrieveItemByIdCategory(String key, String category) throws Exception {
    return client.retrieveSections(key, category);
  }

  @Override public IMediaContainer retrieveItemByCategories(String key, String category, String secondaryCategory)
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

  @Override public void updateBaseUrl(String baseUrl) {
    throw new UnsupportedOperationException("Multiple User Support is not available for Plex Servers");
  }

  @Override public String baseURL() {
    return client.baseURL();
  }

  /**
   * Sets a video as watched. viewCount will be 1.
   */
  @Override public boolean watched(String key) {
    try {
      return client.watched(key);
    } catch (IOException e) {
    }
    return false;
  }

  /**
   * Sets a vide as unwatched. viewCount will not be present.
   */
  @Override public boolean unwatched(String key) {
    try {
      return client.unwatched(key);
    } catch (IOException e) {
    }
    return false;
  }

  @Override public boolean progress(String key, String offset) {
    try {
      return client.progress(key, offset);
    } catch (IOException e) {
    }
    return false;
  }

  @Override public String createMediaTagURL(String resourceType, String resourceName, String identifier) {
    return client.createMediaTagURL(resourceType, resourceName, identifier);
  }

  @Override public String createSectionsURL(String key, String category) {
    return resourcePath.getSeasonsURL(key);
  }

  @Override public String createSectionsURL() {
    return resourcePath.getSectionsURL();
  }

  @Override public String createSectionsUrl(String key) {
    return resourcePath.getSectionsURL(key);
  }

  @Override public String createMovieMetadataURL(String key) {
    return resourcePath.getMovieMetaDataURL(key);
  }

  @Override public String createEpisodesURL(String key) {
    return resourcePath.getEpisodesURL(key);
  }

  @Override public String createSeasonsURL(String key) {
    return resourcePath.getSeasonsURL(key);
  }

  @Override public String createImageURL(String url, int width, int height) {
    return resourcePath.getImageURL(url, width, height);
  }

  @Override public String createTranscodeUrl(String id, int offset) {
    return resourcePath.getTranscoderVideoUrl(id, offset);
  }

  @Override public void reinitialize() {

  }

  @Override public SerenityUser userInfo(String userId) {
    throw new UnsupportedOperationException("Multiple User Support is not available for Plex Servers");
  }

  @Override public List<SerenityUser> allAvailableUsers() {
    throw new UnsupportedOperationException("Multiple User Support is not available for Plex Servers");
  }

  @Override public SerenityUser authenticateUser(SerenityUser user) {
    throw new UnsupportedOperationException("Multiple User Support is not available for Plex Servers");
  }

  @Override public String createUserImageUrl(SerenityUser user, int width, int height) {
    throw new UnsupportedOperationException("Multiple User Support is not available for Plex Servers.");
  }

  @Override public void startPlaying(String key) {
    // DO NOTHING as plex doesn't care when you start or stop
  }

  @Override public void stopPlaying(String key, long currentPosition) {
    // DO NOTHING as plex doesn't care when you start or stop
  }

  @Override public IMediaContainer retrieveSeriesById(String key, String categoryId) {
    try {
      return retrieveItemByIdCategory(key, categoryId);
    } catch (Exception ex) {
    }
    return null;
  }

  @Override public IMediaContainer retrieveSeriesCategoryById(String key) {
    try {
      return retrieveItemByIdCategory(key);
    } catch (Exception e) {
    }
    return null;
  }

  @Override
  public boolean supportsMultipleUsers() {
    return false;
  }
}
