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

package us.nineworlds.serenity;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.services.GDMService;
import us.nineworlds.serenity.handlers.AutoConfigureHandlerRunnable;
import us.nineworlds.serenity.handlers.DownloadHandler;
import us.nineworlds.serenity.handlers.DownloadHandler.DownloadServiceConnection;
import us.nineworlds.serenity.ui.activity.SerenityDrawerLayoutActivity;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.ListView;

import com.castillo.dd.DownloadService;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SerenityDrawerLayoutActivity {

	public static int MAIN_MENU_PREFERENCE_RESULT_CODE = 100;

	protected Handler autoConfigureHandler = new Handler();

	protected DownloadHandler downloadHandler;
	private DownloadServiceConnection downloadService;
	private final BroadcastReceiver gdmReciver = new GDMReceiver();

	private Gallery mainGallery;

	private View mainGalleryBackgroundView;

	private SharedPreferences preferences;

	private boolean restarted_state = false;

	@Override
	protected void createSideMenu() {
		mainGalleryBackgroundView = findViewById(R.id.mainGalleryBackground);
		mainGallery = (Gallery) findViewById(R.id.mainGalleryMenu);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.menudrawer_selector, R.string.drawer_open,
				R.string.drawer_closed) {
			@Override
			public void onDrawerOpened(View drawerView) {

				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(R.string.app_name);
				drawerList.requestFocusFromTouch();

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(R.string.app_name);
				mainGallery.requestFocusFromTouch();
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		drawerList = (ListView) findViewById(R.id.left_drawer);
		populateMenuOptions();
	}

	protected void populateMenuOptions() {
		List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
		drawerMenuItem
				.add(new MenuDrawerItemImpl(getResources().getString(
						R.string.options_main_about),
						R.drawable.ic_action_action_about));
		drawerMenuItem.add(new MenuDrawerItemImpl(getResources().getString(
				R.string.options_main_clear_image_cache),
				R.drawable.ic_action_content_remove));
		drawerMenuItem.add(new MenuDrawerItemImpl(getResources().getString(
				R.string.clear_queue), R.drawable.ic_action_content_remove));

		drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
		drawerList
				.setOnItemClickListener(new MainMenuDrawerOnItemClickedListener(
						drawerLayout, mainGallery));
	}

	protected void discoverPlexServers() {
		Intent GDMService = new Intent(this, GDMService.class);
		startService(GDMService);
	}

	protected void initDownloadService() {
		downloadHandler = DownloadHandler.getInstance(this);
		downloadService = downloadHandler.getDownloadService();
		getApplicationContext().bindService(
				new Intent(this, DownloadService.class), downloadService,
				Context.BIND_AUTO_CREATE);

		downloadHandler.sendMessage(downloadHandler
				.obtainMessage(SerenityApplication.PROGRESS));
	}

	protected void initPreferences() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences != null) {
			ServerConfig config = (ServerConfig) ServerConfig.getInstance();
			if (config != null) {
				preferences
						.registerOnSharedPreferenceChangeListener(((ServerConfig) ServerConfig
								.getInstance()).getServerConfigChangeListener());
			}
		}
	}

	/**
	 * Refresh the screen after coming back from the preferences screen.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MAIN_MENU_PREFERENCE_RESULT_CODE) {
			recreate();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.setCustomView(R.layout.clock_layout);
		actionBar.setDisplayShowCustomEnabled(true);

		setContentView(R.layout.activity_plex_app_main);

		createSideMenu();
		initPreferences();

		initializeDefaultPlayer();
		if (preferences != null) {
			boolean watchedStatusFirstTime = preferences.getBoolean(
					"watched_status_firsttime", true);
			if (watchedStatusFirstTime) {
				SerenityApplication.getImageLoader().clearDiscCache();
				SerenityApplication.getImageLoader().clearMemoryCache();
				Editor editor = preferences.edit();
				editor.putBoolean("watched_status_firsttime", false);
				editor.apply();
			}
		}

		initDownloadService();
	}

	/**
	 *
	 */
	protected void initializeDefaultPlayer() {
		boolean initialRun = preferences.getBoolean("serenity_first_run", true);
		if (initialRun) {
			SharedPreferences.Editor editor = preferences.edit();
			if (!SerenityApplication.isGoogleTV(this)) {
				editor.putBoolean("external_player", true);
			}
			editor.putBoolean("serenity_first_run", false);
			editor.commit();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		downloadHandler.removeMessages(SerenityApplication.PROGRESS);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(gdmReciver);

		getApplicationContext().unbindService(downloadService);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean menuKeySlidingMenu = preferences.getBoolean(
				"remote_control_menu", true);

		if (menuKeySlidingMenu) {
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				if (drawerLayout.isDrawerOpen(drawerList)) {
					drawerLayout.closeDrawers();
					mainGallery.requestFocusFromTouch();
				} else {
					drawerLayout.openDrawer(drawerList);
					drawerList.requestFocusFromTouch();
				}
				return true;
			}
		}

		if (drawerLayout.isDrawerOpen(drawerList)
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			drawerLayout.closeDrawer(drawerList);
			mainGallery.requestFocusFromTouch();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		restarted_state = true;
		populateMenuOptions();
		super.onRestart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
		IntentFilter filters = new IntentFilter();
		filters.addAction(GDMService.MSG_RECEIVED);
		filters.addAction(GDMService.SOCKET_CLOSED);
		LocalBroadcastManager.getInstance(this).registerReceiver(gdmReciver,
				filters);

		// Start the auto-configuration service
		discoverPlexServers();
		mainGallery.requestFocusFromTouch();

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (SerenityApplication.isTrackingEnabled()) {
			EasyTracker.getInstance().activityStart(this);
		}

		autoConfigureHandler.postDelayed(
				new AutoConfigureHandlerRunnable(this), 2500);
		if (restarted_state == false) {
			setupGallery();
		}
		restarted_state = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (SerenityApplication.isTrackingEnabled()) {
			EasyTracker.getInstance().activityStop(this);
		}
	}

	@Override
	public void openOptionsMenu() {
		drawerLayout.openDrawer(drawerList);
		drawerList.requestFocusFromTouch();
	}

	private void setupGallery() {
		mainGallery.setAdapter(new MainMenuTextViewAdapter(this,
				mainGalleryBackgroundView));
		mainGallery
				.setOnItemSelectedListener(new GalleryOnItemSelectedListener(
						mainGalleryBackgroundView));
		mainGallery
				.setOnItemClickListener(new GalleryOnItemClickListener(this));
		mainGallery.setCallbackDuringFling(false);
		mainGallery.requestFocusFromTouch();
	}
}
