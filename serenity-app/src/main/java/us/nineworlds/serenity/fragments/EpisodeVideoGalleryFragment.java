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
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserOnLongClickListener;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.EpisodePosterOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.recyclerview.FocusableLinearLayoutManager;

public class EpisodeVideoGalleryFragment extends InjectingFragment {

  @Inject SharedPreferences preferences;
  @Inject protected MovieSelectedCategoryState categoryState;

  private RecyclerView videoGallery;

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
      videoGallery.setAdapter(adapter);
      videoGallery.setItemAnimator(new FadeInAnimator());
      videoGallery.setHorizontalFadingEdgeEnabled(false);

      FocusableLinearLayoutManager linearLayoutManager = new FocusableLinearLayoutManager(getActivity());
      linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
      videoGallery.setLayoutManager(linearLayoutManager);

      videoGallery.addItemDecoration(new SpaceItemDecoration(
          getResources().getDimensionPixelOffset(R.dimen.horizontal_spacing)));
      videoGallery.setClipToPadding(false);
      videoGallery.setClipChildren(false);


      EpisodeBrowserActivity activity = (EpisodeBrowserActivity) getActivity();
      activity.fetchEpisodes(key);
    }
  }
}
