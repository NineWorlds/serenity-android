package com.github.kingargyle.plexappclient.ui.browser.movie;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.LoaderSettings.*;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Gallery;

public class MovieBrowserActivity extends Activity {
	
	private Gallery posterGallery;
	private String key;
	private View bgLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");

		setContentView(R.layout.activity_movie_browser);
		
		
		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		
		posterGallery = (Gallery) findViewById(R.id.moviePosterGallery);
		posterGallery.setAdapter(new MoviePosterImageGalleryAdapter(this, key));
		posterGallery.setOnItemSelectedListener(new MoviePosterOnItemSelectedListener(bgLayout, this));
		posterGallery.setOnItemClickListener(new MoviePosterOnItemClickListener());
		
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
