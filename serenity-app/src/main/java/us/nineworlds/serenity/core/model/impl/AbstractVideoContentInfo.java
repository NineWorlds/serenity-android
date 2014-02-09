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

import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;

/**
 * General information common t TV Shows and Videos/Movies
 * 
 * @author dcarver
 * 
 */
public abstract class AbstractVideoContentInfo implements VideoContentInfo, Serializable {

	private static final long serialVersionUID = 4744447508883279194L;
	
	private static final float WATCHED_PERCENTAGE = 0.90f;

	private String id;

	private String plotSummary;
	private String castInfo;
	private String posterURL;
	private String parentPosterURL;
	private String grandeParentPosterURL;
	private String backgroundURL;
	private String title;
	private String contentRating;
	private String audioCodec;
	private String videoCodec;
	private String videoResolution;
	private List<String> actors;
	private List<String> directors;
	private List<String> genres;
	private List<String> writers;
	private String year;
	private String directPlayUrl;
	private String aspectRatio;
	private int viewCount;
	private String audioChannels;
	private int resumeOffset;
	private int duration;

	private String season;
	private String episodeNumber;
	private String originalAirDate;
	private String container;
	private String seriesTitle;
	
	private Subtitle subtitle;
	
	private String mediaTagIdentifier;
	private String studio;
	private double rating;
	private String parentURL;
	private boolean trailer;
	private String trailerId;
	
	private List<Subtitle> subtitles;

	@Override
	public String getSeriesTitle() {
		return seriesTitle;
	}

	@Override
	public Subtitle getSubtitle() {
		return subtitle;
	}

	@Override
	public void setSubtitle(Subtitle subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
	}

	@Override
	public String getContainer() {
		return container;
	}

	@Override
	public void setContainer(String container) {
		this.container = container;
	}

	@Override
	public String getSeason() {
		return season;
	}

	@Override
	public void setSeason(String season) {
		this.season = season;
	}

	@Override
	public String getEpisodeNumber() {
		return episodeNumber;
	}

