package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class EpisodesRetrievalEvent extends SerenityEvent {

  public EpisodesRetrievalEvent(IMediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
