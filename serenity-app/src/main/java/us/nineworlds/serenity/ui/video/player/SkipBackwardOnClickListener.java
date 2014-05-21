package us.nineworlds.serenity.ui.video.player;

import android.util.Log;
import android.view.View;

public class SkipBackwardOnClickListener implements View.OnClickListener {
	MediaController mediaController;
	MediaPlayerControl mediaPlayerControl;

	/**
	 * 
	 */
	public SkipBackwardOnClickListener(MediaController mediaController) {
		this.mediaController = mediaController;
		mediaPlayerControl = mediaController.getMediaPlayerControl();
	}

	@Override
	public void onClick(View v) {
		try {
			long skipOffset = mediaPlayerControl.getCurrentPosition() - 10000;
			if (skipOffset < 0) {
				skipOffset = 1;
			}
			mediaPlayerControl.seekTo(skipOffset);
			mediaController.show();
		} catch (IllegalStateException e) {
			Log.d(getClass().getName(),
					"Seeking failed due to media player in an illegalstate.", e);
		}
	}
}
