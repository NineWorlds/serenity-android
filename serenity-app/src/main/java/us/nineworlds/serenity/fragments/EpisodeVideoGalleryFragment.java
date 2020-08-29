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

package us.nineworlds.serenity.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import javax.inject.Inject;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.InjectingFragment;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.EpisodePosterOnItemClickListener;
import us.nineworlds.serenity.ui.recyclerview.FocusableLinearLayoutManager;

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;

public class EpisodeVideoGalleryFragment extends InjectingFragment {

  @Inject SharedPreferences preferences;
  @Inject protected MovieSelectedCategoryState categoryState;

  private HorizontalGridView videoGallery;

  public EpisodeVideoGalleryFragment() {
    super();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.video_gallery_fragment, container);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (videoGallery == null) {

      videoGallery = getActivity().findViewById(R.id.moviePosterView);

      String key = ((EpisodeBrowserActivity) getActivity()).getKey();

      EpisodePosterImageGalleryAdapter adapter = new EpisodePosterImageGalleryAdapter();
      adapter.setOnItemClickListener(new EpisodePosterOnItemClickListener(adapter));
      adapter.setOnItemSelectedListener(new EpisodePosterOnItemSelectedListener(adapter));
      videoGallery.setNumRows(1);
      videoGallery.setVisibility(View.GONE);
      videoGallery.setAdapter(adapter);
      videoGallery.setItemAnimator(new FadeInAnimator());
      videoGallery.setHorizontalFadingEdgeEnabled(false);
      videoGallery.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
      videoGallery.setHasFixedSize(true);
      videoGallery.setOverScrollMode(View.OVER_SCROLL_NEVER);

      videoGallery.setClipToPadding(false);
      videoGallery.setClipChildren(false);
      videoGallery.requestFocusFromTouch();

      EpisodeBrowserActivity activity = (EpisodeBrowserActivity) getActivity();
      activity.fetchEpisodes(key);
    }
  }
}
