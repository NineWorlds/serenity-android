package us.nineworlds.serenity.emby.model;

import androidx.annotation.Nullable;
import java.util.List;
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IGenre;
import us.nineworlds.serenity.common.media.model.ILocation;

public class Directory implements IDirectory {
  private String thumb;
  private String banner;
  private int secondary;
  private List<IGenre> generes;
  private String ratingKey;
  private String studio;
  private String rating;
  private String year;
  private String contentRating;
  private String summary;
  private String leafCount;
  private String viewedLeafCount;
  private String title;
  private String art;
  private int refreshing;
  private String type;
  private String agent;
  private String scanner;
  private String language;
  private String uuid;
  private long updatedAt;
  private long createdAt;
  private String prompt;
  private String search;
  private List<ILocation> locations;
  private String key;

  @Nullable @Override public String getThumb() {
    return thumb;
  }

  @Override public void setThumb(@Nullable String thumb) {
    this.thumb = thumb;
  }

  @Nullable @Override public String getBanner() {
    return banner;
  }

  @Override public void setBanner(@Nullable String banner) {
    this.banner = banner;
  }

  @Override public int getSecondary() {
    return secondary;
  }

  @Override public void setSecondary(int secondary) {
    this.secondary = secondary;
  }

  @Override public List<IGenre> getGenres() {
    return generes;
  }

  @Override public void setGenres(List<IGenre> genres) {
    this.generes = genres;
  }

  @Nullable @Override public String getRatingKey() {
    return ratingKey;
  }

  @Override public void setRatingKey(@Nullable String ratingKey) {
    this.ratingKey = ratingKey;
  }

  @Override public String getStudio() {
    return studio;
  }

  @Override public void setStudio(@Nullable String studio) {
    this.studio = studio;
  }

  @Override public String getRating() {
    return rating;
  }

  @Override public void setRating(@Nullable String rating) {
    this.rating = rating;
  }

  @Override public String getYear() {
    return year;
  }

  @Override public void setYear(@Nullable String year) {
    this.year = year;
  }

  @Nullable @Override public String getContentRating() {
    return contentRating;
  }

  @Override public void setContentRating(@Nullable String contentRating) {
    this.contentRating = contentRating;
  }

  @Override public String getSummary() {
    return summary;
  }

  @Override public void setSummary(@Nullable String summary) {
    this.summary = summary;
  }

  @Nullable @Override public String getLeafCount() {
    return leafCount;
  }

  @Override public void setLeafCount(@Nullable String leafCount) {
    this.leafCount = leafCount;
  }

  @Override public String getViewedLeafCount() {
    return viewedLeafCount;
  }

  @Override public void setViewedLeafCount(String viewedLeafCount) {
    this.viewedLeafCount = viewedLeafCount;
  }

  @Nullable @Override public String getTitle() {
    return title;
  }

  @Override public void setTitle(@Nullable String title) {
    this.title = title;
  }

  @Nullable @Override public String getArt() {
    return art;
  }

  @Override public void setArt(@Nullable String art) {
    this.art = art;
  }

  @Override public int getRefreshing() {
    return refreshing;
  }

  @Override public void setRefreshing(int refreshing) {
    this.refreshing = refreshing;
  }

  @Nullable @Override public String getType() {
    return type;
  }

  @Override public void setType(@Nullable String type) {
    this.type = type;
  }

  @Nullable @Override public String getAgent() {
    return agent;
  }

  @Override public void setAgent(@Nullable String agent) {
    this.agent = agent;
  }

  @Nullable @Override public String getScanner() {
    return scanner;
  }

  @Override public void setScanner(@Nullable String scanner) {
    this.scanner = scanner;
  }

  @Nullable @Override public String getLanguage() {
    return language;
  }

  @Override public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  @Nullable @Override public String getUuid() {
    return uuid;
  }

  @Override public void setUuid(@Nullable String uuid) {
    this.uuid = uuid;
  }

  @Override public long getUpdatedAt() {
    return updatedAt;
  }

  @Override public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override public long getCreatedAt() {
    return createdAt;
  }

  @Override public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  @Nullable @Override public List<ILocation> getLocations() {
    return locations;
  }

  @Override public void setLocation(@Nullable  List<ILocation> location) {
    this.locations = location;
  }

  @Nullable @Override public String getPrompt() {
    return prompt;
  }

  @Override public void setPrompt(@Nullable String prompt) {
    this.prompt = prompt;
  }

  @Nullable @Override public String getSearch() {
    return search;
  }

  @Override public void setSearch(String search) {
    this.search = search;
  }

  @Override public void setLocations(List<ILocation> locations) {
    this.locations = locations;
  }

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }
}
