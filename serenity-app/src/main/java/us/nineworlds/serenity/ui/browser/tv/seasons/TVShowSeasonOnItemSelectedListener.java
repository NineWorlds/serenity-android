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

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import timber.log.Timber;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;

import javax.inject.Inject;

public class TVShowSeasonOnItemSelectedListener extends AbstractVideoOnItemSelectedListener {

    private final Activity context;
    private SeriesContentInfo info;

    @Inject
    protected SerenityClient plexFactory;
    boolean firstTimeSw = true;

    AbstractPosterImageGalleryAdapter adapter;

    public TVShowSeasonOnItemSelectedListener(View bgv, Activity activity, AbstractPosterImageGalleryAdapter adapter) {
        super();
        context = activity;
        this.adapter = adapter;
    }

    private void changeBackgroundImage(View v) {

        SeriesContentInfo mi = info;

        if (mi.getBackgroundURL() != null) {
            final View fanArt = context.findViewById(R.id.fanArt);
            String transcodingURL = plexFactory.createImageURL(mi.getBackgroundURL(), 1280, 720);
            Timber.d("Season Background Image url: " + transcodingURL);

            SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.tvshows, fanArt));
                }
            };

            GlideApp.with(context).asBitmap().load(transcodingURL).into(target);
        }
    }

    @Override
    protected void createVideoDetail(ImageView v) {
        // DO NOTHING
    }

    @Override
    public void onItemSelected(View view, int i) {
        Activity context = (Activity) view.getContext();
        if (context.isDestroyed()) {
            return;
        }

        if (firstTimeSw) {
            firstTimeSw = false;
            return;
        }

        if (i < 0) {
            Log.e(TVShowSeasonOnItemSelectedListener.class.getCanonicalName(),
                    "Season list size: " + adapter.getItemCount() + " position: " + i);
            i = 0;
        }

        info = (SeriesContentInfo) adapter.getItem(i);
        ImageView mpiv = view.findViewById(R.id.posterImageView);

        TextView seasonsTitle = context.findViewById(R.id.tvShowSeasonsTitle);
        seasonsTitle.setText(info.getTitle());

        changeBackgroundImage(mpiv);

        TVShowSeasonBrowserActivity activity = (TVShowSeasonBrowserActivity) getActivity(context);
        activity.fetchEpisodes(info.getKey());
    }

}
