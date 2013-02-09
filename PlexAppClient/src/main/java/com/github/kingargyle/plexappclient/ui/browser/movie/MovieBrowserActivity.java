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

package com.github.kingargyle.plexappclient.ui.browser.movie;

import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.CategoryInfo;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.Spinner;

public class MovieBrowserActivity extends Activity {
	
	private Gallery posterGallery;
	private String key;
	private View bgLayout;
	private Spinner categorySpinner;
	private boolean restarted_state = false;
	private ArrayList<CategoryInfo> categories;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");
		setContentView(R.layout.activity_movie_browser);

		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		posterGallery = (Gallery) findViewById(R.id.moviePosterGallery);
		
		populateCategories();

	}
	
	protected void populateCategories() {
		PlexappFactory factory = SerenityApplication.getPlexFactory();
		try {
			MediaContainer mediaContainer = factory.retrieveSections(key);
			List<Directory> dirs = mediaContainer.getDirectories();
			categories = new ArrayList<CategoryInfo>();
			for(Directory dir : dirs) {
				if (!"folder".equals(dir.getKey()) && !"Search...".equals(dir.getTitle())) {
					CategoryInfo category = new CategoryInfo();
					category.setCategory(dir.getKey());
					category.setCategoryDetail(dir.getTitle());
					if (dir.getSecondary() > 0) {
						category.setLevel(dir.getSecondary());
					}
					categories.add(category);
				}
			}
		} catch (Exception e) {
			Log.e("MovieBrowserActivity", e.getMessage(), e);
		}		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (restarted_state == false) {
			setupMovieBrowser();
		}
		restarted_state = false;
	}


	/**
	 * Setup the Gallery and Category spinners 
	 */
	protected void setupMovieBrowser() {
		posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(this, key, "all"));
		posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, this));
		posterGallery.setOnItemClickListener(new MoviePosterOnItemClickListener());
		
		ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(this, R.layout.serenity_spinner_textview, categories);
		spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
		
		categorySpinner =(Spinner) findViewById(R.id.movieCategoryFilter);
		categorySpinner.setAdapter(spinnerArrayAdapter);
		categorySpinner.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener("all", key));
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movie_browser, menu);
		return true;
	}
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
	}
	
}
