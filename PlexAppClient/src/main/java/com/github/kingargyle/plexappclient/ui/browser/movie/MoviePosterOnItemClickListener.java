/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package com.github.kingargyle.plexappclient.ui.browser.movie;

import com.github.kingargyle.plexappclient.MainActivity;
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

/**
 * @author dcarver
 *
 */
public class MoviePosterOnItemClickListener  implements OnItemClickListener {

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
		SerenityPosterImageView mpiv = (SerenityPosterImageView) v;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		boolean externalPlayer = prefs.getBoolean("external_player", false);
		
		if (externalPlayer) {
			String url = mpiv.getPosterInfo().getDirectPlayUrl();
			Intent vpIntent = new Intent(Intent.ACTION_VIEW);
			vpIntent.setDataAndType(Uri.parse(url), "video/*");
			Activity activity = (Activity) mpiv.getContext();
			activity.startActivity(vpIntent);			
			return;
		}
		
		String url = mpiv.getPosterInfo().getDirectPlayUrl();
		Intent vpIntent = new Intent(mpiv.getContext(), SerenitySurfaceViewVideoActivity.class);
		vpIntent.putExtra("videoUrl", url);
		vpIntent.putExtra("title", mpiv.getPosterInfo().getTitle());
		vpIntent.putExtra("summary", mpiv.getPosterInfo().getPlotSummary());
		vpIntent.putExtra("posterUrl", mpiv.getPosterInfo().getPosterURL());
		vpIntent.putExtra("id", mpiv.getPosterInfo().id());
		vpIntent.putExtra("aspectRatio", mpiv.getPosterInfo().getAspectRatio());
		vpIntent.putExtra("videoResolution", mpiv.getPosterInfo().getVideoResolution());
		vpIntent.putExtra("audioFormat", mpiv.getPosterInfo().getAudioCodec());
		vpIntent.putExtra("videoFormat", mpiv.getPosterInfo().getVideoCodec());
		vpIntent.putExtra("audioChannels", mpiv.getPosterInfo().getAudioChannels());
		
		
		Activity a = (Activity) mpiv.getContext();
		a.startActivityForResult(vpIntent, MainActivity.BROWSER_RESULT_CODE);
	}

}
