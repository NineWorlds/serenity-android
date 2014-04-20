/**
 * The MIT License (MIT)
 * Copyright (c) 2012-2013 David Carver
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

package us.nineworlds.serenity.ui.browser.movie;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.services.CategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.activity.CategoryHandler;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.listeners.MenuDrawerOnClickListener;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;

public class MovieBrowserActivity extends SerenityMultiViewVideoActivity {

	private static String key;
	private boolean restarted_state = false;
	private Handler categoryHandler;
	private SharedPreferences prefs = null;
	private boolean tvMode = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see us.nineworlds.serenity.ui.activity.SerenityActivity#createSideMenu()
	 */
	@Override
	protected void createSideMenu() {
		menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
		if (gridViewActive) {
			menuDrawer.setContentView(R.layout.activity_movie_browser_gridview);
		} else {
			menuDrawer.setContentView(R.layout.activity_movie_browser);
		}
		menuDrawer.setMenuView(R.layout.menu_drawer);

		populateMenuDrawer();
		hideMenuItems();

		View menu = findViewById(R.id.menu_button);
		menu.setOnClickListener(new MenuDrawerOnClickListener(menuDrawer));
	}

	/**
	 * 
	 */
	protected void populateMenuDrawer() {
		List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
		drawerMenuItem.add(new MenuDrawerItemImpl("Grid View",
				R.drawable.ic_action_collections_view_as_grid));
		drawerMenuItem.add(new MenuDrawerItemImpl("Detail View",
				R.drawable.ic_action_collections_view_detail));
		drawerMenuItem.add(new MenuDrawerItemImpl("Play All from Queue",
				R.drawable.menu_play_all_queue));

		menuOptions = (ListView) menuDrawer.getMenuView().findViewById(
				R.id.menu_list_options);
		menuOptions.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
		menuOptions
				.setOnItemClickListener(new MovieMenuDrawerOnItemClickedListener(
						menuDrawer));
	}

	/**
	 * @param listView
	 */
	@Override
	public void hideMenuItems() {
		if (tvMode) {
			menuOptions.setVisibility(View.GONE);
			View gallery = findViewById(R.id.moviePosterGallery);
			if (gallery != null) {
				gallery.requestFocusFromTouch();
			} else {
				View grid = findViewById(R.id.movieGridView);
				grid.requestFocusFromTouch();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean menuKeySlidingMenu = prefs.getBoolean("remote_control_menu",
				true);
		if (menuKeySlidingMenu) {
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				showMenuItems();
				menuDrawer.toggleMenu();
				menuOptions.requestFocusFromTouch();
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && menuDrawer.isMenuVisible()) {
			hideMenuItems();
			menuDrawer.toggleMenu();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		tvMode = prefs.getBoolean("serenity_tv_mode", true);
		gridViewActive = prefs.getBoolean("movie_layout_grid", false);

		Intent intent = getIntent();

		if (intent != null) {
			if (intent.getExtras() != null) {
				key = intent.getExtras().getString("key");
			}
		}

		createSideMenu();

		DisplayUtils.overscanCompensation(this,
				findViewById(R.id.movieBrowserBackgroundLayout),
				findViewById(R.id.menu_drawer_layout));
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (SerenityApplication.isTrackingEnabled()) {
			EasyTracker.getInstance().activityStart(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (restarted_state == false) {
			categoryHandler = new CategoryHandler(this, savedCategory, key);
			Messenger messenger = new Messenger(categoryHandler);
			Intent categoriesIntent = new Intent(this,
					CategoryRetrievalIntentService.class);
			categoriesIntent.putExtra("key", key);
			categoriesIntent.putExtra("MESSENGER", messenger);
			startService(categoriesIntent);
		}
		restarted_state = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
		populateMenuDrawer();
	}

}
