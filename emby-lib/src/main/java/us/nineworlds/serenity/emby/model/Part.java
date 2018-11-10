package us.nineworlds.serenity.emby.model;

import java.util.List;
import us.nineworlds.serenity.common.media.model.IPart;
import us.nineworlds.serenity.common.media.model.IStream;

public class Part implements IPart {

  private String key;
  private String filename;
  private String container;
  private List<IStream> streams;

  @Override public List<IStream> getStreams() {
    return streams;
  }

  @Override public void setStreams(List<IStream> streams) {
    this.streams = streams;
  }

  @Override public String getContainer() {
    return container;
  }

  @Override public void setContainer(String container) {
    this.container = container;
  }

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }

  @Override public String getFilename() {
    return filename;
  }

  @Override public void setFilename(String filename) {
    this.filename = filename;
  }
}
