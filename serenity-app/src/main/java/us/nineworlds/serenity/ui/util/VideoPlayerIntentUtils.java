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
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;

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
	public static void launchExternalPlayer(VideoContentInfo videoContent, boolean mxplayer, Activity activity) {
		String url = videoContent.getDirectPlayUrl();
		Intent vpIntent = new Intent(Intent.ACTION_VIEW);
		vpIntent.setDataAndType(Uri.parse(url), "video/*");

		mxVideoPlayerOptions(videoContent, vpIntent);
		vimuVideoPlayerOptions(videoContent, vpIntent);
		// MX Player and VLC seem to use the same optional extra
		vpIntent.putExtra("position", (long) videoContent.getResumeOffset());

		if (SerenityApplication.isGoogleTV(activity) || mxplayer == false) {
			activity.startActivityForResult(vpIntent, SerenityConstants.BROWSER_RESULT_CODE);
		} else {
			try {
				vpIntent.setPackage("com.mxtech.videoplayer.ad");
				vpIntent.setClassName("com.mxtech.videoplayer.ad","com.mxtech.videoplayer.ad.ActivityScreen");
				activity.startActivityForResult(vpIntent, SerenityConstants.BROWSER_RESULT_CODE);				
			} catch (ActivityNotFoundException ex) {
				try {
					vpIntent.setPackage("com.mxtech.videoplayer.pro");
					vpIntent.setClassName("com.mxtech.videoplayer.pro","com.mxtech.videoplayer.ActivityScreen");
					activity.startActivityForResult(vpIntent, SerenityConstants.BROWSER_RESULT_CODE);				
				} catch (ActivityNotFoundException ex2) {
					vpIntent.setPackage(null);
					vpIntent.setComponent(null);
					activity.startActivity(vpIntent);
				}
			}
		}
		
	}
	

	/**
	 * @param epiv
	 * @param vpIntent
	 */
	protected static void mxVideoPlayerOptions(VideoContentInfo videoContent, Intent vpIntent) {
		// MX Video Player options
		vpIntent.putExtra("decode_mode", 1);
		vpIntent.putExtra("title", videoContent.getTitle());
		vpIntent.putExtra("return_result", true);
		if (videoContent.getSubtitle() != null ) {
			Subtitle subtitle = videoContent.getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				Uri[] subt = { Uri.parse(subtitle.getKey()) };
				vpIntent.putExtra("subs", subt);
				vpIntent.putExtra("subs.enable", subt);
			}
		}
		vpIntent.putExtra("position", videoContent.getResumeOffset());
	}
	
	/**
	 * @param epiv
	 * @param vpIntent
	 */
	protected static void vimuVideoPlayerOptions(VideoContentInfo videoContent, Intent vpIntent) {
		vpIntent.putExtra("forcename", videoContent.getTitle());
		vpIntent.putExtra("forcedirect", true);
		if (videoContent.getSubtitle() != null ) {
			Subtitle subtitle = videoContent.getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				vpIntent.putExtra("forcedsrt", subtitle.getKey());
			}			
		}
	}


	
}
