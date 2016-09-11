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

import javax.inject.Inject;

import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.fragments.MovieVideoGalleryFragment;
import us.nineworlds.serenity.fragments.VideoGridFragment;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.jess.ui.TwoWayGridView;

public class MovieBrowserActivity extends SerenityMultiViewVideoActivity {

	private static String key;
	@Inject
	protected SharedPreferences prefs;

	@Inject
	protected MovieSelectedCategoryState categoryState;

	DpadAwareRecyclerView.Adapter adapter;

	@Override
	protected void createSideMenu() {
		setContentView(R.layout.activity_movie_browser);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		MovieVideoGalleryFragment videoGalleryFragment = (MovieVideoGalleryFragment) fragmentManager
				.findFragmentByTag("videoGallery_fragment");
		VideoGridFragment videoGridFragment = (VideoGridFragment) fragmentManager
				.findFragmentByTag("videoGrid_fragment");

		if (gridViewActive) {
			fragmentTransaction.hide(videoGalleryFragment);
			fragmentTransaction.show(videoGridFragment);
			fragmentTransaction.commit();
		} else {
			fragmentTransaction.hide(videoGridFragment);
			fragmentTransaction.show(videoGalleryFragment);
			fragmentTransaction.commit();
		}

		View fanArt = findViewById(R.id.fanArt);
		fanArt.setBackgroundResource(R.drawable.movies);

		initMenuDrawerViews();

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
				View gallery = findViewById(R.id.moviePosterGallery);
				if (!isGridViewActive()) {
					gallery.requestFocusFromTouch();
				}
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		populateMenuDrawer();
	}

	protected void populateMenuDrawer() {
		List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
		drawerMenuItem.add(new MenuDrawerItemImpl("Grid View",
				R.drawable.ic_action_collections_view_as_grid));
		drawerMenuItem.add(new MenuDrawerItemImpl("Detail View",
				R.drawable.ic_action_collections_view_detail));
		drawerMenuItem.add(new MenuDrawerItemImpl("Play All from Queue",
				R.drawable.menu_play_all_queue));

		drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
		drawerList
		.setOnItemClickListener(new MovieMenuDrawerOnItemClickedListener(
				drawerLayout));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean menuKeySlidingMenu = prefs.getBoolean("remote_control_menu",
				true);
		if (menuKeySlidingMenu) {
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				if (drawerLayout.isDrawerOpen(linearDrawerLayout)) {
					drawerLayout.closeDrawers();
				} else {
					drawerLayout.openDrawer(linearDrawerLayout);
				}
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& drawerLayout.isDrawerOpen(linearDrawerLayout)) {
			drawerLayout.closeDrawers();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.setCustomView(R.layout.move_custom_actionbar);
		actionBar.setDisplayShowCustomEnabled(true);

		gridViewActive = prefs.getBoolean("movie_layout_grid", false);

		Intent intent = getIntent();

		if (intent != null) {
			if (intent.getExtras() != null) {
				key = intent.getExtras().getString("key");
			}
		}

		createSideMenu();

		DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		DpadAwareRecyclerView galleryView = findGalleryView();
		adapter = galleryView.getAdapter();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		populateMenuDrawer();
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		MovieBrowserActivity.key = key;
	}

	@Override
	protected DpadAwareRecyclerView findGalleryView() {
		return (DpadAwareRecyclerView) findViewById(R.id.moviePosterGallery);
	}

	@Override
	protected TwoWayGridView findGridView() {
		return (TwoWayGridView) findViewById(R.id.movieGridView);
	}

	@Override
	public AbstractPosterImageGalleryAdapter getAdapter() {
		return (AbstractPosterImageGalleryAdapter) adapter;
	}
}
