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
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import moxy.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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

import static android.view.View.*;

public class MovieBrowserActivity extends SerenityMultiViewVideoActivity
    implements MovieBrowserContract.MovieBrowserView {

  @Inject protected SharedPreferences prefs;
  @Inject protected MovieSelectedCategoryState categoryState;
  @InjectPresenter MovieBrowserPresenter presenter;

  private String key;

  VideoGridFragment videoGridFragment;
  MovieVideoGalleryFragment movieVideoGalleryFragment;
  RecyclerView.Adapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    actionBar.setCustomView(R.layout.movie_custom_actionbar);
//    actionBar.setDisplayShowCustomEnabled(true);

    gridViewActive = prefs.getBoolean("movie_layout_grid", false);

    Intent intent = getIntent();

    if (intent != null) {
      if (intent.getExtras() != null) {
        key = intent.getExtras().getString("key");
      }
    }

    createContentView();

    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());

  }

  @Override protected String screenName() {
    return "Movie Browser";
  }

  @Override protected void onPause() {
    RecyclerView galleryView = findVideoRecyclerView();
    if (galleryView != null) {
      adapter = galleryView.getAdapter();
    }
    super.onPause();
  }


  protected void createContentView() {
    setContentView(R.layout.activity_movie_browser);
    ButterKnife.bind(this);
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


  public String getKey() {
    return key;
  }

  @Override protected RecyclerView findVideoRecyclerView() {
    return findViewById(R.id.moviePosterView);
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
    categorySpinner.setVisibility(VISIBLE);
    categorySpinner.setAdapter(spinnerArrayAdapter);

    if (categoryState.getCategory() == null) {
      categorySpinner.setOnItemSelectedListener(new MovieCategorySpinnerOnItemSelectedListener("all", key, this));
    } else {
      categorySpinner.setOnItemSelectedListener(
          new MovieCategorySpinnerOnItemSelectedListener(categoryState.getCategory(), key, false, this));
    }
    categorySpinner.requestFocus();
  }

  @Override public void populateSecondaryCategory(List<SecondaryCategoryInfo> categories, String key, String category) {
    if (categories == null || categories.isEmpty()) {
      Toast.makeText(this, R.string.no_entries_available_for_category_, Toast.LENGTH_LONG).show();
      return;
    }

    Spinner secondarySpinner = (Spinner) findViewById(R.id.categoryFilter2);
    secondarySpinner.setVisibility(VISIBLE);

    ArrayAdapter<SecondaryCategoryInfo> spinnerSecArrayAdapter =
        new ArrayAdapter<SecondaryCategoryInfo>(this, R.layout.serenity_spinner_textview, categories);
    spinnerSecArrayAdapter.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
    secondarySpinner.setAdapter(spinnerSecArrayAdapter);
    secondarySpinner.setOnItemSelectedListener(new SecondaryCategorySpinnerOnItemSelectedListener(category, key, this));
  }

  @Override public void displayPosters(List<VideoContentInfo> videos) {
    FrameLayout dataLoadingContainer = findViewById(R.id.data_loading_container);
    if (dataLoadingContainer != null) {
      dataLoadingContainer.setVisibility(GONE);
    }

    RecyclerView recyclerView = findVideoRecyclerView();
    MoviePosterImageAdapter adapter = (MoviePosterImageAdapter) recyclerView.getAdapter();
    adapter.populatePosters(videos);
    recyclerView.requestFocusFromTouch();
    if (adapter.getItemCount() > 0) {
      if (recyclerView.getChildCount() > 0) {
        recyclerView.getChildAt(0).requestFocus();
      }
    }
  }

  public void requestUpdatedVideos(String key, String category) {
    FrameLayout dataLoadingContainer = findViewById(R.id.data_loading_container);
    if (dataLoadingContainer != null) {
      dataLoadingContainer.setVisibility(View.VISIBLE);
    }

    presenter.fetchVideos(key, category);
  }
}
