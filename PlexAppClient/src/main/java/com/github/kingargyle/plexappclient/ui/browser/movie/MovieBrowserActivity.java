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

import com.github.kingargyle.plexappclient.R;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.Spinner;

public class MovieBrowserActivity extends Activity {
	
	private Gallery posterGallery;
	private String key;
	private View bgLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		String categories[] = { "All",
				"Unwatched",
				"Newest",
				"On Deck",
				"Genre",
				"Year",
				"Decade",
				"Director",
				"Actor",
				"Country",
				"Content Rating",
				"Rating",
				"Resolution",
				"First Letter"};
		
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");

		setContentView(R.layout.activity_movie_browser);
		
		
		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		
		posterGallery = (Gallery) findViewById(R.id.moviePosterGallery);
		posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(this, key));
		posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, this));
		posterGallery.setOnItemClickListener(new MoviePosterOnItemClickListener());
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.serenity_spinner_textview, categories);
		spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
		Spinner categorySpinner =(Spinner) findViewById(R.id.movieCategoryFilter);
		categorySpinner.setAdapter(spinnerArrayAdapter);
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movie_browser, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(this, key));
		posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, this));
		posterGallery.setOnItemClickListener(new MoviePosterOnItemClickListener());		
	}
	
}
