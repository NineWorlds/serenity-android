package us.nineworlds.serenity.common.media.model;

import java.util.List;

public interface ITrack {
  String getKey();

  void setKey(String key);

  String getTitle();

  void setTitle(String title);

  String getSummary();

  void setSummary(String summary);

  String getType();

  void setType(String type);

  String getParentKey();

  void setParentKey(String parentKey);

  int getIndex();

  void setIndex(int index);

  long getDuration();

  void setDuration(long duration);

  long getTimeAdded();

  void setTimeAdded(long timeAdded);

  long getTimeUpdated();

  void setTimeUpdated(long timeUpdated);

  List<IMedia> getMedias();

  void setMedias(List<IMedia> medias);
}
