package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class SeasonsRetrievalEvent extends SerenityEvent {

  public SeasonsRetrievalEvent(MediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
