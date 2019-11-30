package us.nineworlds.serenity.emby.model;

import androidx.annotation.Nullable;
import java.util.List;
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.media.model.ITrack;
import us.nineworlds.serenity.common.media.model.IVideo;

public class MediaContainer implements IMediaContainer {
  private String title1;
  private String title2;
  private List<IDirectory> directoryList;
  private int size;
  private int allowSync;
  private String id;
  private String mediaTagPrefix;
  private long mediaTagVersion;
  private List<IVideo> videoList;
  private String art;
  private int sortAsc;
  private String content;
  private String viewGroup;
  private int viewMode;
  private String parentPosterUrl;
  private List<ITrack> tracks;
  private String parentIndex;

  @Nullable @Override public String getTitle2() {
    return title2;
  }

  @Override public void setTitle2(@Nullable String title2) {
    this.title2 = title2;
  }

  @Nullable @Override public List<IDirectory> getDirectories() {
    return directoryList;
  }

  @Override public void setDirectories(List<IDirectory> directory) {
    this.directoryList = directory;
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

  @Nullable @Override public String getIdentifier() {
    return id;
  }

  @Override public void setIdentifier(@Nullable String identifier) {
    this.id = identifier;
  }

  @Nullable @Override public String getMediaTagPrefix() {
    return mediaTagPrefix;
  }

  @Override public void setMediaTagPrefix(String mediaTagPrefix) {
    this.mediaTagPrefix = mediaTagPrefix;
  }

  @Override public long getMediaTagVersion() {
    return mediaTagVersion;
  }

  @Nullable @Override public String getArt() {
    return art;
  }

  @Override public void setArt(@Nullable String art) {
    this.art = art;
  }

  @Override public int getSortAsc() {
    return sortAsc;
  }

  @Override public void setSortAsc(int sortAsc) {
    this.sortAsc = sortAsc;
  }

  @Nullable @Override public String getContent() {
    return content;
  }

  @Override public void setContent(@Nullable String content) {
    this.content = content;
  }

  @Override public String getViewGroup() {
    return viewGroup;
  }

  @Override public void setViewGroup(@Nullable String viewGroup) {
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

  @Nullable @Override public String getTitle1() {
    return title1;
  }

  @Override public void setTitle1(String title1) {
    this.title1 = title1;
  }

  @Override public List<IVideo> getVideos() {
    return videoList;
  }

  @Override public void setVideos(@Nullable List<IVideo> videos) {
    this.videoList = videos;
  }

  @Nullable @Override public String getParentPosterURL() {
    return parentPosterUrl;
  }

  @Override public void setParentPosterURL(@Nullable String parentPosterURL) {
    this.parentPosterUrl = parentPosterURL;
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
