package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class MovieRetrievalEvent extends SerenityEvent {

  public MovieRetrievalEvent(IMediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
