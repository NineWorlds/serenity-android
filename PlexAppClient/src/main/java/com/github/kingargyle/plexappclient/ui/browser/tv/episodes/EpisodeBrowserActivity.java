package com.github.kingargyle.plexappclient.ui.browser.tv.episodes;

import com.github.kingargyle.plexappclient.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Gallery;

public class EpisodeBrowserActivity extends Activity {
	
	private Gallery posterGallery;
	private String key;
	private View bgLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");

		setContentView(R.layout.activity_episode_browser);
		
		
		bgLayout = findViewById(R.id.episodeBrowserBackgroundLayout);
		
		posterGallery = (Gallery) findViewById(R.id.episodePosterGallery);
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		posterGallery.setAdapter(new EpisodePosterImageGalleryAdapter(this, key));
		posterGallery.setOnItemSelectedListener(new EpisodePosterOnItemSelectedListener(bgLayout, this));
		posterGallery.setOnItemClickListener(new EpisodePosterOnItemClickListener());		
	}
	
}
