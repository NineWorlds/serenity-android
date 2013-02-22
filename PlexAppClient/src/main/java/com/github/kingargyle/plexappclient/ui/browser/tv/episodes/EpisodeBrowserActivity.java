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

package com.github.kingargyle.plexappclient.ui.browser.tv.episodes;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.ui.activity.SerenityActivity;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Gallery;

public class EpisodeBrowserActivity extends SerenityActivity {
	
	private Gallery posterGallery;
	private String key;
	private View bgLayout;
	private View metaData;
	private boolean restarted_state = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");

		setContentView(R.layout.activity_movie_browser);
		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		posterGallery = (Gallery) findViewById(R.id.moviePosterGallery);
		metaData = findViewById(R.id.metaDataRow);
		metaData.setVisibility(View.INVISIBLE);
				
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (restarted_state == false) {
			setupEpisodeBrowser();
		}
		restarted_state = false;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}


	/**
	 * Populate the episode browser with data
	 */
	protected void setupEpisodeBrowser() {
		posterGallery.setAdapter(new EpisodePosterImageGalleryAdapter(this, key));
		posterGallery.setOnItemSelectedListener(new EpisodePosterOnItemSelectedListener(bgLayout, this));
		posterGallery.setOnItemClickListener(new EpisodePosterOnItemClickListener());
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movie_browser, menu);
		return true;
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
	}
		
}
