package us.nineworlds.serenity.ui.video.player;

import android.view.View;

public class PauseOnClickListener implements View.OnClickListener {
	private static final int sDefaultTimeout = 3000;

	MediaController mediaController;
	MediaPlayerControl mediaPlayerControl;

	public PauseOnClickListener(MediaController mediaController) {
		this.mediaController = mediaController;
		mediaPlayerControl = this.mediaController.getMediaPlayerControl();
	}

	@Override
	public void onClick(View v) {
		doPauseResume();
		mediaController.show(sDefaultTimeout);
	}

	public void doPauseResume() {
		if (mediaPlayerControl.isPlaying()) {
			mediaPlayerControl.pause();
		} else {
			mediaPlayerControl.start();
		}
	}

}
