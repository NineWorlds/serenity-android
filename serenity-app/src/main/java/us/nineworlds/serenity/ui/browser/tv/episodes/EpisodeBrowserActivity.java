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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class EpisodeBrowserActivity extends SerenityVideoActivity {

	private SerenityGallery posterGallery;
	private String key;
	private View bgLayout;
	private View metaData;
	private boolean restarted_state = false;
	private SharedPreferences prefs;

	@Override
	protected void createSideMenu() {
		setContentView(R.layout.activity_movie_browser);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");
		
		createSideMenu();

		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		posterGallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
		metaData = findViewById(R.id.metaDataRow);
		metaData.setVisibility(View.VISIBLE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (restarted_state == false) {
			setupEpisodeBrowser();
		}
		restarted_state = false;
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
	
	/**
	 * Populate the episode browser with data
	 */
	protected void setupEpisodeBrowser() {
		boolean scrollingAnimation = prefs.getBoolean("animation_gallery_scrolling", true);
		posterGallery
				.setAdapter(new EpisodePosterImageGalleryAdapter(this, key));
		posterGallery
				.setOnItemSelectedListener(new EpisodePosterOnItemSelectedListener(this));
		posterGallery
				.setOnItemClickListener(new GalleryVideoOnItemClickListener());
		if (key.contains("onDeck") || key.contains("recentlyAdded") || (key.contains("recentlyViewed") && !key.contains("recentlyViewedShows"))) {
			posterGallery
			.setOnItemLongClickListener(new EpisodeBrowserOnLongClickListener());
		} else {
			posterGallery
			.setOnItemLongClickListener(new GalleryVideoOnItemLongClickListener());
		}
		if (scrollingAnimation) {
			posterGallery.setAnimationDuration(220);
		} else {
			posterGallery.setAnimationDuration(1);
		}
		posterGallery.setSpacing(25);
		posterGallery.setCallbackDuringFling(false);
		posterGallery.setFocusableInTouchMode(false);
		posterGallery.setDrawingCacheEnabled(true);
		posterGallery.setHorizontalFadingEdgeEnabled(true);
		posterGallery.setUnselectedAlpha(0.75f);
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
		if (key != null && key.contains("onDeck")) {
			restarted_state = false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return;
		}
		restarted_state = true;
	}

}
