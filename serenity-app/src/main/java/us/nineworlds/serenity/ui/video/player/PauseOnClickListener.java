package us.nineworlds.serenity.ui.video.player;

import android.view.View;

public class PauseOnClickListener implements View.OnClickListener {
  private static final int sDefaultTimeout = 3000;

  MediaController mediaController;

  public PauseOnClickListener(MediaController mediaController) {
    this.mediaController = mediaController;
  }

  @Override public void onClick(View v) {
    doPauseResume();
    mediaController.show(sDefaultTimeout);
  }

  public void doPauseResume() {
    MediaPlayerControl mediaPlayerControl = mediaController.getMediaPlayerControl();
    if (mediaPlayerControl.isPlaying()) {
      mediaPlayerControl.pause();
    } else {
      mediaPlayerControl.start();
    }
  }
}
