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

package us.nineworlds.serenity.ui.browser.tv.seasons;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.recyclerview.FocusableGridLayoutManager;
import us.nineworlds.serenity.ui.recyclerview.FocusableLinearLayoutManager;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;

public class TVShowSeasonBrowserActivity extends SerenityVideoActivity implements TVShowSeasonBrowserView {

  public AbstractPosterImageGalleryAdapter adapter;
  private boolean restarted_state = false;
  private String key;

  @InjectPresenter TVShowSeasonBrowserPresenter presenter;

  @BindView(R.id.fanArt) View fanArt;
  @BindView(R.id.tvshowSeasonBrowserLayout) View tvShowSeasonsMainView;
  @BindView(R.id.tvShowSeasonImageGallery) RecyclerView tvShowSeasonsGallery;
  @BindView(R.id.episodeGridView) RecyclerView gridView;
  @BindView(R.id.drawer_layout) DrawerLayout navdrawer;
  @BindView(R.id.data_loading_container) FrameLayout dataLoadingContainer;

  Handler postDelayed = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar.setCustomView(R.layout.season_custom_actionbar);
    actionBar.setDisplayShowCustomEnabled(true);

    key = getIntent().getExtras().getString("key");

    createSideMenu();

    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
  }

  @Override protected String screenName() {
    return "Seasons Browser";
  }

  @Override protected void onStart() {
    super.onStart();
    if (restarted_state == false) {
      setupSeasons();
    }
    restarted_state = false;
  }

  protected void setupSeasons() {
    TVShowSeasonImageGalleryAdapter adapter = new TVShowSeasonImageGalleryAdapter();
    adapter.setOnItemClickListener(new TVShowSeasonOnItemClickListener(this, adapter));
    adapter.setOnItemSelectedListener(new TVShowSeasonOnItemSelectedListener(tvShowSeasonsMainView, this, adapter));

    tvShowSeasonsGallery.setClipChildren(false);
    tvShowSeasonsGallery.setClipToPadding(false);
    tvShowSeasonsGallery.setAdapter(adapter);
    tvShowSeasonsGallery.setItemAnimator(new FadeInAnimator());
    LinearLayoutManager linearLayoutManager = new FocusableLinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    tvShowSeasonsGallery.setLayoutManager(linearLayoutManager);

    tvShowSeasonsGallery.addItemDecoration(createItemDecorator());
    tvShowSeasonsGallery.setFocusable(true);

    SeasonsEpisodePosterImageGalleryAdapter episodeAdapter = new SeasonsEpisodePosterImageGalleryAdapter();
    adapter.setOnItemClickListener(new EpisodePosterOnItemClickListener(episodeAdapter));
    gridView.setClipToPadding(false);
    gridView.setClipChildren(false);
    gridView.setClipToOutline(false);
    gridView.setAdapter(episodeAdapter);
    gridView.addItemDecoration(createItemDecorator());
    gridView.setItemAnimator(new FadeInAnimator());
    GridLayoutManager gridLayoutManager = new FocusableGridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false) {
      @Override public boolean supportsPredictiveItemAnimations() {
        return false;
      }
    };
    gridView.setLayoutManager(gridLayoutManager);

    dataLoadingContainer.setVisibility(View.VISIBLE);
    presenter.retrieveSeasons(key);
  }

  protected RecyclerView.ItemDecoration createItemDecorator() {
    return new SpaceItemDecoration(
        getResources().getDimensionPixelSize(R.dimen.horizontal_spacing));
  }

  @Override protected void onRestart() {
    super.onRestart();
    populateMenuDrawer();
    restarted_state = true;
  }

  @Override protected void createSideMenu() {
    setContentView(R.layout.activity_tvbrowser_show_seasons);

    ButterKnife.bind(this);

    fanArt.setBackgroundResource(R.drawable.tvshows);

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
      }
    };

    drawerLayout.setDrawerListener(drawerToggle);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);

    populateMenuDrawer();
  }

  protected void populateMenuDrawer() {
    List<MenuDrawerItem> drawerMenuItem = new ArrayList<>();
    drawerMenuItem.add(
        new MenuDrawerItemImpl("Play All from Queue", R.drawable.menu_play_all_queue));

    drawerList.setAdapter(new MenuDrawerAdapter(this, drawerMenuItem));
    drawerList.setOnItemClickListener(
        new TVShowSeasonMenuDrawerOnItemClickedListener(drawerLayout));
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    boolean menuKeySlidingMenu =
        PreferenceManager.getDefaultSharedPreferences(this).getBoolean("remote_control_menu", true);
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
      if (tvShowSeasonsGallery != null) {
        tvShowSeasonsGallery.requestFocusFromTouch();
      }
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  @Override public AbstractPosterImageGalleryAdapter getAdapter() {
    return adapter;
  }

  @Override protected RecyclerView findVideoRecyclerView() {
    return gridView;
  }

  @Override public void updateEpisodes(List<VideoContentInfo> episodes) {
    SeasonsEpisodePosterImageGalleryAdapter adapter =
        (SeasonsEpisodePosterImageGalleryAdapter) gridView.getAdapter();
    adapter.updateEpisodes(episodes);
    gridView.setVisibility(View.VISIBLE);
  }

  @Override public void populateSeasons(List<SeriesContentInfo> seasons) {
    if (!seasons.isEmpty()) {
      TextView titleView = findViewById(R.id.tvShowSeasonsDetailText);
      titleView.setText(seasons.get(0).getParentTitle());
      TextView textView = findViewById(R.id.tvShowSeasonsItemCount);
      textView.setText(Integer.toString(seasons.size()) + getString(R.string._item_s_));
    }

    TVShowSeasonImageGalleryAdapter adapter =
        (TVShowSeasonImageGalleryAdapter) tvShowSeasonsGallery.getAdapter();
    adapter.updateSeasonsList(seasons);

    postDelayed.postDelayed(() -> {
      dataLoadingContainer.setVisibility(View.GONE);
      tvShowSeasonsGallery.getChildAt(0).requestFocus();
      adapter.getOnItemSelectedListener().onItemSelected(tvShowSeasonsGallery.getChildAt(0), 0);
    }, 500);
  }

  @Override public void fetchEpisodes(String key) {
    presenter.retrieveEpisodes(key);
  }
}
