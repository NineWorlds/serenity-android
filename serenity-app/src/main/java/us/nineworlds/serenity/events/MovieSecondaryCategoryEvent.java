package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

public class MovieSecondaryCategoryEvent extends SerenityEvent {

  private String key;
  private String category;

  public MovieSecondaryCategoryEvent(MediaContainer mediaContainer, String key, String category) {
    super(mediaContainer);

    this.key = key;
    this.category = category;
  }

  public String getKey() {
    return key;
  }

  public String getCategory() {
    return category;
  }
}
