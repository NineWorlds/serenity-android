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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import moxy.presenter.InjectPresenter;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.adapters.TVSeasonsAdapterDelegate;
import us.nineworlds.serenity.ui.adapters.VideoContentInfoAdapter;
import us.nineworlds.serenity.ui.recyclerview.FocusableGridLayoutManager;
import us.nineworlds.serenity.ui.recyclerview.FocusableLinearLayoutManager;
import us.nineworlds.serenity.ui.util.DisplayUtils;

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;

public class TVShowSeasonBrowserActivity extends SerenityVideoActivity implements TVShowSeasonBrowserView {

    public AbstractPosterImageGalleryAdapter adapter;
    private boolean restarted_state = false;
    private String key;

    @InjectPresenter
    TVShowSeasonBrowserPresenter presenter;

    @BindView(R.id.fanArt)
    View fanArt;
    @BindView(R.id.tvshowSeasonBrowserLayout)
    View tvShowSeasonsMainView;
    @BindView(R.id.tvShowSeasonImageGallery)
    RecyclerView tvShowSeasonsGallery;
    @BindView(R.id.episodeGridView)
    RecyclerView gridView;
    @BindView(R.id.data_loading_container)
    FrameLayout dataLoadingContainer;

    Handler postDelayed = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//    actionBar.setCustomView(R.layout.season_custom_actionbar);
//    actionBar.setDisplayShowCustomEnabled(true);

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
        VideoContentInfoAdapter seasonsAdapter = new VideoContentInfoAdapter();
        List<AdapterDelegate<List<ContentInfo>>> seasonsdelegates = new ArrayList<>();
        seasonsdelegates.add(new TVSeasonsAdapterDelegate());
        seasonsAdapter.addDelegates(seasonsdelegates);

        tvShowSeasonsGallery.setClipChildren(false);
        tvShowSeasonsGallery.setClipToPadding(false);
        tvShowSeasonsGallery.setAdapter(seasonsAdapter);
        tvShowSeasonsGallery.setItemAnimator(new FadeInAnimator());
        LinearLayoutManager linearLayoutManager = new FocusableLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tvShowSeasonsGallery.setLayoutManager(linearLayoutManager);
        tvShowSeasonsGallery.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        tvShowSeasonsGallery.setHasFixedSize(true);
        tvShowSeasonsGallery.setOverScrollMode(View.OVER_SCROLL_NEVER);


        tvShowSeasonsGallery.addItemDecoration(createItemDecorator());
        tvShowSeasonsGallery.setFocusable(true);

        SeasonsEpisodePosterImageGalleryAdapter episodeAdapter = new SeasonsEpisodePosterImageGalleryAdapter();
        episodeAdapter.setOnItemClickListener(new EpisodePosterOnItemClickListener(episodeAdapter));
        gridView.setClipToPadding(false);
        gridView.setClipChildren(false);
        gridView.setClipToOutline(false);
        gridView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        gridView.setHasFixedSize(true);
        gridView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        gridView.setAdapter(episodeAdapter);
        gridView.addItemDecoration(createItemDecorator());
        gridView.setItemAnimator(new FadeInAnimator());
        GridLayoutManager gridLayoutManager = new FocusableGridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        gridView.setLayoutManager(gridLayoutManager);

        dataLoadingContainer.setVisibility(View.VISIBLE);
        presenter.retrieveSeasons(key);
    }

    protected RecyclerView.ItemDecoration createItemDecorator() {
        return new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.horizontal_spacing));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restarted_state = true;
    }

    protected void initContentView() {
        setContentView(R.layout.activity_tvbrowser_show_seasons);

        ButterKnife.bind(this);

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

        VideoContentInfoAdapter adapter =
                (VideoContentInfoAdapter) tvShowSeasonsGallery.getAdapter();
        List<ContentInfo> contentInfoList = new ArrayList<>();
        contentInfoList.addAll(seasons);
        adapter.setItems(contentInfoList);

        postDelayed.postDelayed(() -> {
            dataLoadingContainer.setVisibility(View.GONE);
            View childView = tvShowSeasonsGallery.getChildAt(0);
            if (childView != null) {
                childView.requestFocusFromTouch();
                adapter.forceSelectionForFirstAdapter(childView);
                //adapter..onItemSelected(tvShowSeasonsGallery.getChildAt(0), 0);
            }
        }, 750);
    }

    @Override
    public void fetchEpisodes(String key) {
        presenter.retrieveEpisodes(key);
    }
}
