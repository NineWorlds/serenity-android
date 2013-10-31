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

package us.nineworlds.serenity.ui.browser.tv;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.MenuDrawerItem;
import us.nineworlds.serenity.core.model.impl.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.services.TVShowCategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.listeners.MenuDrawerOnClickListener;

import us.nineworlds.serenity.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

/**
 * @author dcarver
 * 
 */
public class TVShowBrowserActivity extends SerenityVideoActivity {

	private static Spinner categorySpinner;
	private boolean restarted_state = false;
	private static String key;
	private Handler categoryHandler;
	private SharedPreferences preferences;
	public static boolean USE_POSTER_LAYOUT = false;
	public static boolean USE_GRID_LAYOUT = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		createSideMenu();
		
		if (preferences.getBoolean("overscan_compensation", false)) {
			RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.tvshowBrowserLayout);
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)  mainLayout.getLayoutParams();
			params.setMargins(35, 20, 40, 20);
			
			RelativeLayout menuDrawerLayout = (RelativeLayout) findViewById(R.id.menu_drawer_layout);
			FrameLayout.LayoutParams menuParams = (FrameLayout.LayoutParams)  menuDrawerLayout.getLayoutParams();
			menuParams.setMargins(35, 0, 0, 0);			
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);

		if (restarted_state == false) {
			categoryHandler = new CategoryHandler(this);
			Messenger messenger = new Messenger(categoryHandler);
			Intent categoriesIntent = new Intent(this,
					TVShowCategoryRetrievalIntentService.class);
			categoriesIntent.putExtra("key", key);
			categoriesIntent.putExtra("MESSENGER", messenger);
			startService(categoriesIntent);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		restarted_state = true;
		populateMenuDrawer();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		populateMenuDrawer();
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.ui.activity.SerenityActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Gallery gallery = (Gallery) findViewById(R.id.tvShowBannerGallery);
		
		boolean menuKeySlidingMenu = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("remote_control_menu",
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
			if (gallery != null) {
				gallery.requestFocusFromTouch();
			}
			return true;
		}
		
		if (gallery == null) {
			return super.onKeyDown(keyCode, event);				
		}
		
		AbstractPosterImageGalleryAdapter adapter = (AbstractPosterImageGalleryAdapter) gallery.getAdapter();
		if (adapter != null) {
			int itemsCount =  adapter.getCount();
			
			if (contextMenuRequested(keyCode)) {
				View view = gallery.getSelectedView();
				view.performLongClick();
				return true;
			}
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
				int newPosition = selectedItem  + 10;
				if (newPosition > itemsCount) {
					newPosition = itemsCount - 1;
				}
				gallery.setSelection(newPosition);
				gallery.requestFocusFromTouch();
				return true;
			}
		}
							
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void createSideMenu() {
		menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
		menuDrawer.setMenuView(R.layout.menu_drawer);
		
		USE_POSTER_LAYOUT = preferences.getBoolean("series_layout_posters", false);
		USE_GRID_LAYOUT = preferences.getBoolean("series_layout_grid", false);
		if (USE_GRID_LAYOUT) {
			menuDrawer.setContentView(R.layout.activity_tvbrowser_show_gridview_posters);
		} else if (USE_POSTER_LAYOUT) {
			menuDrawer.setContentView(R.layout.activity_tvbrowser_show_posters);
		} else {
			menuDrawer.setContentView(R.layout.activity_tvbrowser_show_banners);
		}
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
		drawerMenuItem.add(new MenuDrawerItemImpl("Grid View", R.drawable.ic_action_collections_view_as_grid));
		drawerMenuItem.add(new MenuDrawerItemImpl("Detail View", R.drawable.ic_action_collections_view_detail));
		drawerMenuItem.add(new MenuDrawerItemImpl("Play All from Queue", R.drawable.menu_play_all_queue));
		
		menuOptions = (ListView)menuDrawer.getMenuView().findViewById(R.id.menu_list_options);
		menuOptions.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
		menuOptions.setOnItemClickListener(new TVShowMenuDrawerOnItemClickedListener(menuDrawer));
	}
	

	private static class CategoryHandler extends Handler {

		private ArrayList<CategoryInfo> categories;
		private Activity context;

		public CategoryHandler(Activity context) {
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				categories = (ArrayList<CategoryInfo>) msg.obj;
				setupShows();
			}
		}

		protected void setupShows() {
			ArrayAdapter<CategoryInfo> spinnerArrayAdapter = new ArrayAdapter<CategoryInfo>(
					context, R.layout.serenity_spinner_textview, categories);
			spinnerArrayAdapter
					.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

			categorySpinner = (Spinner) context
					.findViewById(R.id.tvshow_CategoryFilter);
			categorySpinner.setVisibility(View.VISIBLE);
			categorySpinner.setAdapter(spinnerArrayAdapter);
			categorySpinner
					.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener(
							"all", key));
			categorySpinner.requestFocus();
		}
	}
}
