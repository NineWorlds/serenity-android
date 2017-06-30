package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class EpisodesRetrievalEvent extends SerenityEvent {

    public EpisodesRetrievalEvent(MediaContainer mediaContainer) {
        super(mediaContainer);
    }
}
