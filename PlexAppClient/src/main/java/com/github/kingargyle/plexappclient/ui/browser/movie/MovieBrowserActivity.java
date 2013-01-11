package com.github.kingargyle.plexappclient.ui.browser.movie;

import com.github.kingargyle.plexappclient.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Gallery;

public class MovieBrowserActivity extends Activity {
	
	private Gallery posterGallery;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_browser);
		View bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		
		posterGallery = (Gallery) findViewById(R.id.moviePosterGallery);
		posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(this));
		posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movie_browser, menu);
		return true;
	}

}
