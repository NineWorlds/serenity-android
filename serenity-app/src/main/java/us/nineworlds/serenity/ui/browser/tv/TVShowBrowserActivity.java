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
import android.os.Handler;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.emby.model.Video;
import us.nineworlds.serenity.recyclerutils.ItemOffsetDecoration;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.TVSeriesAdapterDelegate;
import us.nineworlds.serenity.ui.adapters.VideoContentInfoAdapter;
import us.nineworlds.serenity.ui.recyclerview.FocusableGridLayoutManager;
import us.nineworlds.serenity.ui.recyclerview.FocusableLinearLayoutManager;
import us.nineworlds.serenity.ui.util.DisplayUtils;

import static android.view.View.*;
import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;

public class TVShowBrowserActivity extends SerenityMultiViewVideoActivity
    implements TVShowBrowserView {

  public static final String SERIES_LAYOUT_GRID = "series_layout_grid";
  protected boolean restarted_state = false;
  protected String key;

  @BindView(R.id.tvShowRecyclerView) RecyclerView tvShowRecyclerView;
  @BindView(R.id.fanArt) View fanArt;
  @BindView(R.id.tvShowItemCount) TextView tvShowItemCountView;
  @BindView(R.id.categoryFilter2) Spinner secondarySpinner;
  @BindView(R.id.categoryFilter) Spinner categorySpinner;

  @InjectPresenter TVShowBrowserPresenter presenter;
  @Inject Provider<TVShowBrowserPresenter> tvShowBrowserPresenterProvider;
  @Inject protected SharedPreferences preferences;
  @Inject protected TVCategoryState categoryState;

  protected OnKeyDownDelegate onKeyDownDelegate;
  Handler postDelayed = new Handler();

  @ProvidePresenter TVShowBrowserPresenter providesTVShowBrowserPresenter() {
    return tvShowBrowserPresenterProvider.get();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    key = getIntent().getExtras().getString("key");

    initContentView();
    onKeyDownDelegate = new OnKeyDownDelegate(this);

    fanArt.setBackgroundResource(R.drawable.tvshows);

    populateTVShowContent();

    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());

    FrameLayout dataLoadingContainer = findViewById(R.id.data_loading_container);
    if (dataLoadingContainer != null) {
      dataLoadingContainer.setVisibility(View.VISIBLE);
    }

  }

  @Override protected String screenName() {
    return "TV Shows Browser";
  }

  private void populateTVShowContent() {
    tvShowRecyclerView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    tvShowRecyclerView.setHasFixedSize(true);
    tvShowRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);


    if (gridViewActive) {
      GridLayoutManager gridLayoutManager = new FocusableGridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false);
      tvShowRecyclerView.setLayoutManager(gridLayoutManager);

      tvShowRecyclerView.addItemDecoration(
          new ItemOffsetDecoration(
              getResources().getDimensionPixelSize(R.dimen.grid_spacing_dimen)));
      TVShowPosterImageGalleryAdapter adapter = new TVShowPosterImageGalleryAdapter();
      adapter.setOnItemClickListener(new TVShowBrowserGalleryOnItemClickListener(adapter));
      adapter.setOnItemSelectedListener(new TVShowGridOnItemSelectedListener(adapter));
      tvShowRecyclerView.setAdapter(adapter);
      return;
    }

    LinearLayoutManager linearLayoutManager = new FocusableLinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    tvShowRecyclerView.setLayoutManager(linearLayoutManager);
    tvShowRecyclerView.addItemDecoration(
        new SpaceItemDecoration(
            getResources().getDimensionPixelSize(R.dimen.horizontal_spacing)));

    VideoContentInfoAdapter adapter = new VideoContentInfoAdapter();
    List<AdapterDelegate<List<ContentInfo>>> delegates = new ArrayList<>();
    delegates.add(new TVSeriesAdapterDelegate());
    adapter.addDelegates(delegates);

    tvShowRecyclerView.setAdapter(adapter);
  }

  @Override protected void onRestart() {
    super.onRestart();
    restarted_state = true;
  }

  @Override protected void onResume() {
    super.onResume();
    if (!restarted_state) {
      presenter.fetchTVCategories(key);
    }
  }

  protected void initContentView() {
    posterLayoutActive = preferences.getBoolean("series_layout_posters", false);
    gridViewActive = preferences.getBoolean(SERIES_LAYOUT_GRID, false);
    if (isGridViewActive()) {
      setContentView(R.layout.activity_tvbrowser_show_gridview_posters);
    } else {
      setContentView(R.layout.activity_tvbrowser_show_banners);
    }

    ButterKnife.bind(this);
  }

  @Override @Deprecated public AbstractPosterImageGalleryAdapter getAdapter() {
    return null;
  }

  @Override protected RecyclerView findVideoRecyclerView() {
    return tvShowRecyclerView;
  }

  @Override public void updateCategories(List<CategoryInfo> categories) {
    ArrayAdapter<CategoryInfo> spinnerArrayAdapter =
        new ArrayAdapter<>(this, R.layout.serenity_spinner_textview, categories);

    spinnerArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);

    categorySpinner.setVisibility(VISIBLE);
    categorySpinner.setAdapter(spinnerArrayAdapter);

    if (categoryState.getCategory() == null) {
      categorySpinner.setOnItemSelectedListener(
          new TVCategorySpinnerOnItemSelectedListener("all", key));
    } else {
      categorySpinner.setOnItemSelectedListener(
          new TVCategorySpinnerOnItemSelectedListener(categoryState.getCategory(), key, false));
    }
    categorySpinner.requestFocus();
  }

  @Override public void displayShows(List<SeriesContentInfo> series, String category) {
    FrameLayout dataLoadingContainer = findViewById(R.id.data_loading_container);
    if (dataLoadingContainer != null) {
      dataLoadingContainer.setVisibility(GONE);
    }

    if (series.isEmpty()) {
      Toast.makeText(this, getString(R.string.no_shows_found_for_the_category_) + category,
          Toast.LENGTH_LONG).show();
    }

    tvShowRecyclerView.setClipChildren(false);
    tvShowRecyclerView.setClipToPadding(false);
    tvShowRecyclerView.setClipToOutline(false);
    tvShowRecyclerView.setItemAnimator(new FadeInAnimator());
    tvShowItemCountView.setText(Integer.toString(series.size()) + getString(R.string._item_s_));
    VideoContentInfoAdapter adapter = (VideoContentInfoAdapter) tvShowRecyclerView.getAdapter();
    List<ContentInfo> contentInfoList = new ArrayList<>();
    contentInfoList.addAll(series);
    adapter.setItems(contentInfoList);

    tvShowRecyclerView.setVisibility(VISIBLE);

    postDelayed.postDelayed(() ->  {
      View view = tvShowRecyclerView.getChildAt(0);
      if (view != null) {
        view.requestFocusFromTouch();
      }
    }, 500);
  }

  @Override
  public void populateSecondaryCategories(List<SecondaryCategoryInfo> categories,
      String selectedSecondaryCategory) {
    categorySpinner.setVisibility(VISIBLE);
    if (categories == null || categories.isEmpty()) {
      Toast.makeText(this, R.string.no_entries_available_for_category_, Toast.LENGTH_LONG).show();
      return;
    }

    secondarySpinner.setVisibility(VISIBLE);

    ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter =
        new ArrayAdapter<SecondaryCategoryInfo>(this, R.layout.serenity_spinner_textview,
            categories);
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
