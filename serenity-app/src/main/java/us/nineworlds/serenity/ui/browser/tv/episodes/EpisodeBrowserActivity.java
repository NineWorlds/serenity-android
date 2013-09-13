/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import us.nineworlds.serenity.core.model.MenuDrawerItem;
import us.nineworlds.serenity.core.model.impl.MenuDrawerItemImpl;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.browser.movie.MenuDrawerOnClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

public class EpisodeBrowserActivity extends SerenityVideoActivity {

	private SerenityGallery posterGallery;
	private String key;
	private View bgLayout;
	private View metaData;
	private boolean restarted_state = false;
	private SharedPreferences prefs;
	private MenuDrawer menuDrawer;
	private ListView menuOptions;

	@Override
	protected void createSideMenu() {
		menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		menuDrawer.setContentView(R.layout.activity_movie_browser);
		menuDrawer.setMenuView(R.layout.menu_drawer);
		
		List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
		drawerMenuItem.add(new MenuDrawerItemImpl(getResources().getString(R.string.play_all_from_queue), R.drawable.menu_play_all_queue));
		
		menuOptions = (ListView)menuDrawer.getMenuView().findViewById(R.id.menu_list_options);
		menuOptions.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
		menuOptions.setOnItemClickListener(new EpisodeMenuDrawerOnItemClickedListener(menuDrawer));
		hideMenuItems();
		
		View menu = findViewById(R.id.menu_button);
		menu.setOnClickListener(new MenuDrawerOnClickListener(menuDrawer));
	}
	
	/**
	 * @param listView
	 */
	public void hideMenuItems() {
		if (SerenityApplication.isGoogleTV(this) ||
			SerenityApplication.isAndroidTV(this)) {
			menuOptions.setVisibility(View.GONE);
		}
	}
	
	public void showMenuItems() {
		if (SerenityApplication.isGoogleTV(this) ||
			SerenityApplication.isAndroidTV(this)) {
			menuOptions.setVisibility(View.VISIBLE);
		}
		
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = getIntent().getExtras().getString("key");
		
		createSideMenu();

		bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
		posterGallery = (SerenityGallery) findViewById(R.id.moviePosterGallery);
		metaData = findViewById(R.id.metaDataRow);
		metaData.setVisibility(View.VISIBLE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			showMenuItems();
			menuDrawer.toggleMenu();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && menuDrawer.isMenuVisible()) {
			hideMenuItems();
			menuDrawer.toggleMenu();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		if (restarted_state == false) {
			setupEpisodeBrowser();
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
	
	/**
	 * Populate the episode browser with data
	 */
	protected void setupEpisodeBrowser() {
		boolean scrollingAnimation = prefs.getBoolean("animation_gallery_scrolling", true);
		posterGallery
				.setAdapter(new EpisodePosterImageGalleryAdapter(this, key));
		posterGallery
				.setOnItemSelectedListener(new EpisodePosterOnItemSelectedListener(this));
		posterGallery
				.setOnItemClickListener(new GalleryVideoOnItemClickListener());
		if (key.contains("onDeck") || key.contains("recentlyAdded") || (key.contains("recentlyViewed") && !key.contains("recentlyViewedShows"))) {
			posterGallery
			.setOnItemLongClickListener(new EpisodeBrowserOnLongClickListener());
		} else {
			posterGallery
			.setOnItemLongClickListener(new GalleryVideoOnItemLongClickListener());
		}
		if (scrollingAnimation) {
			posterGallery.setAnimationDuration(220);
		} else {
			posterGallery.setAnimationDuration(1);
		}
		posterGallery.setSpacing(25);
		posterGallery.setCallbackDuringFling(false);
		posterGallery.setFocusableInTouchMode(false);
		posterGallery.setDrawingCacheEnabled(true);
		posterGallery.setHorizontalFadingEdgeEnabled(true);
		posterGallery.setUnselectedAlpha(0.75f);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (key != null && key.contains("onDeck")) {
			restarted_state = false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return;
		}
		restarted_state = true;
	}

}
