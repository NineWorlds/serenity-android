package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class TVCategoryEvent extends MainCategoryEvent {

  public TVCategoryEvent(IMediaContainer mediaContainer, String key) {
    super(mediaContainer, key);
  }
}
