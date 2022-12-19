package us.nineworlds.serenity.emby.model;

import java.util.List;
import us.nineworlds.serenity.common.media.model.ICountry;
import us.nineworlds.serenity.common.media.model.IDirector;
import us.nineworlds.serenity.common.media.model.IGenre;
import us.nineworlds.serenity.common.media.model.IMedia;
import us.nineworlds.serenity.common.media.model.IRole;
import us.nineworlds.serenity.common.media.model.IVideo;
import us.nineworlds.serenity.common.media.model.IWriter;

public class Video extends AbstractEmbyObject implements IVideo {

  private String type;
  private String studio;
  private String summary;
  private String titleSort;
  private String title;
  private int viewCount;
  private String tagLine;
  private long viewOffset;
  private String thumbNailImageKey;
  private String backgroundImageKey;
  private String parentThumbNailImageKey;
  private String grandParentThumbNailImageKey;
  private String grandParentTitle;
  private long duration;
  private long timeAdded;
  private long timeUpdated;

  /**
   * Formatted date item was originally available in YYYY-MM-DD format.
   */
  private String originallyAvailableDate;
  private String contentRating;
  private String year;
  private String ratingKey;
  private String parentKey;
  private String episode;
  private String season;
  private double rating;
  private List<ICountry> countries;
  private List<IDirector> directors;
  private List<IRole> actors;
  private List<IWriter> writers;
  private List<IGenre> genres;
  private List<IMedia> medias;
  private String directPlayUrl;
  private String seriesName;

  @Override
  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
  }

  @Override
  public String getSeriesName() {
    return seriesName;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override public String getGrandParentTitle() {
    return grandParentTitle;
  }

  @Override public void setGrandParentTitle(String grandParentTitle) {
    this.grandParentTitle = grandParentTitle;
  }

  @Override public String getGrandParentThumbNailImageKey() {
    return grandParentThumbNailImageKey;
  }

  @Override public void setGrandParentThumbNailImageKey(String grandParentThumbNailImageKey) {
    this.grandParentThumbNailImageKey = grandParentThumbNailImageKey;
  }

  @Override public List<IRole> getActors() {
    return actors;
  }

  @Override public String getBackgroundImageKey() {
    return backgroundImageKey;
  }

  @Override public String getContentRating() {
    return contentRating;
  }

  @Override public List<ICountry> getCountries() {
    return countries;
  }

  @Override public List<IDirector> getDirectors() {
    return directors;
  }

  @Override public long getDuration() {
    return duration;
  }

  @Override public List<IGenre> getGenres() {
    return genres;
  }

  @Override public List<IMedia> getMedias() {
    return medias;
  }

  @Override public String getOriginallyAvailableDate() {
    return originallyAvailableDate;
  }

  @Override public String getSummary() {
    return summary;
  }

  @Override public String getTagLine() {
    return tagLine;
  }

  @Override public String getThumbNailImageKey() {
    return thumbNailImageKey;
  }

  @Override public long getTimeAdded() {
    return timeAdded;
  }

  @Override public long getTimeUpdated() {
    return timeUpdated;
  }

  @Override public String getTitle() {
    return title;
  }

  @Override public String getTitleSort() {
    return titleSort;
  }

  @Override public int getViewCount() {
    return viewCount;
  }

  @Override public long getViewOffset() {
    return viewOffset;
  }

  @Override public List<IWriter> getWriters() {
    return writers;
  }

  @Override public String getYear() {
    return year;
  }

  @Override public void setActors(List<IRole> actors) {
    this.actors = actors;
  }

  @Override public void setBackgroundImageKey(String backgroundImageKey) {
    this.backgroundImageKey = backgroundImageKey;
  }

  @Override public void setContentRating(String contentRating) {
    this.contentRating = contentRating;
  }

  @Override public void setCountries(List<ICountry> countries) {
    this.countries = countries;
  }

  @Override public void setDirectors(List<IDirector> directors) {
    this.directors = directors;
  }

  @Override public void setDuration(long duration) {
    this.duration = duration;
  }

  @Override public void setGenres(List<IGenre> genres) {
    this.genres = genres;
  }

  @Override public void setMedias(List<IMedia> medias) {
    this.medias = medias;
  }

  /**
   * This needs to be formatted in YYYY-MM-DD format.
   *
   * @param originallyAvailableDate the originallyAvailableDate to set
   */
  @Override public void setOriginallyAvailableDate(String originallyAvailableDate) {
    this.originallyAvailableDate = originallyAvailableDate;
  }

  @Override public void setSummary(String summary) {
    this.summary = summary;
  }

  @Override public void setTagLine(String tagLine) {
    this.tagLine = tagLine;
  }

  @Override public void setThumbNailImageKey(String thumbNailImageKey) {
    this.thumbNailImageKey = thumbNailImageKey;
  }

  @Override public void setTimeAdded(long timeAdded) {
    this.timeAdded = timeAdded;
  }

  @Override public void setTimeUpdated(long timeUpdated) {
    this.timeUpdated = timeUpdated;
  }

  @Override public void setTitle(String title) {
    this.title = title;
  }

  @Override public void setTitleSort(String titleSort) {
    this.titleSort = titleSort;
  }

  @Override public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
  }

  @Override public void setViewOffset(long viewOffset) {
    this.viewOffset = viewOffset;
  }

  @Override public void setWriters(List<IWriter> writers) {
    this.writers = writers;
  }

  @Override public void setYear(String year) {
    this.year = year;
  }

  @Override public String getRatingKey() {
    return ratingKey;
  }

  @Override public void setRatingKey(String ratingKey) {
    this.ratingKey = ratingKey;
  }

  @Override public String getParentThumbNailImageKey() {
    return parentThumbNailImageKey;
  }

  @Override public void setParentThumbNailImageKey(String parentThumbNailImageKey) {
    this.parentThumbNailImageKey = parentThumbNailImageKey;
  }

  @Override public String getStudio() {
    return studio;
  }

  @Override public void setStudio(String studio) {
    this.studio = studio;
  }

  @Override public double getRating() {
    return rating;
  }

  @Override public void setRating(double rating) {
    this.rating = rating;
  }

  @Override public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override public String getParentKey() {
    return parentKey;
  }

  @Override public String getEpisode() {
    return episode;
  }

  @Override public void setEpisode(String episode) {
    this.episode = episode;
  }

  @Override public String getSeason() {
    return season;
  }

  @Override public void setSeason(String season) {
    this.season = season;
  }

  @Override public String getDirectPlayUrl() {
    return directPlayUrl;
  }

  @Override public void setDirectPlayUrl(String directPlayUrl) {
    this.directPlayUrl = directPlayUrl;
  }
}
