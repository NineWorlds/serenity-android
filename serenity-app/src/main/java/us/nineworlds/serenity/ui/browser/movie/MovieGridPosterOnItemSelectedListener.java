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

package us.nineworlds.serenity.ui.browser.movie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.ganin.darv.DpadAwareRecyclerView;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;

/**
 * When a poster is selected, update the information displayed in the browser.
 *
 * @author dcarver
 *
 */
public class MovieGridPosterOnItemSelectedListener extends BaseInjector
        implements DpadAwareRecyclerView.OnItemSelectedListener {

    private static Activity context;
    private AbstractPosterImageGalleryAdapter adapter;

    @Inject
    PlexappFactory plexFactory;

    int lastPos = -1;

    public MovieGridPosterOnItemSelectedListener() {
        super();

    }

    private void changeBackgroundImage(VideoContentInfo videoInfo) {
        if (videoInfo.getBackgroundURL() == null) {
            return;
        }

        final View fanArt = context.findViewById(R.id.fanArt);
        String transcodingURL = plexFactory.getImageURL(
                videoInfo.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.movies,
                        fanArt));
            }
        };

        Glide.with(context).load(transcodingURL).asBitmap().into(target);
    }

    private void createMovieMetaData(VideoContentInfo videoContentInfo) {
        VideoContentInfo mi = videoContentInfo;
        TextView subt = (TextView) context.findViewById(R.id.subtitleFilter);
        subt.setVisibility(View.GONE);
        Spinner subtitleSpinner = (Spinner) context
                .findViewById(R.id.videoSubtitle);
        subtitleSpinner.setVisibility(View.GONE);

        TextView posterTitle = (TextView) context
                .findViewById(R.id.movieActionBarPosterTitle);
        posterTitle.setText(mi.getTitle());
    }

    @Override
    public void onItemSelected(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {
        context = (Activity) view.getContext();

        if (lastPos != i) {
            lastPos = i;
        } else {
            return;
        }

        adapter = (AbstractPosterImageGalleryAdapter) dpadAwareRecyclerView.getAdapter();
        if (i > adapter.getItemCount()) {
            return;
        }

        VideoContentInfo videoContentInfo = (VideoContentInfo) adapter.getItem(i);
        if (videoContentInfo == null) {
            return;
        }

        changeBackgroundImage(videoContentInfo);

        createMovieMetaData(videoContentInfo);

    }

    @Override
    public void onItemFocused(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {

    }
}
