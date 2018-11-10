package us.nineworlds.serenity.common.media.model;

import java.util.List;

public interface IDirectory extends ClientObject {
  String getThumb();

  void setThumb(String thumb);

  String getBanner();

  void setBanner(String banner);

  int getSecondary();

  void setSecondary(int secondary);

  List<IGenre> getGenres();

  void setGenres(List<IGenre> genres);

  String getRatingKey();

  void setRatingKey(String ratingKey);

  String getStudio();

  void setStudio(String studio);

  String getRating();

  void setRating(String rating);

  String getYear();

  void setYear(String year);

  String getContentRating();

  void setContentRating(String contentRating);

  String getSummary();

  void setSummary(String summary);

  String getLeafCount();

  void setLeafCount(String leafCount);

  String getViewedLeafCount();

  void setViewedLeafCount(String viewedLeafCount);

  String getTitle();

  void setTitle(String title);

  String getArt();

  void setArt(String art);

  int getRefreshing();

  void setRefreshing(int refreshing);

  String getType();

  void setType(String type);

  String getAgent();

  void setAgent(String agent);

  String getScanner();

  void setScanner(String scanner);

  String getLanguage();

  void setLanguage(String language);

  String getUuid();

  void setUuid(String uuid);

  long getUpdatedAt();

  void setUpdatedAt(long updatedAt);

  long getCreatedAt();

  void setCreatedAt(long createdAt);

  List<ILocation> getLocations();

  void setLocation(List<ILocation> location);

  String getPrompt();

  void setPrompt(String prompt);

  String getSearch();

  void setSearch(String search);

  void setLocations(List<ILocation> locations);
}
