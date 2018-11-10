package us.nineworlds.serenity.emby.model;

import us.nineworlds.serenity.common.media.model.ICrew;

public abstract class AbstractCrew implements ICrew {

  private String tag;

  @Override public String getTag() {
    return tag;
  }

  @Override public void setTag(String tag) {
    this.tag = tag;
  }

}