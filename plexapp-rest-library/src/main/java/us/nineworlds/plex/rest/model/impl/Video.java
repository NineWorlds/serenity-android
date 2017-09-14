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

package us.nineworlds.plex.rest.model.impl;

import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import us.nineworlds.serenity.common.media.model.ICountry;
import us.nineworlds.serenity.common.media.model.IDirector;
import us.nineworlds.serenity.common.media.model.IGenre;
import us.nineworlds.serenity.common.media.model.IMedia;
import us.nineworlds.serenity.common.media.model.IRole;
import us.nineworlds.serenity.common.media.model.IVideo;
import us.nineworlds.serenity.common.media.model.IWriter;

@Root(name = "Video")
public class Video extends AbstractPlexObject implements IVideo {

  @Attribute(required = false) private String studio;

  @Attribute(required = false) private String summary;

  @Attribute(required = false) private String titleSort;

  @Attribute(required = false) private String title;

  @Attribute(required = false) private int viewCount;

  @Attribute(required = false) private String tagLine;

  @Attribute(required = false) private long viewOffset;

  @Attribute(name = "thumb", required = false) private String thumbNailImageKey;

  @Attribute(name = "art", required = false) private String backgroundImageKey;

  @Attribute(name = "parentThumb", required = false) private String parentThumbNailImageKey;

  @Attribute(name = "grandparentThumb", required = false) private String grandParentThumbNailImageKey;

  @Attribute(name = "grandparentTitle", required = false) private String grandParentTitle;

  @Attribute(name = "duration", required = false) private long duration;

  @Attribute(name = "addedAt", required = false) private long timeAdded;

  @Attribute(name = "updatedAt", required = false) private long timeUpdated;

  /**
   * Formatted date item was originally available in YYYY-MM-DD format.
   */
  @Attribute(name = "originallyAvailableAt", required = false) private String originallyAvailableDate;

  @Attribute(name = "contentRating", required = false) private String contentRating;

  @Attribute(name = "year", required = false) private String year;

  @Attribute(name = "ratingKey", required = false) private String ratingKey;

  @Attribute(name = "parentKey", required = false) private String parentKey;

  @Attribute(name = "index", required = false) private String episode;

  @Attribute(name = "parentIndex", required = false) private String season;

  @Attribute(name = "rating", required = false) private double rating;

  @ElementList(inline = true, required = false, type = Country.class) private List<ICountry> countries;

  @ElementList(inline = true, required = false, type = Director.class) private List<IDirector> directors;

  @ElementList(inline = true, required = false, type = Role.class) private List<IRole> actors;

  @ElementList(inline = true, required = false, type = Writer.class) private List<IWriter> writers;

  @ElementList(inline = true, required = false, type = Genre.class) private List<IGenre> genres;

  @ElementList(inline = true, name = "Media", required = true, type = Media.class) private List<IMedia> medias;

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
}
