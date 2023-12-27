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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import moxy.presenter.InjectPresenter;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.databinding.ActivityMainBinding;
import us.nineworlds.serenity.recyclerutils.ItemOffsetDecoration;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;

public class TVShowSeasonBrowserActivity extends SerenityVideoActivity implements TVShowSeasonBrowserView {

    public AbstractPosterImageGalleryAdapter adapter;
    private String key;

    @InjectPresenter
    TVShowSeasonBrowserPresenter presenter;

    View fanArt;
    View tvShowSeasonsMainView;
    HorizontalGridView tvShowSeasonsGallery;
    HorizontalGridView gridView;
    FrameLayout dataLoadingContainer;

    Handler postDelayed = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key = getIntent().getExtras().getString("key");

        initContentView();

        DisplayUtils.overscanCompensation(this, getWindow().getDecorView());
        setupSeasons();
    }

    @Override
    protected String screenName() {
        return "Seasons Browser";
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void setupSeasons() {
        TVShowSeasonImageGalleryAdapter adapter = new TVShowSeasonImageGalleryAdapter();
        adapter.setOnItemClickListener(new TVShowSeasonOnItemClickListener(this, adapter));
        adapter.setOnItemSelectedListener(new TVShowSeasonOnItemSelectedListener(tvShowSeasonsMainView, this, adapter));

        tvShowSeasonsGallery.setNumRows(1);
        tvShowSeasonsGallery.setClipChildren(false);
        tvShowSeasonsGallery.setClipToPadding(false);
        tvShowSeasonsGallery.setAdapter(adapter);
        tvShowSeasonsGallery.setItemAnimator(new FadeInAnimator());
        tvShowSeasonsGallery.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        tvShowSeasonsGallery.setHasFixedSize(true);
        tvShowSeasonsGallery.setOverScrollMode(View.OVER_SCROLL_NEVER);

        tvShowSeasonsGallery.addItemDecoration(new ItemOffsetDecoration(5));
        tvShowSeasonsGallery.setFocusable(true);

        SeasonsEpisodePosterImageGalleryAdapter episodeAdapter = new SeasonsEpisodePosterImageGalleryAdapter();
        adapter.setOnItemClickListener(new EpisodePosterOnItemClickListener(episodeAdapter));
        gridView.setClipToPadding(false);
        gridView.setClipChildren(false);
        gridView.setClipToOutline(false);
        gridView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        gridView.setHasFixedSize(true);
        gridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gridView.setNumRows(2);

        gridView.setAdapter(episodeAdapter);
        gridView.addItemDecoration(new ItemOffsetDecoration(5));
        gridView.setItemAnimator(new FadeInAnimator());

        dataLoadingContainer.setVisibility(View.VISIBLE);
        presenter.retrieveSeasons(key);
    }

    protected void initContentView() {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_tvbrowser_show_seasons);
        fanArt = findViewById(R.id.fanArt);
        tvShowSeasonsMainView = findViewById(R.id.tvshowSeasonBrowserLayout);
        tvShowSeasonsGallery = findViewById(R.id.tvShowSeasonImageGallery);
        gridView = findViewById(R.id.episodeGridView);
        dataLoadingContainer = findViewById(R.id.data_loading_container);

        fanArt.setBackgroundResource(R.drawable.tvshows);
    }

    @Override
    public AbstractPosterImageGalleryAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected RecyclerView findVideoRecyclerView() {
        return gridView;
    }

    @Override
    public void updateEpisodes(List<VideoContentInfo> episodes) {
        SeasonsEpisodePosterImageGalleryAdapter adapter =
                (SeasonsEpisodePosterImageGalleryAdapter) gridView.getAdapter();
        adapter.updateEpisodes(episodes);
        gridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void populateSeasons(List<SeriesContentInfo> seasons) {
        if (!seasons.isEmpty()) {
            TextView titleView = findViewById(R.id.tvShowSeasonsDetailText);
            titleView.setText(seasons.get(0).getParentTitle());
            TextView textView = findViewById(R.id.tvShowSeasonsItemCount);
            textView.setText(Integer.toString(seasons.size()) + getString(R.string._item_s_));
        }

        TVShowSeasonImageGalleryAdapter adapter =
                (TVShowSeasonImageGalleryAdapter) tvShowSeasonsGallery.getAdapter();
        adapter.updateSeasonsList(seasons);
        dataLoadingContainer.setVisibility(View.GONE);
        tvShowSeasonsGallery.requestFocusFromTouch();

        postDelayed.postDelayed(() -> {
            View childView = tvShowSeasonsGallery.getChildAt(0);
            if (childView != null) {
                childView.requestFocus();
                adapter.getOnItemSelectedListener().onItemSelected(childView, 0);
            }
        }, 750);
    }

    @Override
    public void fetchEpisodes(String key) {
        presenter.retrieveEpisodes(key);
    }
}
