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

package us.nineworlds.serenity.ui.browser.tv.seasons;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.listeners.MenuDrawerOnClickListener;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import us.nineworlds.serenity.widgets.SerenityGallery;

import com.google.analytics.tracking.android.EasyTracker;
import com.jess.ui.TwoWayGridView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * @author dcarver
 * 
 */
public class TVShowSeasonBrowserActivity extends SerenityVideoActivity {

	private Gallery tvShowSeasonsGallery;
	private View tvShowSeasonsMainView;
	private boolean restarted_state = false;
	private String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");

		createSideMenu();

		tvShowSeasonsMainView = findViewById(R.id.tvshowSeasonBrowserLayout);
		tvShowSeasonsGallery = (Gallery) findViewById(R.id.tvShowSeasonImageGallery);

		DisplayUtils.overscanCompensation(this,
				tvShowSeasonsMainView,
				findViewById(R.id.menu_drawer_layout));
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			setupSeasons();
		}
		restarted_state = false;
	}

	protected void setupSeasons() {

		tvShowSeasonsGallery.setAdapter(new TVShowSeasonImageGalleryAdapter(
				this, key));
		tvShowSeasonsGallery
				.setOnItemSelectedListener(new TVShowSeasonOnItemSelectedListener(
						tvShowSeasonsMainView, this));
		tvShowSeasonsGallery
				.setOnItemClickListener(new TVShowSeasonOnItemClickListener(
						this));
		tvShowSeasonsGallery
				.setOnItemLongClickListener(new SeasonOnItemLongClickListener(
						this));
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
		populateMenuDrawer();
		restarted_state = true;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see us.nineworlds.serenity.ui.activity.SerenityActivity#createSideMenu()
	 */
	@Override
	protected void createSideMenu() {
		menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
		menuDrawer.setMenuView(R.layout.menu_drawer);
		menuDrawer.setContentView(R.layout.activity_tvbrowser_show_seasons);
		menuDrawer.setDrawerIndicatorEnabled(true);

		populateMenuDrawer();

		hideMenuItems();

		View menuButton = findViewById(R.id.menu_button);
		menuButton
				.setOnClickListener(new MenuDrawerOnClickListener(menuDrawer));

	}

	/**
	 * 
	 */
	protected void populateMenuDrawer() {
		List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
		drawerMenuItem.add(new MenuDrawerItemImpl("Play All from Queue",
				R.drawable.menu_play_all_queue));

		menuOptions = (ListView) menuDrawer.getMenuView().findViewById(
				R.id.menu_list_options);
		menuOptions.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
		menuOptions
				.setOnItemClickListener(new TVShowSeasonMenuDrawerOnItemClickedListener(
						menuDrawer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see us.nineworlds.serenity.ui.activity.SerenityActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean menuKeySlidingMenu = PreferenceManager
				.getDefaultSharedPreferences(this).getBoolean(
						"remote_control_menu", true);
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
			if (tvShowSeasonsGallery != null) {
				tvShowSeasonsGallery.requestFocusFromTouch();
			}
			return true;
		}

		View focusView = getCurrentFocus();

		Gallery gallery = (Gallery) findViewById(R.id.tvShowSeasonImageGallery);
		TwoWayGridView gridView = (TwoWayGridView) findViewById(R.id.episodeGridView);
		if (gridView == null) {
			gridView = (TwoWayGridView) findViewById(R.id.tvShowGridView);
		}

		if (gallery == null && gridView == null) {
			return super.onKeyDown(keyCode, event);
		}

		BaseAdapter adapter = null;
		if (focusView instanceof TwoWayGridView) {
			adapter = (BaseAdapter) gridView.getAdapter();
		} else {
			adapter = (BaseAdapter) gallery.getAdapter();
		}

		if (adapter != null) {
			int itemsCount = adapter.getCount();

			if (contextMenuRequested(keyCode)) {
				View view = null;
				if (focusView instanceof TwoWayGridView) {
					view = gridView.getSelectedView();
				} else if (gallery != null) {
					view = gallery.getSelectedView();
				}
				if (view == null) {
					return super.onKeyDown(keyCode, event);
				}
				view.performLongClick();
				return true;
			}

			if (gallery != null) {
				if (isKeyCodeSkipBack(keyCode)) {
					int selectedItem = gallery.getSelectedItemPosition();
					int newPosition = selectedItem - 10;
					if (newPosition < 0) {
						newPosition = 0;
					}
					gallery.setSelection(newPosition);
					gallery.requestFocusFromTouch();
					return true;
				}
				if (isKeyCodeSkipForward(keyCode)) {
					int selectedItem = gallery.getSelectedItemPosition();
					int newPosition = selectedItem + 10;
					if (newPosition > itemsCount) {
						newPosition = itemsCount - 1;
					}
					gallery.setSelection(newPosition);
					gallery.requestFocusFromTouch();
					return true;
				}
			}
		}

		return super.onKeyDown(keyCode, event);

	}

	/** 
	 * Nothing really to update here now, so will return null.
	 * 
	 */
	@Override
	protected SerenityGallery findGalleryView() {
		return null;
	}

	/**
	 * We want to update playback position and onscreen info when completing.
	 * 
	 * So pass back the appropriate grid view in this case.
	 */
	@Override
	protected TwoWayGridView findGridView() {
		return (TwoWayGridView) findViewById(R.id.episodeGridView);
	}
}
