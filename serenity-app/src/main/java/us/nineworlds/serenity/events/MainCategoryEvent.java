package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;

public class MainCategoryEvent extends SerenityEvent {

  String key;

  public MainCategoryEvent(IMediaContainer mediaContainer, String key) {
    super(mediaContainer);

    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
