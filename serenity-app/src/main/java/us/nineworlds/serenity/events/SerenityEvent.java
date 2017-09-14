package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class SerenityEvent {

  IMediaContainer mediaContainer;

  public SerenityEvent(IMediaContainer mediaContainer) {
    this.mediaContainer = mediaContainer;
  }

  public IMediaContainer getMediaContainer() {
    return mediaContainer;
  }
}
