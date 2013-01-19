package com.github.kingargyle.plexappclient.ui.browser.tv;

public class TVShowBannerInfo {
	private String plotSummary;
	private String posterURL;
	private String backgroundURL;
	private String title;
	private String contentRating;
	private String year;
	private String showMetaDataURL;
	private String thumbNailURL;
	

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

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
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

	public void setYear(String year) {
		this.year = year;
	}
}
