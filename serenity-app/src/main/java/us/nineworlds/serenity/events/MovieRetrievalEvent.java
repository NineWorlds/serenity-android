package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class MovieRetrievalEvent extends SerenityEvent {

  public MovieRetrievalEvent(MediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
