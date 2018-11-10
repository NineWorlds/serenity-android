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
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IGenre;
import us.nineworlds.serenity.common.media.model.ILocation;

@Root(name = "Directory")
public class Directory extends AbstractPlexObject implements IDirectory {

  @Attribute(required = true) private String title;

  @Attribute(required = false) private String art;

  @Attribute(required = false) private String banner;

  @Attribute(required = false) private String thumb;

  @Attribute(required = false) private int refreshing;

  @Attribute(required = false) private String type;

  @Attribute(required = false) private String agent;

  @Attribute(required = false) private String scanner;

  @Attribute(required = false) private String language;

  @Attribute(required = false) private String uuid;

  @Attribute(required = false) private long updatedAt;

  @Attribute(required = false) private long createdAt;

  @Attribute(required = false) private String prompt;

  @Attribute(required = false) private String search;

  @Attribute(required = false) private int secondary;

  @ElementList(inline = true, required = false, type = Genre.class) private List<IGenre> genres;

  @ElementList(inline = true, name = "Location", required = false, type = Location.class) private List<ILocation>
      locations;

  @Attribute(required = false) private String ratingKey;

  @Attribute(required = false) private String studio;

  @Attribute(required = false) private String rating;

  @Attribute(required = false) private String year;

  @Attribute(required = false) private String contentRating;

  @Attribute(required = false) private String summary;

  @Attribute(required = false) private String leafCount;

  @Attribute(required = false) private String viewedLeafCount;

  @Override public String getThumb() {
    return thumb;
  }

  @Override public void setThumb(String thumb) {
    this.thumb = thumb;
  }

  @Override public String getBanner() {
    return banner;
  }

  @Override public void setBanner(String banner) {
    this.banner = banner;
  }

  @Override public int getSecondary() {
    return secondary;
  }

  @Override public void setSecondary(int secondary) {
    this.secondary = secondary;
  }

  @Override public List<IGenre> getGenres() {
    return genres;
  }

  @Override public void setGenres(List<IGenre> genres) {
    this.genres = genres;
  }

  @Override public String getRatingKey() {
    return ratingKey;
  }

  @Override public void setRatingKey(String ratingKey) {
    this.ratingKey = ratingKey;
  }

  @Override public String getStudio() {
    return studio;
  }

  @Override public void setStudio(String studio) {
    this.studio = studio;
  }

  @Override public String getRating() {
    return rating;
  }

  @Override public void setRating(String rating) {
    this.rating = rating;
  }

  @Override public String getYear() {
    return year;
  }

  @Override public void setYear(String year) {
    this.year = year;
  }

  @Override public String getContentRating() {
    return contentRating;
  }

  @Override public void setContentRating(String contentRating) {
    this.contentRating = contentRating;
  }

  @Override public String getSummary() {
    return summary;
  }

  @Override public void setSummary(String summary) {
    this.summary = summary;
  }

  @Override public String getLeafCount() {
    return leafCount;
  }

  @Override public void setLeafCount(String leafCount) {
    this.leafCount = leafCount;
  }

  @Override public String getViewedLeafCount() {
    return viewedLeafCount;
  }

  @Override public void setViewedLeafCount(String viewedLeafCount) {
    this.viewedLeafCount = viewedLeafCount;
  }

  @Override public String getTitle() {
    return title;
  }

  @Override public void setTitle(String title) {
    this.title = title;
  }

  @Override public String getArt() {
    return art;
  }

  @Override public void setArt(String art) {
    this.art = art;
  }

  @Override public int getRefreshing() {
    return refreshing;
  }

  @Override public void setRefreshing(int refreshing) {
    this.refreshing = refreshing;
  }

  @Override public String getType() {
    return type;
  }

  @Override public void setType(String type) {
    this.type = type;
  }

  @Override public String getAgent() {
    return agent;
  }

  @Override public void setAgent(String agent) {
    this.agent = agent;
  }

  @Override public String getScanner() {
    return scanner;
  }

  @Override public void setScanner(String scanner) {
    this.scanner = scanner;
  }

  @Override public String getLanguage() {
    return language;
  }

  @Override public void setLanguage(String language) {
    this.language = language;
  }

  @Override public String getUuid() {
    return uuid;
  }

  @Override public void setUuid(String uuid) {
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

  @Override public List<ILocation> getLocations() {
    return locations;
  }

  @Override public void setLocation(List<ILocation> location) {
    this.locations = location;
  }

  @Override public String getPrompt() {
    return prompt;
  }

  @Override public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  @Override public String getSearch() {
    return search;
  }

  @Override public void setSearch(String search) {
    this.search = search;
  }

  @Override public void setLocations(List<ILocation> locations) {
    this.locations = locations;
  }
}
