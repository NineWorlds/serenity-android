/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver
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

package us.nineworlds.serenity.ui.video.player;

import android.content.Context;

/**
 * @author davidcarver
 * 
 */
public class MediaControllerDataObject {

	String audioChannels;
	String audioFormat;

	Context context;

	String mediaTagId;

	String posterURL;

	String resolution;

	String summary;

	String title;

	String videoFormat;

	/**
	 * @return the audioChannels
	 */
	public String getAudioChannels() {
		return audioChannels;
	}

	/**
	 * @return the audioFormat
	 */
	public String getAudioFormat() {
		return audioFormat;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @return the mediaTagId
	 */
	public String getMediaTagId() {
		return mediaTagId;
	}

	/**
	 * @return the posterURL
	 */
	public String getPosterURL() {
		return posterURL;
	}

	/**
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the videoFormat
	 */
	public String getVideoFormat() {
		return videoFormat;
	}

	/**
	 * @param audioChannels
	 *            the audioChannels to set
	 */
	public void setAudioChannels(String audioChannels) {
		this.audioChannels = audioChannels;
	}

	/**
	 * @param audioFormat
	 *            the audioFormat to set
	 */
	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @param mediaTagId
	 *            the mediaTagId to set
	 */
	public void setMediaTagId(String mediaTagId) {
		this.mediaTagId = mediaTagId;
	}

	/**
	 * @param posterURL
	 *            the posterURL to set
	 */
	public void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}

	/**
	 * @param resolution
	 *            the resolution to set
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param videoFormat
	 *            the videoFormat to set
	 */
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
}
