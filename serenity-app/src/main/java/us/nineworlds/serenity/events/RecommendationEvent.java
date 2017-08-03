package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class RecommendationEvent extends SerenityEvent {

  public RecommendationEvent(MediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
