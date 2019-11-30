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

package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;

import javax.inject.Inject;

/**
 * Display selected TV Show Information.
 *
 * @author dcarver
 */
public class TVShowGridOnItemSelectedListener extends AbstractVideoOnItemSelectedListener {

    private SeriesContentInfo videoInfo;
    private final Handler handler = new Handler();
    private Runnable runnable;

    @Inject
    SerenityClient factory;

    @BindView(R.id.tvShowGridTitle)
    TextView titleView;
    @BindView(R.id.fanArt)
    View fanArt;

    AbstractPosterImageGalleryAdapter adapter;

    public TVShowGridOnItemSelectedListener(AbstractPosterImageGalleryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void createVideoDetail(ImageView v) {
        // DO NOTHING
    }

    @Override
    public void onItemSelected(View view, int i) {
        ButterKnife.bind(this, (Activity) view.getContext());
        videoInfo = (SeriesContentInfo) adapter.getItem(i);

        final ImageView imageView = view.findViewById(R.id.posterImageView);

        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        runnable = () -> {
            changeBackgroundImage(imageView);
            runnable = null;
        };
        handler.postDelayed(runnable, 500);

        if (titleView != null) {
            titleView.setText(videoInfo.getTitle());
        }
    }

    /**
     * Change the background image of the activity.
     * <p>
     * Should be a background activity
     */
    private void changeBackgroundImage(View v) {
        final Activity context = (Activity) v.getContext();

        String transcodingURL = factory.createImageURL(videoInfo.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.movies, fanArt));
            }
        };

        if (context != null && !context.isDestroyed()) {
            GlideApp.with(context).asBitmap().load(transcodingURL).into(target);
        }
    }
}
