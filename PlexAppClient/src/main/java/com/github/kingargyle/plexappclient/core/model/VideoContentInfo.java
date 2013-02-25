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

package com.github.kingargyle.plexappclient.core.model;

import java.util.List;

/**
 * @author dcarver
 *
 */
public interface VideoContentInfo extends ContentInfo {

	public String getDirectPlayUrl();

	public void setDirectPlayUrl(String directPlayUrl);

	public List<String> getActors();

	public String getAudioCodec();

	public String getCastInfo();

	public String getContentRating();

	public List<String> getDirectors();
	
	public List<String> getGenres();

	public String getVideoCodec();

	public String getVideoResolution();

	public List<String> getWriters();

	public String getYear();
	
	public String getAspectRatio();
	
	public int getViewCount();
	
	public String getParentPosterURL();

	public void setActors(List<String> actors);

	public void setAudioCodec(String audioCodec);

	public void setBackgroundURL(String backgroundURL);

	public void setCastInfo(String castInfo);

	public void setContentRating(String contentRating);
	
	public void setDirectors(List<String> directors);

	public void setGenres(List<String> genres);

	public void setPlotSummary(String plotSummary);

	public void setPosterURL(String posterURL);

	public void setTitle(String title);

	public void setVideoCodec(String videoCodec);

	public void setVideoResolution(String videoResolution);

	public void setWriters(List<String> writers);

	public void setYear(String year);
	
	public void setAspectRatio(String ratio);
	
	public void setViewCount(int viewCount);
	
	public void setParentPosterURL(String parentPosterURL);
	
	public String getAudioChannels();
	
	public void setAudioChannels(String audioChannels);
	
	public void setResumeOffset(int offset );
	
	public int getResumeOffset();
	
	public String getSeason();
	
	public void setSeason(String season);
	
	public String getEpisodeNumber();
	
	public void setEpisodeNumber(String episodeNum);
	
	public void setOriginalAirDate(String airDate);
	
	public String getOriginalAirDate();

}