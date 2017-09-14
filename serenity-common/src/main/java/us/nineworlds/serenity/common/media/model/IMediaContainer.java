package us.nineworlds.serenity.common.media.model;

import java.util.List;

public interface IMediaContainer {
  String getTitle2();

  void setTitle2(String title2);

  List<IDirectory> getDirectories();

  void setDirectories(List<IDirectory> directory);

  int getSize();

  void setSize(int size);

  int getAllowSync();

  void setAllowSync(int allowSync);

  String getIdentifier();

  void setIdentifier(String identifier);

  String getMediaTagPrefix();

  void setMediaTagPrefix(String mediaTagPrefix);

  long getMediaTagVersion();

  String getArt();

  void setArt(String art);

  int getSortAsc();

  void setSortAsc(int sortAsc);

  String getContent();

  void setContent(String content);

  String getViewGroup();

  void setViewGroup(String viewGroup);

  int getViewMode();

  void setViewMode(int viewMode);

  void setMediaTagVersion(long mediaTagVersion);

  void setMediaTagVersion(int mediaTagVersion);

  String getTitle1();

  void setTitle1(String title1);

  List<IVideo> getVideos();

  void setVideos(List<IVideo> videos);

  String getParentPosterURL();

  void setParentPosterURL(String parentPosterURL);

  List<ITrack> getTracks();

  void setTracks(List<ITrack> tracks);

  String getParentIndex();

  void setParentIndex(String parentIndex);
}
