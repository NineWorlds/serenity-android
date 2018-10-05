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

package us.nineworlds.serenity.ui.browser.tv;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.com.tvrecyclerview.TvRecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.recyclerutils.ItemOffsetDecoration;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import us.nineworlds.serenity.widgets.SerenityTVRecylcerView;

public class TVShowBrowserActivity extends SerenityMultiViewVideoActivity implements TVShowBrowserView {

  public static final String SERIES_LAYOUT_GRID = "series_layout_grid";
  protected boolean restarted_state = false;
  protected String key;

  @InjectPresenter TVShowBrowserPresenter presenter;

  @Inject Provider<TVShowBrowserPresenter> tvShowBrowserPresenterProvider;

  @Inject protected SharedPreferences preferences;

  @Inject protected TVCategoryState categoryState;

  protected OnKeyDownDelegate onKeyDownDelegate;

  @BindView(R.id.tvShowBannerGallery) @Nullable SerenityTVRecylcerView tvShowBannerGalleryView;
  @BindView(R.id.tvShowGridView) @Nullable SerenityTVRecylcerView tvShowGridView;
  @BindView(R.id.fanArt) View fanArt;
  @BindView(R.id.tvShowItemCount) TextView tvShowItemCountView;
  @BindView(R.id.categoryFilter2) Spinner secondarySpinner;
  @BindView(R.id.categoryFilter) Spinner categorySpinner;

  @ProvidePresenter TVShowBrowserPresenter providesTVShowBrowserPresenter() {
    return tvShowBrowserPresenterProvider.get();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    actionBar.setCustomView(R.layout.tvshow_custom_actionbar);
    actionBar.setDisplayShowCustomEnabled(true);
    key = getIntent().getExtras().getString("key");

    createSideMenu();
    onKeyDownDelegate = new OnKeyDownDelegate(this);

    fanArt.setBackgroundResource(R.drawable.tvshows);

    populateTVShowContent();

    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
  }

  private void populateTVShowContent() {
    TvRecyclerView recyclerView = findGalleryView() != null ? findGalleryView() : findGridView();

    if (!gridViewActive) {
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
      recyclerView.setLayoutManager(linearLayoutManager);
      recyclerView.addItemDecoration(
          new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.horizontal_spacing)));

