package com.github.kingargyle.plexappclient.ui.browser.tv.episodes;

import java.util.List;

public class EpisodePosterInfo {
	private String plotSummary;
	private String castInfo;
	private String posterURL;
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
	
	public String getDirectPlayUrl() {
		return directPlayUrl;
	}

	public void setDirectPlayUrl(String directPlayUrl) {
		this.directPlayUrl = directPlayUrl;
	}

	public List<String> getActors() {
		return actors;
	}
	
	public String getAudioCodec() {
		return audioCodec;
	}

	public String getBackgroundURL() {
		return backgroundURL;
	}

	public String getCastInfo() {
		return castInfo;
	}

	public String getContentRating() {
		return contentRating;
	}

	public List<String> getDirectors() {
		return directors;
	}

	public List<String> getGenres() {
		return genres;
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
	

	public String getVideoCodec() {
		return videoCodec;
	}

	public String getVideoResolution() {
		return videoResolution;
	}

	public List<String> getWriters() {
		return writers;
	}

	public String getYear() {
		return year;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	public void setCastInfo(String castInfo) {
		this.castInfo = castInfo;
	}

	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	public void setDirectors(List<String> directors) {
		this.directors = directors;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	public void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	public void setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
	}

	public void setWriters(List<String> writers) {
		this.writers = writers;
	}

	public void setYear(String year) {
		this.year = year;
	}
	

}
