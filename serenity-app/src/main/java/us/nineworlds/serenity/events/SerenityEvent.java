package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class SerenityEvent {

  MediaContainer mediaContainer;

  public SerenityEvent(MediaContainer mediaContainer) {
    this.mediaContainer = mediaContainer;
  }

  public MediaContainer getMediaContainer() {
    return mediaContainer;
  }
}
