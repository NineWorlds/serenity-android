package us.nineworlds.serenity.ui.video.player;

import android.util.Log;
import android.view.View;

public class SkipForwardOnClickListener implements View.OnClickListener {
  MediaController mediaController;
  MediaPlayerControl mediaPlayerControl;

  public SkipForwardOnClickListener(MediaController mediaController) {
    this.mediaController = mediaController;
  }

  @Override public void onClick(View v) {
    MediaPlayerControl mediaPlayerControl = mediaController.getMediaPlayerControl();
    if (mediaPlayerControl == null) {
      Log.e(getClass().getName(),
          "Seeking failed due to media player in an illegalstate. MediaPlayerControl is null.");
      return;
    }

    try {
      long skipOffset = 10000 + mediaPlayerControl.getCurrentPosition();
      long duration = mediaPlayerControl.getDuration();
      if (skipOffset > duration) {
        skipOffset = duration - 1;
      }
      mediaPlayerControl.seekTo(skipOffset);
      mediaController.show();
    } catch (IllegalStateException e) {
      Log.d(getClass().getName(), "Seeking failed due to media player in an illegalstate.", e);
    }
  }
}
