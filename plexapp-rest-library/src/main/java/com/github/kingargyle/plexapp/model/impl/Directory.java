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

@Root(name="Directory")
public class Directory extends AbstractPlexObject {

	@Attribute(required=true)
	private String title;
	
	@Attribute(required=false)
	private String art;
	
	@Attribute(required=false)
	private int refreshing;
		
	@Attribute(required=false)
	private String type;
	
	@Attribute(required=false)
	private String agent;
	
	@Attribute(required=false)
	private String scanner;
	
	@Attribute(required=false)
	private String language;
	
	@Attribute(required=false)
	private String uuid;
	
	@Attribute(required=false)
	private long updatedAt;
	
	@Attribute(required=false)
	private long createdAt;
	
	@Attribute(required=false)
	/**
	 * Used for searches.
	 */
	private String prompt;
	
	@Attribute(required=false)
	/**
	 * Only appears with prompt.
	 */
	private String search;	
	
	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public String getRatingKey() {
		return ratingKey;
	}

	public void setRatingKey(String ratingKey) {
		this.ratingKey = ratingKey;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getContentRating() {
		return contentRating;
	}

	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLeafCount() {
		return leafCount;
	}

	public void setLeafCount(String leafCount) {
		this.leafCount = leafCount;
	}

	public String getViewedLeafCount() {
		return viewedLeafCount;
	}

	public void setViewedLeafCount(String viewedLeafCount) {
		this.viewedLeafCount = viewedLeafCount;
	}

	@ElementList(inline=true,required=false)
	private List<Genre> genres;

	@Element(name="Location",required=false)
	private Location location;
	
	@Attribute(required=false)
	private String ratingKey;
	
	@Attribute(required=false)
	private String studio;
	
	@Attribute(required=false)
	private String rating;
	
	@Attribute(required=false)
	private String year;
	
	@Attribute(required=false)
	private String contentRating;
	
	@Attribute(required=false)
	private String summary;
	
	@Attribute(required=false)
	private String leafCount;
	
	@Attribute(required=false)
	private String viewedLeafCount;
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public int getRefreshing() {
		return refreshing;
	}

	public void setRefreshing(int refreshing) {
		this.refreshing = refreshing;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getScanner() {
		return scanner;
	}

	public void setScanner(String scanner) {
		this.scanner = scanner;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
}