      if (!posterLayoutActive) {
        TVShowRecyclerAdapter adapter = new TVShowRecyclerAdapter();
        adapter.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(adapter));
        adapter.setOnItemSelectedListener(new TVShowGalleryOnItemSelectedListener(adapter));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemStateListener(adapter);
      } else {
        TVShowPosterImageGalleryAdapter adapter = new TVShowPosterImageGalleryAdapter();
        adapter.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(adapter));
        adapter.setOnItemSelectedListener(new TVShowGalleryOnItemSelectedListener(adapter));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemStateListener(adapter);
      }
    } else {
      GridLayoutManager serenityMenuGridLayoutManager =
          new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false);
      recyclerView.setLayoutManager(serenityMenuGridLayoutManager);
      recyclerView.addItemDecoration(
          new ItemOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.grid_spacing_dimen)));
      TVShowPosterImageGalleryAdapter adapter = new TVShowPosterImageGalleryAdapter();
      adapter.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(adapter));
      adapter.setOnItemSelectedListener(new TVShowGridOnItemSelectedListener(adapter));
      recyclerView.setAdapter(adapter);
      recyclerView.setOnItemStateListener(adapter);
    }
    recyclerView.setSelectPadding(0, 0, 0, 0);
  }

  @Override protected void onRestart() {
    super.onRestart();
    restarted_state = true;
    populateMenuDrawer();
  }

  @Override protected void onResume() {
    super.onResume();
    populateMenuDrawer();
    if (!restarted_state) {
      presenter.fetchTVCategories(key);
    }
  }

  @Override protected void createSideMenu() {
    posterLayoutActive = preferences.getBoolean("series_layout_posters", false);
    gridViewActive = preferences.getBoolean(SERIES_LAYOUT_GRID, false);
    if (isGridViewActive()) {
      setContentView(R.layout.activity_tvbrowser_show_gridview_posters);
    } else if (posterLayoutActive) {
      setContentView(R.layout.activity_tvbrowser_show_posters);
    } else {
      setContentView(R.layout.activity_tvbrowser_show_banners);
    }

    ButterKnife.bind(this);

    initMenuDrawerViews();

    drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.menudrawer_selector, R.string.drawer_open,
        R.string.drawer_closed) {
      @Override public void onDrawerOpened(View drawerView) {

        super.onDrawerOpened(drawerView);
        getSupportActionBar().setTitle(R.string.app_name);
        drawerList.requestFocusFromTouch();
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        getSupportActionBar().setTitle(R.string.app_name);
      }
    };

    drawerLayout.setDrawerListener(drawerToggle);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);

    populateMenuDrawer();
  }

  protected void populateMenuDrawer() {
    List<MenuDrawerItem> drawerMenuItem = new ArrayList<MenuDrawerItem>();
    drawerMenuItem.add(new MenuDrawerItemImpl("Grid View", R.drawable.ic_action_collections_view_as_grid));
    drawerMenuItem.add(new MenuDrawerItemImpl("Detail View", R.drawable.ic_action_collections_view_detail));
    drawerMenuItem.add(new MenuDrawerItemImpl("Play All from Queue", R.drawable.menu_play_all_queue));

    drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
    drawerList.setOnItemClickListener(new TVShowMenuDrawerOnItemClickedListener(drawerLayout));
  }

  @Override @Deprecated public AbstractPosterImageGalleryAdapter getAdapter() {
    return null;
  }

  @Override protected TvRecyclerView findGalleryView() {
    return tvShowBannerGalleryView;
  }

  @Override protected TvRecyclerView findGridView() {
    return tvShowGridView;
  }

  @Override public void updateCategories(List<CategoryInfo> categories) {
    ArrayAdapter<CategoryInfo> spinnerArrayAdapter =
        new ArrayAdapter<CategoryInfo>(this, R.layout.serenity_spinner_textview, categories);

    spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

    categorySpinner.setVisibility(View.VISIBLE);
    categorySpinner.setAdapter(spinnerArrayAdapter);

    if (categoryState.getCategory() == null) {
      categorySpinner.setOnItemSelectedListener(new TVCategorySpinnerOnItemSelectedListener("all", key));
    } else {
      categorySpinner.setOnItemSelectedListener(
          new TVCategorySpinnerOnItemSelectedListener(categoryState.getCategory(), key, false));
    }
    categorySpinner.requestFocus();
  }

  @Override public void displayShows(List<SeriesContentInfo> series, String category) {
    TvRecyclerView recyclerView = findGalleryView() != null ? findGalleryView() : findGridView();
    if (series.isEmpty()) {
      Toast.makeText(this, getString(R.string.no_shows_found_for_the_category_) + category, Toast.LENGTH_LONG).show();
    }
    tvShowItemCountView.setText(Integer.toString(series.size()) + getString(R.string._item_s_));
    TVShowRecyclerAdapter adapter = (TVShowRecyclerAdapter) recyclerView.getAdapter();
    adapter.updateSeries(series);
    recyclerView.requestFocus();
    if (adapter.getItemCount() > 0) {
      recyclerView.setItemSelected(0);
      recyclerView.getChildAt(0).requestFocus();
    }
  }

  @Override
  public void populateSecondaryCategories(List<SecondaryCategoryInfo> categories, String selectedSecondaryCategory) {
    categorySpinner.setVisibility(View.VISIBLE);
    if (categories == null || categories.isEmpty()) {
      Toast.makeText(this, R.string.no_entries_available_for_category_, Toast.LENGTH_LONG).show();
      return;
    }

    secondarySpinner.setVisibility(View.VISIBLE);

    ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter =
        new ArrayAdapter<SecondaryCategoryInfo>(this, R.layout.serenity_spinner_textview, categories);
    spinnerSecArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
    secondarySpinner.setAdapter(spinnerSecArrayAdapter);
    secondarySpinner.setOnItemSelectedListener(
        new TVSecondaryCategorySpinnerOnItemSelectedListener(selectedSecondaryCategory, key));
    secondarySpinner.requestFocusFromTouch();
  }

  @Override public void recreate() {
    super.recreate();

    if (categoryState.getGenreCategory() != null) {
      requestUpdatedVideos(key, categoryState.getGenreCategory());
    } else {
      presenter.fetchTVCategories(categoryState.getCategory());
    }
  }

  @Override public void requestUpdatedVideos(String key, String category) {
    presenter.fetchTVShows(key, category);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (onKeyDownDelegate.onKeyDown(keyCode, event)) {
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }
}
