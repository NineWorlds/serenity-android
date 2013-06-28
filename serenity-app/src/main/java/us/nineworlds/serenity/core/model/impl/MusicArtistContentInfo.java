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

import us.nineworlds.serenity.core.model.ArtistContentInfo;

/**
 * @author dcarver
 *
 */
public class MusicArtistContentInfo implements ArtistContentInfo, Serializable {
	
	private static final long serialVersionUID = -3671771266686596176L;
	private String id;
	private String summary;
	private String backgroundImageURL;
	private String imageURL;
	private String title;

	@Override
	public String id() {
		return id;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public String getBackgroundURL() {
		return backgroundImageURL;
	}

	@Override
	public String getImageURL() {
		return imageURL;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public void setBackgroundURL(String backgroundURL) {
		this.backgroundImageURL = backgroundURL;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getMediaTagIdentifier() {
		return null;
	}

	@Override
	public void setMediaTagIdentifier(String mediaTagIdentifier) {

	}

}
