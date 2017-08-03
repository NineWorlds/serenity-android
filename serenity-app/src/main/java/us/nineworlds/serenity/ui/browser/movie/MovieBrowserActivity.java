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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.browser.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.fragments.MovieVideoGalleryFragment;
import us.nineworlds.serenity.fragments.VideoGridFragment;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;

public class MovieBrowserActivity extends SerenityMultiViewVideoActivity
    implements MovieBrowserView {

  @Inject protected SharedPreferences prefs;

  @Inject protected MovieSelectedCategoryState categoryState;

  @InjectPresenter MovieBrowserPresenter presenter;

  private String key;

  VideoGridFragment videoGridFragment;
  MovieVideoGalleryFragment movieVideoGalleryFragment;

  DpadAwareRecyclerView.Adapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
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

  @Override protected void onResume() {
    super.onResume();
  }

  protected void onPause() {
    DpadAwareRecyclerView galleryView = findGalleryView();
    if (galleryView != null) {
      adapter = galleryView.getAdapter();
    }
    super.onPause();
  }

  @Override protected void onRestart() {
    super.onRestart();
    populateMenuDrawer();
  }

  @Override protected void createSideMenu() {
    createContentView();

    initMenuDrawerViews();

    drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.menudrawer_selector,
        R.string.drawer_open, R.string.drawer_closed) {
      @Override public void onDrawerOpened(View drawerView) {

        super.onDrawerOpened(drawerView);
        getSupportActionBar().setTitle(R.string.app_name);
        drawerList.requestFocusFromTouch();
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        getSupportActionBar().setTitle(R.string.app_name);
        View gallery = findViewById(R.id.moviePosterView);
        gallery.requestFocusFromTouch();
      }
    };

    drawerLayout.setDrawerListener(drawerToggle);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);

    populateMenuDrawer();
  }

  protected void createContentView() {
    setContentView(R.layout.activity_movie_browser);
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

    if (gridViewActive) {
      videoGridFragment = new VideoGridFragment();
      fragmentTransaction.replace(R.id.fragment_container, videoGridFragment);
    } else {
      movieVideoGalleryFragment = new MovieVideoGalleryFragment();
      fragmentTransaction.replace(R.id.fragment_container, movieVideoGalleryFragment);
    }
    fragmentTransaction.commit();

    View fanArt = findViewById(R.id.fanArt);
    fanArt.setBackgroundResource(R.drawable.movies);
  }

  protected void populateMenuDrawer() {
    List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
    drawerMenuItem.add(
        new MenuDrawerItemImpl("Grid View", R.drawable.ic_action_collections_view_as_grid));
    drawerMenuItem.add(
        new MenuDrawerItemImpl("Detail View", R.drawable.ic_action_collections_view_detail));
    drawerMenuItem.add(
        new MenuDrawerItemImpl("Play All from Queue", R.drawable.menu_play_all_queue));

    drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
    drawerList.setOnItemClickListener(new MovieMenuDrawerOnItemClickedListener(drawerLayout));
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    boolean menuKeySlidingMenu = prefs.getBoolean("remote_control_menu", true);
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

    if (keyCode == KeyEvent.KEYCODE_BACK && drawerLayout.isDrawerOpen(linearDrawerLayout)) {
      drawerLayout.closeDrawers();
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  public String getKey() {
    return key;
  }

  @Override protected DpadAwareRecyclerView findGalleryView() {
    return (DpadAwareRecyclerView) findViewById(R.id.moviePosterView);
  }

  @Override protected DpadAwareRecyclerView findGridView() {
    return findGalleryView();
  }

  @Override public AbstractPosterImageGalleryAdapter getAdapter() {
    return (AbstractPosterImageGalleryAdapter) adapter;
  }

  @Override public void populateCategory(List<CategoryInfo> categories, String key) {
    ArrayAdapter<CategoryInfo> spinnerArrayAdapter =
        new ArrayAdapter<CategoryInfo>(this, R.layout.serenity_spinner_textview, categories);
    spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

    Spinner categorySpinner = (Spinner) findViewById(R.id.categoryFilter);
    if (categorySpinner == null) {
      return;
    }
    categorySpinner.setVisibility(View.VISIBLE);
    categorySpinner.setAdapter(spinnerArrayAdapter);

    if (categoryState.getCategory() == null) {
      categorySpinner.setOnItemSelectedListener(
          new MovieCategorySpinnerOnItemSelectedListener("all", key, this));
    } else {
      categorySpinner.setOnItemSelectedListener(
          new MovieCategorySpinnerOnItemSelectedListener(categoryState.getCategory(), key, false,
              this));
    }
    categorySpinner.requestFocus();
  }

  @Override
  public void populateSecondaryCategory(List<SecondaryCategoryInfo> categories, String key,
      String category) {
    if (categories == null || categories.isEmpty()) {
      Toast.makeText(this, R.string.no_entries_available_for_category_, Toast.LENGTH_LONG).show();
      return;
    }

    Spinner secondarySpinner = (Spinner) findViewById(R.id.categoryFilter2);
    secondarySpinner.setVisibility(View.VISIBLE);

    ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter =
        new ArrayAdapter<SecondaryCategoryInfo>(this, R.layout.serenity_spinner_textview,
            categories);
    spinnerSecArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
    secondarySpinner.setAdapter(spinnerSecArrayAdapter);
    secondarySpinner.setOnItemSelectedListener(
        new SecondaryCategorySpinnerOnItemSelectedListener(category, key, this));
  }

  @Override public void displayPosters(List<VideoContentInfo> videos) {
    DpadAwareRecyclerView recyclerView = findGalleryView();
    MoviePosterImageAdapter adapter = (MoviePosterImageAdapter) recyclerView.getAdapter();
    adapter.populatePosters(videos);
    recyclerView.requestFocusFromTouch();
  }

  public void requestUpdatedVideos(String key, String category) {
    presenter.fetchVideos(key, category);
  }
}
