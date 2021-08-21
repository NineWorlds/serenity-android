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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import moxy.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;

import static android.view.View.*;

public class EpisodeBrowserActivity extends SerenityVideoActivity implements EpisodeBrowserView {

  @Inject protected SharedPreferences prefs;

  @InjectPresenter EpisodeBrowserPresenter presenter;

  public AbstractPosterImageGalleryAdapter seasonEpisodeAdapter;

  private static String key;
  private View bgLayout;
  private View metaData;

  Handler postLoadHandler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_episode_browser);

    key = getIntent().getExtras().getString("key");

    bgLayout = findViewById(R.id.movieBrowserBackgroundLayout);
    metaData = findViewById(R.id.metaDataRow);
    metaData.setVisibility(VISIBLE);

    DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
    FrameLayout dataLoadingContainer = findViewById(R.id.data_loading_container);
    if (dataLoadingContainer != null) {
      dataLoadingContainer.setVisibility(View.VISIBLE);
    }
  }

  @Override protected String screenName() {
    return "Episode Browser";
  }

  @Override public AbstractPosterImageGalleryAdapter getAdapter() {
    return seasonEpisodeAdapter;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (key != null && key.contains("onDeck")) {
      recreate();
      return;
    }
  }

  public static String getKey() {
    return key;
  }

  @Override protected RecyclerView findVideoRecyclerView() {
    return findViewById(R.id.moviePosterView);
  }

  @Override public void updateGallery(List<VideoContentInfo> episodes) {
    final RecyclerView gallery = findVideoRecyclerView();
    EpisodePosterImageGalleryAdapter adapter =
        (EpisodePosterImageGalleryAdapter) gallery.getAdapter();

    adapter.updateEpisodes(episodes);
    gallery.setVisibility(View.VISIBLE);
    FrameLayout dataLoadingContainer = findViewById(R.id.data_loading_container);
    if (dataLoadingContainer != null) {
      dataLoadingContainer.setVisibility(GONE);
    }
    gallery.requestFocus();
  }

  @Override public void fetchEpisodes(String key) {
    presenter.retrieveEpisodes(key);
  }
}