	@Override
	public void setEpisodeNumber(String episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	@Override
	public String getOriginalAirDate() {
		return originalAirDate;
	}

	@Override
	public void setOriginalAirDate(String originalAirDate) {
		this.originalAirDate = originalAirDate;
	}

	@Override
	public int getResumeOffset() {
		return resumeOffset;
	}

	@Override
	public void setResumeOffset(int resumeOffset) {
		this.resumeOffset = resumeOffset;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String getParentPosterURL() {
		return parentPosterURL;
	}

	@Override
	public int getViewCount() {
		return viewCount;
	}

	@Override
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	@Override
	public String getAspectRatio() {
		return aspectRatio;
	}

	@Override
	public void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * getDirectPlayUrl()
	 */
	@Override
	public String getDirectPlayUrl() {
		return directPlayUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * setDirectPlayUrl(java.lang.String)
	 */
	@Override
	public void setDirectPlayUrl(String directPlayUrl) {
		this.directPlayUrl = directPlayUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getActors
	 * ()
	 */
	@Override
	public List<String> getActors() {
		return actors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getAudioCodec
	 * ()
	 */
	@Override
	public String getAudioCodec() {
		return audioCodec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * getBackgroundURL()
	 */
	@Override
	public String getBackgroundURL() {
		return backgroundURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getCastInfo
	 * ()
	 */
	@Override
	public String getCastInfo() {
		return castInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * getContentRating()
	 */
	@Override
	public String getContentRating() {
		return contentRating;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getDirectors
	 * ()
	 */
	@Override
	public List<String> getDirectors() {
		return directors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getGenres
	 * ()
	 */
	@Override
	public List<String> getGenres() {
		return genres;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * getPlotSummary()
	 */
	@Override
	public String getSummary() {
		return plotSummary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getPosterURL
	 * ()
	 */
	@Override
	public String getImageURL() {
		return posterURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getTitle
	 * ()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getVideoCodec
	 * ()
	 */
	@Override
	public String getVideoCodec() {
		return videoCodec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * getVideoResolution()
	 */
	@Override
	public String getVideoResolution() {
		return videoResolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getWriters
	 * ()
	 */
	@Override
	public List<String> getWriters() {
		return writers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#getYear()
	 */
	@Override
	public String getYear() {
		return year;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setActors
	 * (java.util.List)
	 */
	@Override
	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setAudioCodec
	 * (java.lang.String)
	 */
	@Override
	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * setBackgroundURL(java.lang.String)
	 */
	@Override
	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setCastInfo
	 * (java.lang.String)
	 */
	@Override
	public void setCastInfo(String castInfo) {
		this.castInfo = castInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * setContentRating(java.lang.String)
	 */
	@Override
	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setDirectors
	 * (java.util.List)
	 */
	@Override
	public void setDirectors(List<String> directors) {
		this.directors = directors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setGenres
	 * (java.util.List)
	 */
	@Override
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * setPlotSummary(java.lang.String)
	 */
	@Override
	public void setSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setPosterURL
	 * (java.lang.String)
	 */
	@Override
	public void setImageURL(String posterURL) {
		this.posterURL = posterURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setTitle
	 * (java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setVideoCodec
	 * (java.lang.String)
	 */
	@Override
	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.core.model.VideoContentInfo#
	 * setVideoResolution(java.lang.String)
	 */
	@Override
	public void setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setWriters
	 * (java.util.List)
	 */
	@Override
	public void setWriters(List<String> writers) {
		this.writers = writers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.kingargyle.plexappclient.core.model.VideoContentInfo#setYear
	 * (java.lang.String)
	 */
	@Override
	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setParentPosterURL(String parentPosterURL) {
		this.parentPosterURL = parentPosterURL;
	}
	
	@Override
	public void setGrandParentPosterURL(String posterURL) {
		this.grandeParentPosterURL = posterURL;
	}
	
	@Override
	public String getGrandParentPosterURL() {
		return this.grandeParentPosterURL;
	}

	@Override
	public String getAudioChannels() {
		return this.audioChannels;
	}

	@Override
	public void setAudioChannels(String audioChannels) {
		this.audioChannels = audioChannels;
	}
	
	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public void setDuration(int duration) {
		this.duration = duration;
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
	public void setStudio(String studio) {
		this.studio = studio;
	}
	
	@Override
	public String getStudio() {
		return studio;
	}

	@Override
	public double getRating() {
		return rating;
	}

	@Override
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	@Override
	public String getParentKey() {
		return parentURL;
	}
	
	@Override
	public void setParentKey(String parentKey) {
		this.parentURL = parentKey;
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.VideoContentInfo#isPartiallyWatched()
	 */
	@Override
	public boolean isPartiallyWatched() {
		if (getResumeOffset() > 0) {
			final float percentWatched = viewedPercentage();
			if (percentWatched <= SerenityConstants.WATCHED_PERCENT) {
				return true;
			}
		}		
		return false;
	}
	
	@Override
	public boolean isWatched() {
		final float percentWatched = viewedPercentage();
		if (percentWatched > SerenityConstants.WATCHED_PERCENT) {
			return true;
		}
		
		if (getResumeOffset() == 0 && getViewCount() > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isUnwatched() {
		if (getViewCount() == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public float viewedPercentage() {
		float duration = Float.valueOf(getDuration());
		float offset = Float.valueOf(getResumeOffset());
		if (duration == 0) {
			return 0;
		}
		
		float percentWatched = offset / duration;
		return percentWatched;
	}
	
	@Override
	public void toggleWatchStatus() {
		if (isPartiallyWatched() || isUnwatched()) {
			new WatchedVideoAsyncTask().execute(id());
			setViewCount(getViewCount() + 1);
			return;
		}
		
		new UnWatchVideoAsyncTask().execute(id());
		setViewCount(0);
	}
	
	@Override
	public void setTrailer(boolean trailer) {
		this.trailer = trailer; 
	}
	
	@Override
	public boolean hasTrailer() {
		return trailer;
	}
	
	@Override
	public String trailerId() {
		return trailerId;
	}
	
	@Override
	public void setTrailerId(String id) {
		trailerId = id;
	}
	
	@Override
	public List<Subtitle> getAvailableSubtitles() {
		return subtitles;
	}
	
	@Override
	public void setAvailableSubTitles(List<Subtitle> subtitles) {
		this.subtitles = subtitles;
	}
}
