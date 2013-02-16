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
			Activity activity = (Activity) epiv.getContext();
			activity.startActivity(vpIntent);			
			return;
		}
		
		
		String url = epiv.getPosterInfo().getDirectPlayUrl();
		Intent vpIntent = new Intent(epiv.getContext(), SerenitySurfaceViewVideoActivity.class);
		vpIntent.putExtra("videoUrl", url);
		vpIntent.putExtra("summary", epiv.getPosterInfo().getPlotSummary());
		vpIntent.putExtra("title", epiv.getPosterInfo().getTitle());
		vpIntent.putExtra("posterUrl", epiv.getPosterInfo().getPosterURL());
		vpIntent.putExtra("id", epiv.getPosterInfo().id());
		vpIntent.putExtra("aspectRatio", epiv.getPosterInfo().getAspectRatio());
		
		Activity a = (Activity) epiv.getContext();
		a.startActivityForResult(vpIntent, 0);
	}

}
