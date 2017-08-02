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
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ganin.darv.DpadAwareRecyclerView;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.InjectingFragment;
import us.nineworlds.serenity.recyclerutils.SpaceItemDecoration;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.EpisodePosterOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;

public class EpisodeVideoGalleryFragment extends InjectingFragment {

    @Inject
    SharedPreferences preferences;

    @Inject
    GalleryVideoOnItemClickListener onItemClickListener;

    @Inject
    GalleryVideoOnItemLongClickListener onItemLongClickListener;

    @Inject
    protected MovieSelectedCategoryState categoryState;

    MoviePosterOnItemSelectedListener onItemSelectedListener;

    private DpadAwareRecyclerView videoGallery;

    public EpisodeVideoGalleryFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onItemSelectedListener = new MoviePosterOnItemSelectedListener();
        return inflater.inflate(R.layout.video_gallery_fragment, container);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (videoGallery == null) {

            videoGallery = (DpadAwareRecyclerView) getActivity().findViewById(
                    R.id.moviePosterView);

            String key = ((EpisodeBrowserActivity) getActivity()).getKey();

            videoGallery.setAdapter(new EpisodePosterImageGalleryAdapter());
            videoGallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            videoGallery.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.horizontal_spacing)));
            videoGallery
                    .setOnItemSelectedListener(new EpisodePosterOnItemSelectedListener());
            videoGallery.setOnItemClickListener(new EpisodePosterOnItemClickListener());
            videoGallery.setSelectorVelocity(0);
            EpisodeBrowserActivity activity = (EpisodeBrowserActivity) getActivity();
            activity.fetchEpisodes(key);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
