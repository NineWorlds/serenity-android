package com.github.kingargyle.plexappclient.ui.browser.movie;

public class MoviePosterInfo {
	private String plotSummary;
	private String castInfo;
	private String posterURL;
	private String backgroundURL;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBackgroundURL() {
		return backgroundURL;
	}

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	public String getPlotSummary() {
		return plotSummary;
	}

	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	public String getCastInfo() {
		return castInfo;
	}

	public void setCastInfo(String castInfo) {
		this.castInfo = castInfo;
	}

	public String getPosterURL() {
		return posterURL;
	}

	public void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}
	

}
