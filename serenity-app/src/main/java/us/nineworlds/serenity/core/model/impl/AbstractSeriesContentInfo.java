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

package us.nineworlds.serenity.core.model.impl;

import java.util.List;

import us.nineworlds.serenity.core.model.SeriesContentInfo;


/**
 * @author dcarver
 * 
 */
public abstract class AbstractSeriesContentInfo implements SeriesContentInfo {

	private String id;
	private String plotSummary;
	private String posterURL;
	private String backgroundURL;
	private String title;
	private String contentRating;
	private String year;
	private String showMetaDataURL;
	private String thumbNailURL;
	private List<String> generes;
	private String showsWatched;
	private String ShowsUnwatched;
	private String key;
	private String parentShowTitle;
	
	public String id() {
		return id;
	}

	public String getParentShowTitle() {
		return parentShowTitle;
	}

	/* (non-Javadoc)
	 * @see com.github.kingargyle.plexappclient.core.model.SeriesContentInfo#setParentShowTitle(java.lang.String)
	 */
	public void setParentShowTitle(String title) {
		parentShowTitle = title;
	}
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getGeneres() {
		return generes;
	}

	public void setGeneres(List<String> generes) {
		this.generes = generes;
	}

	public String getShowsWatched() {
		return showsWatched;
	}

	public void setShowsWatched(String showsWatched) {
		this.showsWatched = showsWatched;
	}

	public String getShowsUnwatched() {
		return ShowsUnwatched;
	}

	public void setShowsUnwatched(String showsUnwatched) {
		ShowsUnwatched = showsUnwatched;
	}

	public String getBackgroundURL() {
		return backgroundURL;
	}

	public String getShowMetaDataURL() {
		return showMetaDataURL;
	}

	public void setShowMetaDataURL(String showMetaDataURL) {
		this.showMetaDataURL = showMetaDataURL;
	}

	public String getThumbNailURL() {
		return thumbNailURL;
	}

	public void setThumbNailURL(String thumbNailURL) {
		this.thumbNailURL = thumbNailURL;
	}

	public String getContentRating() {
		return contentRating;
	}

	public String getPlotSummary() {
		return plotSummary;
	}

	public String getPosterURL() {
		return posterURL;
	}

	public String getTitle() {
		return title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.SeriesContentInfo#
	 * setContentRating(java.lang.String)
	 */
	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;

	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}

	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}
