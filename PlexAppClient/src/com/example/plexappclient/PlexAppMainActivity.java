package com.example.plexappclient;

import com.example.google.tv.leftnavbar.LeftNavBar;
import com.example.google.tv.leftnavbar.LeftNavBarService;
import com.example.plexappclient.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlexAppMainActivity extends Activity {

	private LeftNavBar mLeftNavBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		(LeftNavBarService.instance()).getLeftNavBar((Activity) this);
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

		ActionBar bar = getLeftNavBar();
		bar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.leftnav_bar_background_dark));

		// no navigation
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

	}

}