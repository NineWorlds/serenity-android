package us.nineworlds.serenity.common.rest;

import java.io.IOException;
import java.util.List;
import us.nineworlds.serenity.common.media.model.IMediaContainer;

public interface SerenityClient {

  IMediaContainer retrieveRootData() throws Exception;

  IMediaContainer retrieveLibrary() throws Exception;

  IMediaContainer retrieveItemByCategories() throws Exception;

  IMediaContainer retrieveItemByIdCategory(String key) throws Exception;

  IMediaContainer retrieveItemByIdCategory(String key, String category) throws Exception;

  IMediaContainer retrieveItemByCategories(String key, String category, String secondaryCategory) throws Exception;

  IMediaContainer retrieveSeasons(String key) throws Exception;

  IMediaContainer retrieveMusicMetaData(String key) throws Exception;

  IMediaContainer retrieveEpisodes(String key) throws Exception;

  IMediaContainer retrieveMovieMetaData(String key) throws Exception;

  IMediaContainer searchMovies(String key, String query) throws Exception;

  IMediaContainer searchEpisodes(String key, String query) throws Exception;

  void updateBaseUrl(String baseUrl);

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

  SerenityUser userInfo(String userId);

  List<SerenityUser> allAvailableUsers();

  SerenityUser authenticateUser(SerenityUser user);

  String createUserImageUrl(SerenityUser user, int width, int height);

  void startPlaying(String key);

  void stopPlaying(String key);

  IMediaContainer retrieveSeriesById(String key, String categoryId);
}
