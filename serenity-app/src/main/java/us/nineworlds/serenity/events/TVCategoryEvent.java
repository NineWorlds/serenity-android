package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class TVCategoryEvent extends MainCategoryEvent {

  public TVCategoryEvent(MediaContainer mediaContainer, String key) {
    super(mediaContainer, key);
  }
}
