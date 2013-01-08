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

package com.example.plexappclient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.google.tv.leftnavbar.LeftNavBar;
import com.example.google.tv.leftnavbar.LeftNavBarService;
import com.example.google.tv.leftnavbar.LeftNavView;
import com.example.google.tv.leftnavbar.TabListView;
import com.example.plexappclient.R;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class PlexAppMainActivity extends Activity {

	private LeftNavBar mLeftNavBar;
	private ImageView mainImageView;
	private LeftNavView leftNavView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Yes I know this is bad, really need to make network activity happen in AsyncTask.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		(LeftNavBarService.instance()).getLeftNavBar(this);
		setContentView(R.layout.activity_plex_app_main);

		// prepare the left navigation bar
		setupBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plex_app_main, menu);
		return true;
	}

	private LeftNavBar getLeftNavBar() {
		if (mLeftNavBar == null) {
			mLeftNavBar = new LeftNavBar(this);
			mLeftNavBar.setOnClickHomeListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// This is called when the app icon is selected in the left
					// navigation bar
				}
			});
		}
		return mLeftNavBar;
	}

	private void flipOption(int option) {
		ActionBar bar = getLeftNavBar();
		int options = bar.getDisplayOptions();
		boolean hadOption = (options & option) != 0;
		bar.setDisplayOptions(hadOption ? 0 : option, option);
	}

	private void setupBar() {
		
		mainImageView = (ImageView) findViewById(R.id.sectionImageView);

		LeftNavBar bar = getLeftNavBar();
			
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.leftnav_bar_background_dark));
		
		Tab moviesTab = bar.newTab();
		moviesTab.setText("Movies");
		moviesTab.setTabListener(new MovieListListener(mainImageView));
		bar.addTab(moviesTab);
		
		Tab tvshowsTab = bar.newTab();
		tvshowsTab.setText("TV Shows");
		tvshowsTab.setTabListener(new TVShowsListListener(mainImageView));
		bar.addTab(tvshowsTab);

		// no navigation
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	public static Bitmap getBitmapFromURL(String src) {  
        try {

            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap mybitmap = BitmapFactory.decodeStream(input);

            return mybitmap;

        } catch (Exception ex) {
        	
            return null;
        }	

	}
	
	
	
}