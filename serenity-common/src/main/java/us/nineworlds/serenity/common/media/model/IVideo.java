package us.nineworlds.serenity.common.media.model;

import java.util.List;

public interface IVideo extends ClientObject {

  void setType(String type);
  String getType();

  String getGrandParentTitle();

  void setGrandParentTitle(String grandParentTitle);

  String getGrandParentThumbNailImageKey();

  void setGrandParentThumbNailImageKey(String grandParentThumbNailImageKey);

  List<IRole> getActors();

  String getBackgroundImageKey();

  String getContentRating();

  List<ICountry> getCountries();

  List<IDirector> getDirectors();

  long getDuration();

  List<IGenre> getGenres();

  List<IMedia> getMedias();

  String getOriginallyAvailableDate();

  String getSummary();

  String getTagLine();

  String getThumbNailImageKey();

  long getTimeAdded();

  long getTimeUpdated();

  String getTitle();

  String getTitleSort();

  int getViewCount();

  long getViewOffset();

  List<IWriter> getWriters();

  String getYear();

  void setActors(List<IRole> actors);

  void setBackgroundImageKey(String backgroundImageKey);

  void setContentRating(String contentRating);

  void setCountries(List<ICountry> countries);

  void setDirectors(List<IDirector> directors);

  void setDuration(long duration);

  void setGenres(List<IGenre> genres);

  void setMedias(List<IMedia> medias);

  void setOriginallyAvailableDate(String originallyAvailableDate);

  void setSummary(String summary);

  void setTagLine(String tagLine);

  void setThumbNailImageKey(String thumbNailImageKey);

  void setTimeAdded(long timeAdded);

  void setTimeUpdated(long timeUpdated);

  void setTitle(String title);

  void setTitleSort(String titleSort);

  void setViewCount(int viewCount);

  void setViewOffset(long viewOffset);

  void setWriters(List<IWriter> writers);

  void setYear(String year);

  String getRatingKey();

  void setRatingKey(String ratingKey);

  String getParentThumbNailImageKey();

  void setParentThumbNailImageKey(String parentThumbNailImageKey);

  String getStudio();

  void setStudio(String studio);

  double getRating();

  void setRating(double rating);

  void setParentKey(String parentKey);

  String getParentKey();

  String getEpisode();

  void setEpisode(String episode);

  String getSeason();

  void setSeason(String season);

  void setDirectPlayUrl(String url);

  String getDirectPlayUrl();

}
