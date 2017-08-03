package us.nineworlds.serenity.events;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public class SubtitleEvent extends SerenityEvent {

  VideoContentInfo videoContentInfo;

  public SubtitleEvent(MediaContainer mediaContainer) {
    super(mediaContainer);
  }

  public SubtitleEvent(MediaContainer mediaContainer, VideoContentInfo videoContentInfo) {
    super(mediaContainer);
    this.videoContentInfo = videoContentInfo;
  }

  public VideoContentInfo getVideoContentInfo() {
    return videoContentInfo;
  }
}
