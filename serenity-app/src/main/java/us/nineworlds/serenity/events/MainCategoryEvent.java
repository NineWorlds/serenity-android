package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class MainCategoryEvent extends SerenityEvent {

    String key;

    public MainCategoryEvent(MediaContainer mediaContainer, String key) {
        super(mediaContainer);

        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
