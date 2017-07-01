package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class TVShowRetrievalEvent extends SerenityEvent {

    public TVShowRetrievalEvent(MediaContainer mediaContainer) {
        super(mediaContainer);
    }
}
