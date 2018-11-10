package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

/**
 * Created by dcarver on 4/2/17.
 */

public class MainMenuEvent extends SerenityEvent {

  public MainMenuEvent(IMediaContainer mediaContainer) {
    super(mediaContainer);
  }
}
