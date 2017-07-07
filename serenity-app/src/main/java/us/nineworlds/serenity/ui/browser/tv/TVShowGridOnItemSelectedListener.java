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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import net.ganin.darv.DpadAwareRecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;

/**
 * Display selected TV Show Information.
 *
 * @author dcarver
 *
 */
public class TVShowGridOnItemSelectedListener extends BaseInjector implements
        DpadAwareRecyclerView.OnItemSelectedListener {

    private SeriesContentInfo videoInfo;
    private final Handler handler = new Handler();
    private Runnable runnable;

    @Inject
    PlexappFactory factory;

    @BindView(R.id.tvShowGridTitle) TextView titleView;
    @BindView(R.id.fanArt) View fanArt;

    @Override
    public void onItemSelected(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {
        ButterKnife.bind(this, (Activity) dpadAwareRecyclerView.getContext());
        AbstractPosterImageGalleryAdapter adapter = (AbstractPosterImageGalleryAdapter) dpadAwareRecyclerView.getAdapter();
        videoInfo = (SeriesContentInfo) adapter.getItem(i);

        final ImageView imageView = (ImageView) view.findViewById(R.id.posterImageView);

        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                changeBackgroundImage(imageView);
                runnable = null;
            }
        };
        handler.postDelayed(runnable, 500);

        if (titleView != null) {
            titleView.setText(videoInfo.getTitle());
        }

    }

    /**
     * Change the background image of the activity.
     *
     * Should be a background activity
     *
     * @param v
     */
    private void changeBackgroundImage(View v) {
        final Activity context = (Activity) v.getContext();

        String transcodingURL = factory.getImageURL(videoInfo.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.movies, fanArt));
            }
        };

        Glide.with(context).load(transcodingURL).asBitmap().into(target);
    }


    @Override
    public void onItemFocused(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {

    }
}
