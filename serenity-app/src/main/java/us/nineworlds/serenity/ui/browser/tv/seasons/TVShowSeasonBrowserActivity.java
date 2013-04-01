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

package us.nineworlds.serenity.ui.browser.tv.seasons;

import us.nineworlds.serenity.R;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;

/**
 * @author dcarver
 * 
 */
public class TVShowSeasonBrowserActivity extends Activity {

	private Gallery tvShowSeasonsGallery;
	private View tvShowSeasonsMainView;
	private boolean restarted_state = false;
	private String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tvbrowser_show_seasons);

		key = getIntent().getExtras().getString("key");

		tvShowSeasonsMainView = findViewById(R.id.tvshowSeasonBrowserLayout);
		tvShowSeasonsGallery = (Gallery) findViewById(R.id.tvShowSeasonImageGallery);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			setupSeasons();
		}
		restarted_state = false;
	}

	protected void setupSeasons() {

		tvShowSeasonsGallery.setAdapter(new TVShowSeasonImageGalleryAdapter(
				this, key));
		tvShowSeasonsGallery
				.setOnItemSelectedListener(new TVShowSeasonOnItemSelectedListener(
						tvShowSeasonsMainView, this));
		tvShowSeasonsGallery
				.setOnItemClickListener(new TVShowSeasonOnItemClickListener(
						this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
	}

}
