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

package us.nineworlds.plex.rest.model.impl;

import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.media.model.ITrack;
import us.nineworlds.serenity.common.media.model.IVideo;

@Root(name = "MediaContainer")
public class MediaContainer implements IMediaContainer {

  @Attribute(required = true) private int size;

  @Attribute(required = false) private int allowSync;

  @Attribute(required = false) private String art;

  @Attribute(required = false) private String identifier;

  @Attribute(required = false) private String mediaTagPrefix;

  @Attribute(required = false) private long mediaTagVersion;

  @Attribute(required = false) private String title1;

  @Attribute(required = false) private String title2;

  @Attribute(required = false) private int sortAsc;

  @Attribute(required = false) private String content;

  @Attribute(required = false) private String viewGroup;

  @Attribute(required = false) private int viewMode;

  @Attribute(name = "thumb", required = false) private String parentPosterURL;

  @Attribute(name = "parentIndex", required = false) private String parentIndex;

  @ElementList(inline = true, required = false, type = Directory.class) private List<IDirectory> directories;

  @ElementList(inline = true, required = false, type = Video.class) private List<IVideo> videos;

  @ElementList(inline = true, required = false, type = Track.class) private List<ITrack> tracks;

  @Override public String getTitle2() {
    return title2;
  }

  @Override public void setTitle2(String title2) {
    this.title2 = title2;
  }

  @Override public List<IDirectory> getDirectories() {
    return directories;
  }

  @Override public void setDirectories(List<IDirectory> directory) {
    this.directories = directory;
  }

  @Override public int getSize() {
    return size;
  }

  @Override public void setSize(int size) {
    this.size = size;
  }

  @Override public int getAllowSync() {
    return allowSync;
  }

  @Override public void setAllowSync(int allowSync) {
    this.allowSync = allowSync;
  }

  @Override public String getIdentifier() {
    return identifier;
  }

  @Override public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  @Override public String getMediaTagPrefix() {
    return mediaTagPrefix;
  }

  @Override public void setMediaTagPrefix(String mediaTagPrefix) {
    this.mediaTagPrefix = mediaTagPrefix;
  }

  @Override public long getMediaTagVersion() {
    return mediaTagVersion;
  }

  @Override public String getArt() {
    return art;
  }

  @Override public void setArt(String art) {
    this.art = art;
  }

  @Override public int getSortAsc() {
    return sortAsc;
  }

  @Override public void setSortAsc(int sortAsc) {
    this.sortAsc = sortAsc;
  }

  @Override public String getContent() {
    return content;
  }

  @Override public void setContent(String content) {
    this.content = content;
  }

  @Override public String getViewGroup() {
    return viewGroup;
  }

  @Override public void setViewGroup(String viewGroup) {
    this.viewGroup = viewGroup;
  }

  @Override public int getViewMode() {
    return viewMode;
  }

  @Override public void setViewMode(int viewMode) {
    this.viewMode = viewMode;
  }

  @Override public void setMediaTagVersion(long mediaTagVersion) {
    this.mediaTagVersion = mediaTagVersion;
  }

  @Override public void setMediaTagVersion(int mediaTagVersion) {
    this.mediaTagVersion = mediaTagVersion;
  }

  @Override public String getTitle1() {
    return title1;
  }

  @Override public void setTitle1(String title1) {
    this.title1 = title1;
  }

  @Override public List<IVideo> getVideos() {
    return videos;
  }

  @Override public void setVideos(List<IVideo> videos) {
    this.videos = videos;
  }

  @Override public String getParentPosterURL() {
    return parentPosterURL;
  }

  @Override public void setParentPosterURL(String parentPosterURL) {
    this.parentPosterURL = parentPosterURL;
  }

  @Override public List<ITrack> getTracks() {
    return tracks;
  }

  @Override public void setTracks(List<ITrack> tracks) {
    this.tracks = tracks;
  }

  @Override public String getParentIndex() {
    return parentIndex;
  }

  @Override public void setParentIndex(String parentIndex) {
    this.parentIndex = parentIndex;
  }
}
