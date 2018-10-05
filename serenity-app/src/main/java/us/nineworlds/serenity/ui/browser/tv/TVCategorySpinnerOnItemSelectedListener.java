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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import app.com.tvrecyclerview.TvRecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.birbit.android.jobqueue.JobManager;
import javax.inject.Inject;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.jobs.TVCategorySecondaryJob;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;

public class TVCategorySpinnerOnItemSelectedListener extends BaseInjector
    implements OnItemSelectedListener {

  private String selected;
  private String key;
  private boolean firstSelection = true;
  private String category;
  private String savedInstanceCategory;
  private boolean isGridViewActive;
  private boolean posterLayoutActive;

  @Inject JobManager jobManager;

  @Inject SharedPreferences prefs;

  @Inject TVCategoryState categoryState;

  @BindView(R.id.categoryFilter2) Spinner secondarySpinner;
  @BindView(R.id.tvShowBannerGallery) @Nullable TvRecyclerView posterGallery;
  @BindView(R.id.tvShowGridView) @Nullable TvRecyclerView tvGridView;

  public TVCategorySpinnerOnItemSelectedListener(String defaultSelection, String ckey) {
    selected = defaultSelection;
    key = ckey;
  }

  public TVCategorySpinnerOnItemSelectedListener(String defaultSelection, String ckey, boolean sw) {
    selected = defaultSelection;
    key = ckey;
    savedInstanceCategory = defaultSelection;
    firstSelection = sw;
  }

  @Override
  public void onItemSelected(AdapterView<?> viewAdapter, View view, int position, long id) {
    Activity context = getActivity(viewAdapter.getContext());
    if (context.isDestroyed()) {
      return;
    }
    ButterKnife.bind(this, context);
    isGridViewActive = prefs.getBoolean("series_layout_grid", false);
    posterLayoutActive = prefs.getBoolean("series_layout_posters", false);

    CategoryInfo item = (CategoryInfo) viewAdapter.getItemAtPosition(position);

    if (savedInstanceCategory != null) {
      int savedInstancePosition = getSavedInstancePosition(viewAdapter);
      item = (CategoryInfo) viewAdapter.getItemAtPosition(savedInstancePosition);
      viewAdapter.setSelection(savedInstancePosition);
      savedInstanceCategory = null;
      if (item.getLevel() == 0) {
        populatePrimaryCategory(item, secondarySpinner);
      } else {
        populateSecondaryCategory();
      }
      return;
    }

    if (firstSelection) {
      String filter = prefs.getString("serenity_series_category_filter", "all");

      int count = viewAdapter.getCount();
      for (int i = 0; i < count; i++) {
        CategoryInfo citem = (CategoryInfo) viewAdapter.getItemAtPosition(i);
        if (citem.getCategory().equals(filter)) {
          item = citem;
          selected = citem.getCategory();
          viewAdapter.setSelection(i);
          continue;
        }
      }

      if (item.getCategory().equals("newest")
          || item.getCategory().equals("recentlyAdded")
          || item.getCategory().equals("recentlyViewed")
          || item.getCategory().equals("onDeck")) {
        Intent i = new Intent(context, EpisodeBrowserActivity.class);
        i.putExtra("key", "/library/sections/" + key + "/" + item.getCategory());
        context.startActivityForResult(i, 0);
      } else {
        setupImageGallery(item);
      }
      firstSelection = false;
      return;
    }

    if (selected.equalsIgnoreCase(item.getCategory())) {
      return;
    }

    selected = item.getCategory();
    category = item.getCategory();
    categoryState.setCategory(selected);

    if (item.getLevel() == 0) {
      populatePrimaryCategory(item, secondarySpinner);
    } else {
      populateSecondaryCategory();
    }
  }

  protected void populatePrimaryCategory(CategoryInfo item, Spinner secondarySpinner) {
    if (item.getCategory().equals("newest")
        || item.getCategory().equals("recentlyAdded")
        || item.getCategory().equals("recentlyViewed")
        || item.getCategory().equals("onDeck")) {
      Activity context = getActivity(secondarySpinner.getContext());
      Intent i = new Intent(context, EpisodeBrowserActivity.class);
      i.putExtra("key", "/library/sections/" + key + "/" + item.getCategory());
      context.startActivityForResult(i, 0);
    } else {
      secondarySpinner.setVisibility(View.INVISIBLE);
      setupImageGallery(item);
    }
  }

  protected void populateSecondaryCategory() {
    TVCategorySecondaryJob tvCategoryJob = new TVCategorySecondaryJob(key, category);
    jobManager.addJobInBackground(tvCategoryJob);
  }

  private int getSavedInstancePosition(AdapterView<?> viewAdapter) {
    int count = viewAdapter.getCount();
    for (int i = 0; i < count; i++) {
      CategoryInfo citem = (CategoryInfo) viewAdapter.getItemAtPosition(i);
      if (citem.getCategory().equals(savedInstanceCategory)) {
        return i;
      }
    }
    return 0;
  }

  protected void setupImageGallery(CategoryInfo item) {
    if (isGridViewActive) {
      tvGridView.setAdapter(new TVShowPosterImageGalleryAdapter());
      tvGridView.setOnKeyListener(new TVShowGridOnKeyListener());
      refreshData(item);
      return;
    }

    refreshData(item);
    //			posterGallery
    //			.setOnItemLongClickListener(new ShowOnItemLongClickListener());
  }

  private void refreshData(CategoryInfo item) {
    Activity activity = getActivity(secondarySpinner.getContext());
    if (activity instanceof TVShowBrowserActivity) {
      TVShowBrowserActivity a = (TVShowBrowserActivity) activity;
      a.requestUpdatedVideos(key, item.getCategory());
    }
  }

  @Override public void onNothingSelected(AdapterView<?> va) {

  }
}
