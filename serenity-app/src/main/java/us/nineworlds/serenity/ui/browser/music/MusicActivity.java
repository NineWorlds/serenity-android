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

package us.nineworlds.serenity.ui.browser.music;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.services.CategoryRetrievalIntentService;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import us.nineworlds.serenity.ui.util.DisplayUtils;

/**
 * @author dcarver
 * 
 */
public class MusicActivity extends Activity {

	private static String key;
	private boolean restarted_state = false;
	public static boolean MUSIC_GRIDVIEW = true;
	private static Activity context;
	private static Spinner categorySpinner;
	private Handler categoryHandler = new CategoryHandler();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		key = getIntent().getExtras().getString("key");

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		MUSIC_GRIDVIEW = prefs.getBoolean("music_layout_grid", false);
		
		if (!MUSIC_GRIDVIEW) {
			setContentView(R.layout.activity_music_artist_posters);
		} else {
			setContentView(R.layout.activity_music_artist_gridview);
		}


		final RelativeLayout mainLayout;
		if (MUSIC_GRIDVIEW) {
			mainLayout = (RelativeLayout) findViewById(R.id.musicBrowserBackgroundLayout);
		} else {
			mainLayout = (RelativeLayout) findViewById(R.id.musicArtistBrowserLayout);
		}
		DisplayUtils.overscanCompensation(this, mainLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			setupMusicAdapters();
		}
		restarted_state = false;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
	}

	protected void setupMusicAdapters() {
		
		categoryHandler = new CategoryHandler();
		Messenger messenger = new Messenger(categoryHandler);
		Intent categoriesIntent = new Intent(this,
				CategoryRetrievalIntentService.class);
		categoriesIntent.putExtra("key", key);
		categoriesIntent.putExtra("filterAlbums", true);
		categoriesIntent.putExtra("MESSENGER", messenger);
		startService(categoriesIntent);
		context = this;		
	}
	
	private static class CategoryHandler extends Handler {

		private ArrayList<CategoryInfo> categories;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				categories = (ArrayList<CategoryInfo>) msg.obj;
				setupMovieBrowser();
			}
		}

		/**
		 * Setup the Gallery and Category spinners
		 */
		protected void setupMovieBrowser() {
			ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(
					context, R.layout.serenity_spinner_textview, categories);
			spinnerArrayAdapter
					.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

			categorySpinner = (Spinner) context
					.findViewById(R.id.musicCategoryFilter);
			categorySpinner.setVisibility(View.VISIBLE);
			categorySpinner.setAdapter(spinnerArrayAdapter);
			categorySpinner
					.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener(
							"all", key));
			categorySpinner.requestFocus();
		}

	}
	
}
