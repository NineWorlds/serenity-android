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

package us.nineworlds.serenity.core.model;

import java.util.List;

/**
 * @author dcarver
 * 
 */
public interface SeriesContentInfo extends ContentInfo {

	public void setKey(String key);

	public String getKey();

	public List<String> getGeneres();

	public void setGeneres(List<String> generes);

	public String getShowsWatched();

	public void setShowsWatched(String showsWatched);

	public String getShowsUnwatched();

	public void setShowsUnwatched(String showsUnwatched);

	public String getShowMetaDataURL();

	public void setShowMetaDataURL(String showMetaDataURL);

	public String getThumbNailURL();

	public void setThumbNailURL(String thumbNailURL);

	public String getContentRating();

	public String getYear();

	public void setContentRating(String contentRating);

	public void setYear(String year);

	public String getParentTitle();

	public void setParentTitle(String title);
	
	public String getStudio();
	
	public void setStudio(String studio);
	
	public void setRating(double ratings);
	
	public double getRating();
	
	/**
	 * Indicates if a show has been partially watched
	 * @return true if it has been watched
	 */
	public boolean isPartiallyWatched();
	
	/**
	 * Indicates that the show has been watched
	 * 
	 * Use getShowsWatched to get the actual count
	 * 
	 */
	public boolean isWatched();
	
	
	/**
	 * Indicates that the show has not been watched.
	 * 
	 * Use getShowsUnwatched to get the actual count.
	 * 
	 */
	public boolean isUnwatched();
	
	
	public float viewedPercentage();
	
	
	public void toggleWatchedStatus();
	
	public int totalShows();

}
