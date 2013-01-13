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

package com.github.kingargyle.plexapp.model.impl;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Represents the video container
 * 
 * @author dcarver
 *
 */
@Root(name="Video")
public class Video extends AbstractPlexObject {
	
	@Attribute(required=false)
	private String summary;
	
	@Attribute(required=false)
	private String titleSort;
	
	@Attribute(required=false)
	private String title;
	
	@Attribute(required=false)
	private int viewCount;
	
	@Attribute(required=false)
	private String tagLine;
	
	@Attribute(required=false)
	/**
	 * Point where viewing can be resumed.
	 */
	private long viewOffset;
	
	@Attribute(name="thumb",required=false)
	/**
	 * REST path for obtaining thumbnail image
	 */
	private String thumbNailImageKey;
	
	@Attribute(name="art",required=false)
	private String backgroundImageKey;
	
	@Attribute(name="duration",required=false)
	private long duration;
	
	@Attribute(name="addedAt",required=false)
	private long timeAdded;
	
	@Attribute(name="updatedAt",required=false)
	private long timeUpdated;
	
	@Attribute(name="originallyAvailableAt",required=false)
	/**
	 * Formatted date item was originally available in YYYY-MM-DD format.
	 */
	private String originallyAvailableDate;
	
	@Attribute(name="contentRating",required=false)
	private String contentRating;
	
	@ElementList(inline=true,required=false)
	private List<Country> countries;

	@ElementList(inline=true,required=false)
	private List<Director> directors;

	@ElementList(inline=true,required=false)
	private List<Role> actors;

	@ElementList(inline=true,required=false)
	private List<Writer> writers;

	@ElementList(inline=true,required=false)
	private List<Genre> genres;

	@Element(name="Media",required=true)
	private Media media;

	public List<Role> getActors() {
		return actors;
	}

	/**
	 * @return the backgroundImageKey
	 */
	public String getBackgroundImageKey() {
		return backgroundImageKey;
	}

	public String getContentRating() {
		return contentRating;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public List<Director> getDirectors() {
		return directors;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	
	public List<Genre> getGenres() {
		return genres;
	}
	
	public Media getMedia() {
		return media;
	}
	
	/**
	 * @return the originallyAvailableDate in YYYY-MM-DD format.
	 */
	public String getOriginallyAvailableDate() {
		return originallyAvailableDate;
	}
	
	
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the tagLine
	 */
	public String getTagLine() {
		return tagLine;
	}

	/**
	 * @return the thumbNailImageKey
	 */
	public String getThumbNailImageKey() {
		return thumbNailImageKey;
	}

	/**
	 * @return the timeAdded
	 */
	public long getTimeAdded() {
		return timeAdded;
	}

	/**
	 * @return the timeUpdated
	 */
	public long getTimeUpdated() {
		return timeUpdated;
	}
	

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the titleSort
	 */
	public String getTitleSort() {
		return titleSort;
	}

	/**
	 * @return the viewCount
	 */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * @return the viewOffset
	 */
	public long getViewOffset() {
		return viewOffset;
	}

	public List<Writer> getWriters() {
		return writers;
	}

	public void setActors(List<Role> actors) {
		this.actors = actors;
	}

	/**
	 * @param backgroundImageKey the backgroundImageKey to set
	 */
	public void setBackgroundImageKey(String backgroundImageKey) {
		this.backgroundImageKey = backgroundImageKey;
	}

	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public void setDirectors(List<Director> directors) {
		this.directors = directors;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * This needs to be formatted in YYYY-MM-DD format.
	 * @param originallyAvailableDate the originallyAvailableDate to set
	 */
	public void setOriginallyAvailableDate(String originallyAvailableDate) {
		this.originallyAvailableDate = originallyAvailableDate;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @param tagLine the tagLine to set
	 */
	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	/**
	 * @param thumbNailImageKey the thumbNailImageKey to set
	 */
	public void setThumbNailImageKey(String thumbNailImageKey) {
		this.thumbNailImageKey = thumbNailImageKey;
	}

	/**
	 * @param timeAdded the timeAdded to set
	 */
	public void setTimeAdded(long timeAdded) {
		this.timeAdded = timeAdded;
	}

	/**
	 * @param timeUpdated the timeUpdated to set
	 */
	public void setTimeUpdated(long timeUpdated) {
		this.timeUpdated = timeUpdated;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param titleSort the titleSort to set
	 */
	public void setTitleSort(String titleSort) {
		this.titleSort = titleSort;
	}

	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @param viewOffset the viewOffset to set
	 */
	public void setViewOffset(long viewOffset) {
		this.viewOffset = viewOffset;
	}

	public void setWriters(List<Writer> writers) {
		this.writers = writers;
	}
		

}
