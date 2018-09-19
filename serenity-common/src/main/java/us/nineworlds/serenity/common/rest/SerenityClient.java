package us.nineworlds.serenity.common.rest;

import java.io.IOException;
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

  boolean watched(String key) throws IOException;

  boolean unwatched(String key) throws IOException;

  boolean progress(String key, String offset) throws IOException;

  String createMediaTagURL(String resourceType, String resourceName, String identifier);

  String createSectionsURL(String key, String category);

  String createSectionsURL();

  String createSectionsUrl(String key);

  String createMovieMetadataURL(String key);

  String createEpisodesURL(String key);

  String createSeasonsURL(String key);

  String createImageURL(String url, int width, int height);

  String createTranscodeUrl(String id, int offset);

  void reinitialize();
}
