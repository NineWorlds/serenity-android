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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.birbit.android.jobqueue.JobManager;

import net.ganin.darv.DpadAwareRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.impl.CategoryMediaContainer;
import us.nineworlds.serenity.events.MainCategoryEvent;
import us.nineworlds.serenity.injection.InjectingFragment;
import us.nineworlds.serenity.jobs.MovieCategoryJob;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;

import static butterknife.ButterKnife.bind;

public class MovieVideoGalleryFragment extends InjectingFragment {

    @Inject
    SharedPreferences preferences;

    @Inject
    GalleryVideoOnItemClickListener onItemClickListener;

    @Inject
    GalleryVideoOnItemLongClickListener onItemLongClickListener;

    @Inject
    protected MovieSelectedCategoryState categoryState;

    @Inject
    JobManager jobManager;

    @Inject
    PlexappFactory factory;

    @Inject
    Resources resources;

    protected DpadAwareRecyclerView.OnItemSelectedListener onItemSelectedListener;

    @BindView(R.id.moviePosterView)
    protected DpadAwareRecyclerView videoGallery;

    public MovieVideoGalleryFragment() {
        super();
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onItemSelectedListener = new MoviePosterOnItemSelectedListener();
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
        RecyclerView.LayoutManager linearLayoutManager = createLayoutManager();

        videoGallery.addItemDecoration(createItemDecorator());
        videoGallery.setLayoutManager(linearLayoutManager);

        videoGallery.setOnItemSelectedListener(onItemSelectedListener);
        videoGallery.setOnItemClickListener(onItemClickListener);

        videoGallery.setHorizontalFadingEdgeEnabled(true);
        videoGallery.setFocusableInTouchMode(false);
        videoGallery.setDrawingCacheEnabled(true);

        MovieBrowserActivity activity = (MovieBrowserActivity) getActivity();

        String key = activity.getKey();

        MovieCategoryJob job = new MovieCategoryJob(key);
        jobManager.addJobInBackground(job);
    }

    protected LinearLayoutManager createLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        return linearLayoutManager;
    }
}
