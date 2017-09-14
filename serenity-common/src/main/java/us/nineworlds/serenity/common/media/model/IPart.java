package us.nineworlds.serenity.common.media.model;

import java.util.List;

public interface IPart {
  List<IStream> getStreams();

  void setStreams(List<IStream> streams);

  String getContainer();

  void setContainer(String container);

  String getKey();

  void setKey(String key);

  String getFilename();

  void setFilename(String filename);
}
