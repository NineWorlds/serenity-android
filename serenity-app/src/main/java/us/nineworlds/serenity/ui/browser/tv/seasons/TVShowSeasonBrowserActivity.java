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
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import app.com.tvrecyclerview.TvRecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;

/**
 * @author dcarver
 */
public class TVShowSeasonBrowserActivity extends SerenityVideoActivity
    implements TVShowSeasonBrowserView {

  public AbstractPosterImageGalleryAdapter adapter;
  private boolean restarted_state = false;
  private String key;

  @InjectPresenter TVShowSeasonBrowserPresenter presenter;

  @BindView(R.id.fanArt) View fanArt;
  @BindView(R.id.tvshowSeasonBrowserLayout) View tvShowSeasonsMainView;
  @BindView(R.id.tvShowSeasonImageGallery) TvRecyclerView tvShowSeasonsGallery;
  @BindView(R.id.episodeGridView) TvRecyclerView gridView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar.setCustomView(R.layout.season_custom_actionbar);
    actionBar.setDisplayShowCustomEnabled(true);

    key = getIntent().getExtras().getString("key");

    createSideMenu();

    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
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

    tvShowSeasonsGallery.setAdapter(adapter);
    tvShowSeasonsGallery.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    tvShowSeasonsGallery.addItemDecoration(createItemDecorator());
    tvShowSeasonsGallery.setFocusable(true);
    tvShowSeasonsGallery.setOnItemStateListener(adapter);
    tvShowSeasonsGallery.setSelectPadding(0,0,0, 0);

    SeasonsEpisodePosterImageGalleryAdapter episodeAdapter = new SeasonsEpisodePosterImageGalleryAdapter();
    adapter.setOnItemClickListener(new EpisodePosterOnItemClickListener(episodeAdapter));
    gridView.setAdapter(episodeAdapter);
    gridView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
    gridView.setOnItemStateListener(episodeAdapter);
    gridView.setSelectPadding(0, 0, 0, 0);

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

  /**
   * Nothing really to update here now, so will return null.
   */
  @Override protected RecyclerView findGalleryView() {
    return null;
  }

  /**
   * We want to update playback position and onscreen info when completing.
   * <p>
   * So pass back the appropriate grid view in this case.
   */
  @Override protected RecyclerView findGridView() {
    return (TvRecyclerView) findViewById(R.id.episodeGridView);
  }

  @Override public void updateEpisodes(List<VideoContentInfo> episodes) {
    SeasonsEpisodePosterImageGalleryAdapter adapter =
        (SeasonsEpisodePosterImageGalleryAdapter) gridView.getAdapter();
    adapter.updateEpisodes(episodes);
    gridView.setVisibility(View.VISIBLE);
    if (adapter.getItemCount() > 0) {
      gridView.setItemSelected(0);
    }
  }

  @Override public void populateSeasons(List<SeriesContentInfo> seasons) {
    if (!seasons.isEmpty()) {
      TextView titleView = (TextView) findViewById(R.id.tvShowSeasonsDetailText);
      titleView.setText(seasons.get(0).getParentTitle());
      TextView textView = (TextView) findViewById(R.id.tvShowSeasonsItemCount);
      textView.setText(Integer.toString(seasons.size()) + getString(R.string._item_s_));
    }

    TVShowSeasonImageGalleryAdapter adapter =
        (TVShowSeasonImageGalleryAdapter) tvShowSeasonsGallery.getAdapter();
    adapter.updateSeasonsList(seasons);
    if (adapter.getItemCount() > 0) {
      tvShowSeasonsGallery.setItemSelected(0);
      tvShowSeasonsGallery.requestFocusFromTouch();
      if (tvShowSeasonsGallery.getChildCount() > 0) {
        tvShowSeasonsGallery.getChildAt(0).requestFocus();
      }
    }
  }

  @Override public void fetchEpisodes(String key) {
    presenter.retrieveEpisodes(key);
  }
}
