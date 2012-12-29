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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Represents the video container
 * 
 * @author dcarver
 *
 */
@Root(name="Video")
public class Video extends AbstractPlexObject {
	
	@Attribute
	private String summary;
	
	@Attribute
	private String titleSort;
	
	@Attribute
	private String title;
	
	@Attribute
	private int viewCount;
	
	@Attribute
	private String tagLine;
	
	@Attribute
	/**
	 * Point where viewing can be resumed.
	 */
	private long viewOffset;
	
	@Attribute(name="thumb")
	/**
	 * REST path for obtaining thumbnail image
	 */
	private String thumbNailImageKey;
	
	@Attribute(name="art")
	private String backgroundImageKey;
	
	@Attribute(name="duration")
	private long duration;
	
	@Attribute(name="addedAt")
	private long timeAdded;
	
	@Attribute(name="updatedAt")
	private long timeUpdated;
	
	@Attribute(name="originallyAvailableAt")
	/**
	 * Formatted date item was originally available in YYYY-MM-DD format.
	 */
	private String originallyAvailableDate;
	
	@Element(name="Media")
	private Media media;
	

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the titleSort
	 */
	public String getTitleSort() {
		return titleSort;
	}

	/**
	 * @param titleSort the titleSort to set
	 */
	public void setTitleSort(String titleSort) {
		this.titleSort = titleSort;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the viewCount
	 */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @return the tagLine
	 */
	public String getTagLine() {
		return tagLine;
	}

	/**
	 * @param tagLine the tagLine to set
	 */
	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	/**
	 * @return the viewOffset
	 */
	public long getViewOffset() {
		return viewOffset;
	}

	/**
	 * @param viewOffset the viewOffset to set
	 */
	public void setViewOffset(long viewOffset) {
		this.viewOffset = viewOffset;
	}

	/**
	 * @return the thumbNailImageKey
	 */
	public String getThumbNailImageKey() {
		return thumbNailImageKey;
	}

	/**
	 * @param thumbNailImageKey the thumbNailImageKey to set
	 */
	public void setThumbNailImageKey(String thumbNailImageKey) {
		this.thumbNailImageKey = thumbNailImageKey;
	}

	/**
	 * @return the backgroundImageKey
	 */
	public String getBackgroundImageKey() {
		return backgroundImageKey;
	}

	/**
	 * @param backgroundImageKey the backgroundImageKey to set
	 */
	public void setBackgroundImageKey(String backgroundImageKey) {
		this.backgroundImageKey = backgroundImageKey;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the timeAdded
	 */
	public long getTimeAdded() {
		return timeAdded;
	}

	/**
	 * @param timeAdded the timeAdded to set
	 */
	public void setTimeAdded(long timeAdded) {
		this.timeAdded = timeAdded;
	}

	/**
	 * @return the timeUpdated
	 */
	public long getTimeUpdated() {
		return timeUpdated;
	}

	/**
	 * @param timeUpdated the timeUpdated to set
	 */
	public void setTimeUpdated(long timeUpdated) {
		this.timeUpdated = timeUpdated;
	}

	/**
	 * @return the originallyAvailableDate in YYYY-MM-DD format.
	 */
	public String getOriginallyAvailableDate() {
		return originallyAvailableDate;
	}

	/**
	 * This needs to be formatted in YYYY-MM-DD format.
	 * @param originallyAvailableDate the originallyAvailableDate to set
	 */
	public void setOriginallyAvailableDate(String originallyAvailableDate) {
		this.originallyAvailableDate = originallyAvailableDate;
	}
		

}
