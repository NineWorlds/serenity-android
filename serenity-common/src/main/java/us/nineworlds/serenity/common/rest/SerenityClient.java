package us.nineworlds.serenity.common.rest;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public interface SerenityClient {
  IMediaContainer retrieveRootData() throws Exception;

  IMediaContainer retrieveLibrary() throws Exception;

  IMediaContainer retrieveSections() throws Exception;

  IMediaContainer retrieveSections(String key) throws Exception;

  IMediaContainer retrieveSections(String key, String category) throws Exception;

  IMediaContainer retrieveSections(String key, String category, String secondaryCategory) throws Exception;

  IMediaContainer retrieveSeasons(String key) throws Exception;

  IMediaContainer retrieveMusicMetaData(String key) throws Exception;

  IMediaContainer retrieveEpisodes(String key) throws Exception;

  IMediaContainer retrieveMovieMetaData(String key) throws Exception;

  IMediaContainer searchMovies(String key, String query) throws Exception;

  IMediaContainer searchEpisodes(String key, String query) throws Exception;

  String baseURL();

  boolean setWatched(String key);

  boolean setUnWatched(String key);

  boolean setProgress(String key, String offset);

  String getMediaTagURL(String resourceType, String resourceName, String identifier);

  String getSectionsURL(String key, String category);

  String getSectionsURL();

  String getSectionsUrl(String key);

  String getMovieMetadataURL(String key);

  String getEpisodesURL(String key);

  String getSeasonsURL(String key);

  String getImageURL(String url, int width, int height);

  String getTranscodeUrl(String id, int offset);
}
