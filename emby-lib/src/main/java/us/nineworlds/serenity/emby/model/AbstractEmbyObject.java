package us.nineworlds.serenity.emby.model;

import us.nineworlds.serenity.common.media.model.ClientObject;

public abstract class AbstractEmbyObject implements ClientObject {

  private String key;

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }
}