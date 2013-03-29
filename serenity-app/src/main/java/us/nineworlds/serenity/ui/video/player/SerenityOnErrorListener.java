package us.nineworlds.serenity.ui.video.player;

import com.google.analytics.tracking.android.EasyTracker;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class SerenityOnErrorListener implements OnErrorListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.media.MediaPlayer.OnErrorListener#onError(android.media.
	 * MediaPlayer, int, int)
	 */
	public boolean onError(MediaPlayer mp, int what, int extra) {
		String error_msg = "What: " + what + "Extra: " + extra;
		if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
			error_msg = "Unknown Media Player Error. Extra Code: " + extra;
		}

		if (what == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
			error_msg = "Media not valid for progessive playback. Extra Code: "
					+ extra;
		}

		if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			error_msg = "Server croaked. Extra Code: " + extra;
		}

		EasyTracker.getTracker().sendEvent("Video Error", "Playback",
				error_msg, (long) 0);

		Log.e(getClass().getName(), error_msg);
		return true;
	}

}
