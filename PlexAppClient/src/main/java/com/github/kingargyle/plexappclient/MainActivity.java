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

package com.github.kingargyle.plexappclient;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.core.ServerConfig;
import com.github.kingargyle.plexappclient.ui.activity.SerenityActivity;
import com.github.kingargyle.plexappclient.ui.preferences.SerenityPreferenceActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SerenityActivity {

	private Gallery mainGallery;
	private View mainView;
	private SharedPreferences preferences;
	public static int MAIN_MENU_PREFERENCE_RESULT_CODE = 100;
	public static int BROWSER_RESULT_CODE = 200;
	private boolean restarted_state = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_plex_app_main);
		mainView = findViewById(R.id.mainLayout);
		mainGallery = (Gallery) findViewById(R.id.mainGalleryMenu);	
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(((ServerConfig)ServerConfig.getInstance()).getServerConfigChangeListener());
		
		boolean googletv = SerenityApplication.isGoogleTV(this);
		if (!googletv) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("external_player", true);
			editor.commit();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plex_app_main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent(MainActivity.this,
				SerenityPreferenceActivity.class);
		startActivityForResult(i, 0);

		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// Hey! This refreshes the whole Activity!
		//this.onCreate(null);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			setupGallery();
		}
		restarted_state = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		restarted_state = true;
		super.onRestart();
		
	}

	/**
	 * Refresh the screen after coming back from the preferences screen.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MAIN_MENU_PREFERENCE_RESULT_CODE) {
			setupGallery();
		}
		
	}
	

	private void setupGallery() {
		mainGallery.setAdapter(new MainMenuTextViewAdapter(this, mainView));
		mainGallery
				.setOnItemSelectedListener(new GalleryOnItemSelectedListener(
						mainView));
		mainGallery
				.setOnItemClickListener(new GalleryOnItemClickListener(this));
	}
	
}
