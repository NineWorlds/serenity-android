package us.nineworlds.serenity.ui.video.player;

import android.util.Log;
import android.view.View;

public class SkipBackwardOnClickListener implements View.OnClickListener {
	MediaController mediaController;

	/**
	 *
	 */
	public SkipBackwardOnClickListener(MediaController mediaController) {
		this.mediaController = mediaController;
	}

	@Override
	public void onClick(View v) {
		MediaPlayerControl mediaPlayerControl = mediaController
				.getMediaPlayerControl();
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
