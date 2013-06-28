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

package us.nineworlds.serenity.ui.listeners;

import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Common class used by both the Poster Gallery view for itemClicks and the
 * Grid View.  It launches video play back when a poster is selected.
 * 
 * @author dcarver
 *
 */
public class AbstractVideoOnItemClickListener {
	
	private SharedPreferences prefs;

	/**
	 * @param v
	 */
	protected void onItemClick(View v) {
		SerenityPosterImageView mpiv = (SerenityPosterImageView) v;
	
		prefs = PreferenceManager
				.getDefaultSharedPreferences(v.getContext());
		boolean externalPlayer = prefs.getBoolean("external_player", false);
		boolean mxplayer = prefs.getBoolean("mxplayer_plex_offset", false);
	
		if (externalPlayer) {
			String url = mpiv.getPosterInfo().getDirectPlayUrl();
			Intent vpIntent = new Intent(Intent.ACTION_VIEW);
			vpIntent.setDataAndType(Uri.parse(url), "video/*");
	
			mxVideoPlayerOptions(mpiv, vpIntent);
			vimuVideoPlayerOptions(mpiv, vpIntent);
	
			Activity activity = (Activity) mpiv.getContext();
			if (SerenityApplication.isGoogleTV(activity) || mxplayer == false) {
				activity.startActivity(vpIntent);
			} else {
				try {
					vpIntent.setPackage("com.mxtech.videoplayer.ad");
					vpIntent.setClassName("com.mxtech.videoplayer.ad","com.mxtech.videoplayer.ad.ActivityScreen");
					activity.startActivityForResult(vpIntent, MainActivity.BROWSER_RESULT_CODE);				
				} catch (ActivityNotFoundException ex) {
					try {
						vpIntent.setPackage("com.mxtech.videoplayer.pro");
						vpIntent.setClassName("com.mxtech.videoplayer.pro","com.mxtech.videoplayer.ActivityScreen");
						activity.startActivityForResult(vpIntent, MainActivity.BROWSER_RESULT_CODE);				
					} catch (ActivityNotFoundException ex2) {
						vpIntent.setPackage(null);
						vpIntent.setComponent(null);
						activity.startActivity(vpIntent);
					}
				}
			}
			
			new WatchedVideoAsyncTask().execute(mpiv.getPosterInfo().id());
			updatedWatchedCount(mpiv, activity);
			return;
		}
	
		Activity a = launchInternalPlayer(mpiv);
		updatedWatchedCount(mpiv, a);
	}

	/**
	 * @param mpiv
	 * @return
	 */
	protected Activity launchInternalPlayer(SerenityPosterImageView mpiv) {
		VideoContentInfo video = mpiv.getPosterInfo();
	
		String url = video.getDirectPlayUrl();
		Intent vpIntent = new Intent(mpiv.getContext(),
				SerenitySurfaceViewVideoActivity.class);
		vpIntent.putExtra("videoUrl", url);
		vpIntent.putExtra("title", video.getTitle());
		vpIntent.putExtra("summary", video.getSummary());
	
		if (video.getGrandParentPosterURL() != null) {
			vpIntent.putExtra("posterUrl", video.getGrandParentPosterURL());
		} else if (video.getParentPosterURL() != null) {
			vpIntent.putExtra("posterUrl", video.getParentPosterURL());
		} else {
			vpIntent.putExtra("posterUrl", video.getImageURL());
		}
		vpIntent.putExtra("id", video.id());
		vpIntent.putExtra("aspectRatio", video.getAspectRatio());
		vpIntent.putExtra("videoResolution", video.getVideoResolution());
		vpIntent.putExtra("audioFormat", video.getAudioCodec());
		vpIntent.putExtra("videoFormat", video.getVideoCodec());
		vpIntent.putExtra("audioChannels", video.getAudioChannels());
		vpIntent.putExtra("resumeOffset", video.getResumeOffset());
		vpIntent.putExtra("duration", video.getDuration());
		vpIntent.putExtra("mediaTagId", video.getMediaTagIdentifier());
		if (video.getSubtitle() != null) {
			Subtitle subtitle = video.getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				vpIntent.putExtra("subtitleURL", subtitle.getKey());
				vpIntent.putExtra("subtitleFormat", subtitle.getFormat());
			}
		}
		
		Activity a = (Activity) mpiv.getContext();
		a.startActivityForResult(vpIntent, MainActivity.BROWSER_RESULT_CODE);
		return a;
	}

	/**
	 * @param epiv
	 * @param a
	 */
	protected void updatedWatchedCount(SerenityPosterImageView epiv, Activity a) {
		int watchedCount = epiv.getPosterInfo().getViewCount();
		epiv.getPosterInfo().setViewCount(watchedCount + 1);
	}

	/**
	 * @param epiv
	 * @param vpIntent
	 */
	protected void vimuVideoPlayerOptions(SerenityPosterImageView epiv, Intent vpIntent) {
		vpIntent.putExtra("forcename", epiv.getPosterInfo().getTitle());
		vpIntent.putExtra("forcedirect", true);
		if (epiv.getPosterInfo().getSubtitle() != null ) {
			Subtitle subtitle = epiv.getPosterInfo().getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				vpIntent.putExtra("forcedsrt", subtitle.getKey());
			}			
		}
	}

	/**
	 * @param epiv
	 * @param vpIntent
	 */
	protected void mxVideoPlayerOptions(SerenityPosterImageView epiv, Intent vpIntent) {
		// MX Video Player options
		vpIntent.putExtra("decode_mode", 1);
		vpIntent.putExtra("title", epiv.getPosterInfo().getTitle());
		vpIntent.putExtra("return_result", true);
		if (epiv.getPosterInfo().getSubtitle() != null ) {
			Subtitle subtitle = epiv.getPosterInfo().getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				Uri[] subt = { Uri.parse(subtitle.getKey()) };
				vpIntent.putExtra("subs", subt);
				vpIntent.putExtra("subs.enable", subt);
			}
		}
		
		boolean usePlexResumeOffset = prefs.getBoolean("mxplayer_plex_offset", false);
		if (usePlexResumeOffset) {
			vpIntent.putExtra("position", epiv.getPosterInfo().getResumeOffset());
		}
		
	}

}
