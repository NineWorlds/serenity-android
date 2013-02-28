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

package com.github.kingargyle.plexappclient.ui.browser.tv.episodes;

import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.ui.video.player.SerenitySurfaceViewVideoActivity;
import com.github.kingargyle.plexappclient.ui.views.SerenityPosterImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.github.kingargyle.plexappclient.core.services.*;

/**
 * @author dcarver
 *
 */
public class EpisodePosterOnItemClickListener  implements OnItemClickListener {

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
		SerenityPosterImageView epiv = (SerenityPosterImageView) v;
	
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		boolean externalPlayer = prefs.getBoolean("external_player", false);
		
		if (externalPlayer) {
			String url = epiv.getPosterInfo().getDirectPlayUrl();
			Intent vpIntent = new Intent(Intent.ACTION_VIEW);
			vpIntent.setDataAndType(Uri.parse(url), "video/*");
			
			mxVideoPlayerOptions(epiv, vpIntent);
			vimuVideoPlayerOptions(epiv, vpIntent);
			
			Activity activity = (Activity) epiv.getContext();
			activity.startActivity(vpIntent);
			new WatchedEpisodeAsyncTask().execute(epiv.getPosterInfo().id());
			return;
		}
		
		
		String url = epiv.getPosterInfo().getDirectPlayUrl();
		Intent vpIntent = new Intent(epiv.getContext(), SerenitySurfaceViewVideoActivity.class);
		vpIntent.putExtra("videoUrl", url);
		vpIntent.putExtra("summary", epiv.getPosterInfo().getPlotSummary());
		vpIntent.putExtra("title", epiv.getPosterInfo().getTitle());
		String posterUrl = epiv.getPosterInfo().getParentPosterURL();
		vpIntent.putExtra("posterUrl", posterUrl);
		vpIntent.putExtra("id", epiv.getPosterInfo().id());
		vpIntent.putExtra("aspectRatio", epiv.getPosterInfo().getAspectRatio());
		vpIntent.putExtra("videoResolution", epiv.getPosterInfo().getVideoResolution());
		vpIntent.putExtra("audioFormat", epiv.getPosterInfo().getAudioCodec());
		vpIntent.putExtra("videoFormat", epiv.getPosterInfo().getVideoCodec());
		vpIntent.putExtra("audioChannels", epiv.getPosterInfo().getAudioChannels());
		vpIntent.putExtra("resumeOffset", epiv.getPosterInfo().getResumeOffset());
		
		Activity a = (Activity) epiv.getContext();
		a.startActivityForResult(vpIntent, 0);
	}

	/**
	 * @param epiv
	 * @param vpIntent
	 */
	protected void vimuVideoPlayerOptions(SerenityPosterImageView epiv,
			Intent vpIntent) {
		vpIntent.putExtra("forcename", epiv.getPosterInfo().getTitle());
		vpIntent.putExtra("forcedirect", true);
	}

	/**
	 * @param epiv
	 * @param vpIntent
	 */
	protected void mxVideoPlayerOptions(SerenityPosterImageView epiv,
			Intent vpIntent) {
		// MX Video Player options
		vpIntent.putExtra("decode_mode", 1);
		vpIntent.putExtra("title", epiv.getPosterInfo().getTitle());
		vpIntent.putExtra("return_result", true);
	}
	

}
