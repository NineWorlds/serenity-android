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

import java.io.Serializable;
import java.util.List;

import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;

/**
 * @author dcarver
 * 
 */
public abstract class AbstractSeriesContentInfo implements SeriesContentInfo, Serializable {

	private static final long serialVersionUID = 9068543270225774788L;
		
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
	private String mediaTagIdentifier;
	private String studio;
	private double rating;

	@Override
	public String id() {
		return id;
	}

	@Override
	public String getParentTitle() {
		return parentShowTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.SeriesContentInfo#
	 * setParentShowTitle(java.lang.String)
	 */
	@Override
	public void setParentTitle(String title) {
		parentShowTitle = title;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public List<String> getGeneres() {
		return generes;
	}

	@Override
	public void setGeneres(List<String> generes) {
		this.generes = generes;
	}

	@Override
	public String getShowsWatched() {
		return showsWatched;
	}

	@Override
	public void setShowsWatched(String showsWatched) {
		this.showsWatched = showsWatched;
	}

	@Override
	public String getShowsUnwatched() {
		return ShowsUnwatched;
	}

	@Override
	public void setShowsUnwatched(String showsUnwatched) {
		ShowsUnwatched = showsUnwatched;
	}

	@Override
	public String getBackgroundURL() {
		return backgroundURL;
	}

	@Override
	public String getShowMetaDataURL() {
		return showMetaDataURL;
	}

	@Override
	public void setShowMetaDataURL(String showMetaDataURL) {
		this.showMetaDataURL = showMetaDataURL;
	}

	@Override
	public String getThumbNailURL() {
		return thumbNailURL;
	}

	@Override
	public void setThumbNailURL(String thumbNailURL) {
		this.thumbNailURL = thumbNailURL;
	}

	@Override
	public String getContentRating() {
		return contentRating;
	}

	@Override
	public String getSummary() {
		return plotSummary;
	}

	@Override
	public String getImageURL() {
		return posterURL;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getYear() {
		return year;
	}

	@Override
	public void setYear(String year) {
		this.year = year;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.SeriesContentInfo#
	 * setContentRating(java.lang.String)
	 */
	@Override
	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;

	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setImageURL(String posterURL) {
		this.posterURL = posterURL;
	}

	@Override
	public void setSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	@Override
	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getMediaTagIdentifier() {
		return mediaTagIdentifier;
	}

	@Override
	public void setMediaTagIdentifier(String mediaTagIdentifier) {
		this.mediaTagIdentifier = mediaTagIdentifier;
	}

	@Override
	public String getStudio() {
		return studio;
	}

	@Override
	public void setStudio(String studio) {
		this.studio = studio;
	}

	@Override
	public double getRating() {
		return rating;
	}

	@Override
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.SeriesContentInfo#isPartiallyWatched()
	 */
	@Override
	public boolean isPartiallyWatched() {
		
		int unwatched = 0;
		int watched = 0;
		
		if (getShowsUnwatched() != null) {
			unwatched = Integer.parseInt(getShowsUnwatched());
		}
		
		if (getShowsWatched() != null) {
			watched = Integer.parseInt(getShowsWatched());
		}
		int total = watched + unwatched;
		
		if (unwatched != total && watched < total) {
			return true;
		}
		
		return false;
		
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.SeriesContentInfo#isUnwatched()
	 */
	@Override
	public boolean isUnwatched() {
		int unwatched = Integer.parseInt(getShowsUnwatched());
		
		if (unwatched > 0) {
			return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.SeriesContentInfo#isWatched()
	 */
	@Override
	public boolean isWatched() {
		int watchedCount = Integer.parseInt(getShowsWatched());
		if (totalShows() == watchedCount) {
			return true;
		}
		return false;
	}
	
	@Override
	public int totalShows() {
		int unwatched = Integer.parseInt(getShowsUnwatched());
		int watched = Integer.parseInt(getShowsWatched());
		int totalShows = unwatched + watched;
		return totalShows;
	}
	
	@Override
	public float viewedPercentage() {
		if (totalShows() == 0) {
			return 0;
		}
		
		float watched = Float.parseFloat(getShowsWatched());
		float percent = watched / totalShows();
		
		return percent;
	}
	
	@Override
	public void toggleWatchedStatus() {
		if (isPartiallyWatched() || isUnwatched()) {
			new WatchedVideoAsyncTask().execute(id());
			setShowsWatched(Integer.toString(totalShows()));
			setShowsUnwatched("0");
			return;
		}
		
		new UnWatchVideoAsyncTask().execute(id());
		setShowsUnwatched(Integer.toString(totalShows()));
		setShowsWatched("0");
	}

	
	

}
