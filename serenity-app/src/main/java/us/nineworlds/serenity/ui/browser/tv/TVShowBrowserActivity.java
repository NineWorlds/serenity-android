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

import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.services.TVShowCategoryRetrievalIntentService;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
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
import us.nineworlds.serenity.ui.util.DisplayUtils;

/**
 * @author dcarver
 * 
 */
public class TVShowBrowserActivity extends SerenityMultiViewVideoActivity {

	private static Spinner categorySpinner;
	private boolean restarted_state = false;
	private static String key;
	private Handler categoryHandler;
	private SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		createSideMenu();

		DisplayUtils.overscanCompensation(this,
				findViewById(R.id.tvshowBrowserLayout),
				findViewById(R.id.menu_drawer_layout));
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
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
		if (!restarted_state) {
			categoryHandler = new CategoryHandler(this);
			Messenger messenger = new Messenger(categoryHandler);
			Intent categoriesIntent = new Intent(this,
					TVShowCategoryRetrievalIntentService.class);
			categoriesIntent.putExtra("key", key);
			categoriesIntent.putExtra("MESSENGER", messenger);
			startService(categoriesIntent);
		}

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
			if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY || keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
				SeriesContentInfo info = (SeriesContentInfo) gallery.getSelectedItem();
				new FindUnwatchedAsyncTask(this).execute(info);
				return true;
			}
		}
							
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void createSideMenu() {
		menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
		menuDrawer.setMenuView(R.layout.menu_drawer);
		
		posterLayoutActive = preferences.getBoolean("series_layout_posters", false);
		gridViewActive = preferences.getBoolean("series_layout_grid", false);
		if (isGridViewActive()) {
			menuDrawer.setContentView(R.layout.activity_tvbrowser_show_gridview_posters);
		} else if (posterLayoutActive) {
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (savedCategory != null) {
			outState.putString("savedCategory", savedCategory );
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();
		savedCategory = null;
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		savedCategory = savedInstanceState.getString("savedCategory");
	}
	
	

	private class CategoryHandler extends Handler {

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
					.findViewById(R.id.categoryFilter);
			categorySpinner.setVisibility(View.VISIBLE);
			categorySpinner.setAdapter(spinnerArrayAdapter);
			
			if (savedCategory == null) {
				categorySpinner
						.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener(
								"all", key));
			} else {
				categorySpinner
				.setOnItemSelectedListener(new CategorySpinnerOnItemSelectedListener(
						savedCategory, key, false));
				
			}
			categorySpinner.requestFocus();
		}
	}
}
