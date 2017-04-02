package us.nineworlds.serenity.events;


import us.nineworlds.plex.rest.model.impl.MediaContainer;

/**
 * Created by dcarver on 4/2/17.
 */

public class MainMenuEvent extends SerenityEvent {

    public MainMenuEvent(MediaContainer mediaContainer) {
        super(mediaContainer);
    }
}
