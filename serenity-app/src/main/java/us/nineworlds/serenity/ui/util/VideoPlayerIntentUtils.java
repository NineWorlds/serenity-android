/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayer;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayerFactory;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;

/**
 * @author dcarver
 *
 */
public class VideoPlayerIntentUtils {

	/**
	 * This must run on a UI thread.
	 * 
	 * Launches an external player based on the information provided.
	 * 
	 * @param videoContent
	 * @param vpIntent
	 * @param mxplayer
	 * @param activity
	 */
	public static void launchExternalPlayer(VideoContentInfo videoContent, Activity activity) {
		ExternalPlayerFactory factory = new ExternalPlayerFactory(videoContent, activity);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String externalPlayerValue = preferences.getString("serenity_external_player_filter", "default");
		
		ExternalPlayer extplay = factory.createExternalPlayer(externalPlayerValue);
		try {
			extplay.launch();
		} catch (ActivityNotFoundException ex) {
			extplay = factory.createExternalPlayer("default");
			extplay.launch();
		}
	}


	/**
	 * Play all videos in the queue launching the appropriate player.
	 * 
	 * @param context
	 */
	public static void playAllFromQueue(Activity context) {
		if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			boolean extplayer = prefs.getBoolean("external_player", false);
			boolean extplayerVideoQueue = prefs.getBoolean("external_player_continuous_playback", false);

			
			if (extplayer) {
				if (extplayerVideoQueue) {
					VideoContentInfo videoContent = SerenityApplication.getVideoPlaybackQueue().poll();
					launchExternalPlayer(videoContent, context);
				} else {
					Toast.makeText(context, context.getResources().getString(R.string.external_player_video_queue_support_has_not_been_enabled_), Toast.LENGTH_LONG).show();
				}
			} else {
				Intent vpIntent = new Intent(context,
						SerenitySurfaceViewVideoActivity.class);
				context.startActivityForResult(vpIntent, SerenityConstants.EXIT_PLAYBACK_IMMEDIATELY);
			}
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.queue_is_empty_), Toast.LENGTH_LONG).show();
		}
	}
	
}
