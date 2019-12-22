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
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.birbit.android.jobqueue.JobManager;
import javax.inject.Inject;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.InjectingFragment;
import us.nineworlds.serenity.jobs.MovieCategoryJob;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterImageAdapter;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.recyclerview.FocusableLinearLayoutManager;

import static android.view.View.*;
import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;
import static butterknife.ButterKnife.bind;

public class MovieVideoGalleryFragment extends InjectingFragment {

  @Inject SharedPreferences preferences;
  @Inject MovieSelectedCategoryState categoryState;
  @Inject JobManager jobManager;
  @Inject SerenityClient factory;
  @Inject Resources resources;
  @Inject AndroidHelper androidHelper;

  @BindView(R.id.moviePosterView) protected RecyclerView recyclerView;
  @BindView(R.id.data_loading_container) protected FrameLayout dataLoadingContainer;

  public MovieVideoGalleryFragment() {
    super();
    setRetainInstance(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflateView(inflater, container);
    bind(this, view);
    setupRecyclerView();
    return view;
  }

  protected View inflateView(LayoutInflater inflater, ViewGroup container) {
    return inflater.inflate(R.layout.video_gallery_fragment, null);
  }

  protected RecyclerView.ItemDecoration createItemDecorator() {
    return new SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.horizontal_spacing));
  }

  protected void setupRecyclerView() {
    LinearLayoutManager linearLayoutManager = createLayoutManager();
    MoviePosterImageAdapter adapter = new MoviePosterImageAdapter();
    adapter.setOnItemSelectedListener(createOnItemSelectedListener(adapter));
    adapter.setOnItemClickListener(createOnItemClickListener(adapter));

    recyclerView.addItemDecoration(createItemDecorator());
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setHorizontalFadingEdgeEnabled(false);
    recyclerView.setItemAnimator(new FadeInAnimator());
    recyclerView.setClipToPadding(false);
    recyclerView.setClipChildren(false);

    recyclerView.setFocusableInTouchMode(true);

    recyclerView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    recyclerView.setHasFixedSize(true);
    recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

    recyclerView.requestFocus();

    if (!androidHelper.isAndroidTV() && !androidHelper.isAmazonFireTV()) {
      LinearSnapHelper snapHelper = new LinearSnapHelper();
      snapHelper.attachToRecyclerView(recyclerView);

      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);

          if (newState == RecyclerView.SCROLL_STATE_IDLE) {

            snapHelper.findSnapView(linearLayoutManager).requestFocusFromTouch();
//            int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//            recyclerView.findViewHolderForLayoutPosition(position).itemView.requestFocusFromTouch();
//            adapter.notifyItemChanged(position);
          }
        }

      });
    }

    MovieBrowserActivity activity = (MovieBrowserActivity) getActivity();

    String key = activity.getKey();

    MovieCategoryJob job = new MovieCategoryJob(key);

    dataLoadingContainer.setVisibility(VISIBLE);
    jobManager.addJobInBackground(job);
  }

  private AbstractVideoOnItemLongClickListener createOnItemLongClickListener(MoviePosterImageAdapter adapter) {
    return new GalleryVideoOnItemLongClickListener(adapter);
  }

  @NonNull protected AbstractVideoOnItemClickListener createOnItemClickListener(MoviePosterImageAdapter adapter) {
    return new GalleryVideoOnItemClickListener(adapter);
  }

  @NonNull protected AbstractVideoOnItemSelectedListener createOnItemSelectedListener(MoviePosterImageAdapter adapter) {
    return new MoviePosterOnItemSelectedListener(adapter);
  }

  protected LinearLayoutManager createLayoutManager() {
    LinearLayoutManager linearLayoutManager = new FocusableLinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    return linearLayoutManager;
  }
}
