package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class TVCategorySecondaryEvent extends MainCategoryEvent {

  private String category;

  public TVCategorySecondaryEvent(IMediaContainer mediaContainer, String key, String category) {
    super(mediaContainer, key);
    this.category = category;
  }

  public String getCategory() {
    return category;
  }
}
