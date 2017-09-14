package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class MovieSecondaryCategoryEvent extends SerenityEvent {

  private String key;
  private String category;

  public MovieSecondaryCategoryEvent(IMediaContainer mediaContainer, String key, String category) {
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
