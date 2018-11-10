package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class SeasonsRetrievalEvent extends SerenityEvent {

  public SeasonsRetrievalEvent(IMediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
