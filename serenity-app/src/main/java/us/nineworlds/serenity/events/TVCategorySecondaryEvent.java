package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class TVCategorySecondaryEvent extends MainCategoryEvent {

  private String category;

  public TVCategorySecondaryEvent(MediaContainer mediaContainer, String key, String category) {
    super(mediaContainer, key);
    this.category = category;
  }

  public String getCategory() {
    return category;
  }
}
