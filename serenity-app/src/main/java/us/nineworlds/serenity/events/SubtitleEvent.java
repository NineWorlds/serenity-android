package us.nineworlds.serenity.events;

import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public class SubtitleEvent extends SerenityEvent {

  VideoContentInfo videoContentInfo;

  public SubtitleEvent(IMediaContainer mediaContainer) {
    super(mediaContainer);
  }

  public SubtitleEvent(IMediaContainer mediaContainer, VideoContentInfo videoContentInfo) {
    super(mediaContainer);
    this.videoContentInfo = videoContentInfo;
  }

  public VideoContentInfo getVideoContentInfo() {
    return videoContentInfo;
  }
}
