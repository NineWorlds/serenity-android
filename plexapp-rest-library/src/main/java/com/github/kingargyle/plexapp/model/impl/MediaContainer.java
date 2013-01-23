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

package com.github.kingargyle.plexapp.model.impl;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="MediaContainer")
public class MediaContainer {

	@Attribute(required=true)
	private int size;
	
	@Attribute(required=false)
	private int allowSync;
	
	@Attribute(required=false)
	private String art;
	
	@Attribute(required=false)
	private String identifier;
	
	@Attribute(required=false)
	private String mediaTagPrefix;
	
	@Attribute(required=false)
	private long mediaTagVersion;
	
	@Attribute(required=false)
	private String title1;
	
	@Attribute(required=false)
	private String title2;
	
	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	@Attribute(required=false)
	private int sortAsc;
	
	@Attribute(required=false)
	private String content;
	
	@Attribute(required=false)
	private String viewGroup;
	
	@Attribute(required=false)
	private int viewMode;
	

	@ElementList(inline=true,required=false)
	private List<Directory> directories;
	
	@ElementList(inline=true,required=false)
	private List<Video> videos;
	
	public List<Directory> getDirectories() {
		return directories;
	}

	public void setDirectories(List<Directory> directory) {
		this.directories = directory;
	}
	

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getAllowSync() {
		return allowSync;
	}

	public void setAllowSync(int allowSync) {
		this.allowSync = allowSync;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getMediaTagPrefix() {
		return mediaTagPrefix;
	}

	public void setMediaTagPrefix(String mediaTagPrefix) {
		this.mediaTagPrefix = mediaTagPrefix;
	}

	public long getMediaTagVersion() {
		return mediaTagVersion;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public int getSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(int sortAsc) {
		this.sortAsc = sortAsc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getViewGroup() {
		return viewGroup;
	}

	public void setViewGroup(String viewGroup) {
		this.viewGroup = viewGroup;
	}

	public int getViewMode() {
		return viewMode;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
	}

	public void setMediaTagVersion(long mediaTagVersion) {
		this.mediaTagVersion = mediaTagVersion;
	}

	public void setMediaTagVersion(int mediaTagVersion) {
		this.mediaTagVersion = mediaTagVersion;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	/**
	 * @return the videos
	 */
	public List<Video> getVideos() {
		return videos;
	}

	/**
	 * @param videos the videos to set
	 */
	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	
}
