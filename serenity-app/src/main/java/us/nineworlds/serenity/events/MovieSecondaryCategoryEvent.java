package us.nineworlds.serenity.events;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class MovieSecondaryCategoryEvent extends SerenityEvent {

    @Inject
    EventBus eventBus;

    String key;
    String category;

    public MovieSecondaryCategoryEvent(MediaContainer mediaContainer, String key, String category) {
        super(mediaContainer);

        this.key = key;
        this.category = category;
    }

    public String getKey() {
        return key;
    }
}
