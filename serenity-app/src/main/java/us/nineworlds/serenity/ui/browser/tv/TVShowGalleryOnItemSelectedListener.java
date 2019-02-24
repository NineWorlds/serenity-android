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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.*;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;

/**
 * Display selected TV Show Information.
 *
 * @author dcarver
 */
public class TVShowGalleryOnItemSelectedListener extends AbstractVideoOnItemSelectedListener {

    private Activity context;
    private SeriesContentInfo info;

    AbstractPosterImageGalleryAdapter adapter;

    public TVShowGalleryOnItemSelectedListener(AbstractPosterImageGalleryAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    private void createTVShowDetail() {

        createSummary();

        createTitle();

        int w = context.getResources().getDimensionPixelSize(R.dimen.tv_show_infographic_width);
        int h = context.getResources().getDimensionPixelSize(R.dimen.tv_show_infographic_height);

        ImageView imageView = context.findViewById(R.id.tvShowRating);
        ImageInfographicUtils infog = new ImageInfographicUtils(w, h);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        params.topMargin = 10;
        params.rightMargin = 5;
        imageView.setLayoutParams(params);
        GlideApp.with(context).load(infog.createTVContentRating(info.getContentRating())).into(imageView);

        ImageView studiov = context.findViewById(R.id.tvShowStudio);
        if (info.getStudio() != null) {
            studiov.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams sparams = new LinearLayout.LayoutParams(w, h);
            sparams.rightMargin = 5;
            sparams.topMargin = 10;
            sparams.leftMargin = 5;

            studiov.setLayoutParams(sparams);
            String studio = info.getStudio();
            studio = fixStudio(studio);
            String studioUrl = serenityClient.createMediaTagURL("studio", studio, info.getMediaTagIdentifier());
            GlideApp.with(context).load(studioUrl).into(studiov);
        } else {
            studiov.setVisibility(View.GONE);
        }

        createRatings();
    }

    private String fixStudio(String studio) {
        if ("FOX".equals(studio)) {
            return "Fox";
        }
        if ("Starz!".equals(studio)) {
            return "Starz";
        }
        return studio;
    }

    protected void createTitle() {
        TextView title = context.findViewById(R.id.tvBrowserTitle);
        title.setText(info.getTitle());
    }

    protected void createSummary() {
        TextView summary = context.findViewById(R.id.tvShowSeriesSummary);
        String plotSummary = info.getSummary();
        if (plotSummary == null) {
            summary.setText("");
        } else {
            summary.setText(plotSummary);
        }
    }

    protected void createRatings() {
        RatingBar ratingBar = context.findViewById(R.id.tvRatingbar);
        ratingBar.setMax(4);
        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.1f);
        ratingBar.setNumStars(4);
        ratingBar.setPadding(0, 0, 5, 0);
        double rating = info.getRating();
        ratingBar.setRating((float) (rating / 2.5));
    }

    /**
     * Change the background image of the activity.
     * <p>
     * Should be a background activity
     */
    private void changeBackgroundImage(View v) {

        SeriesContentInfo mi = info;

        final View fanArt = context.findViewById(R.id.fanArt);

        String transcodingURL = serenityClient.createImageURL(mi.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.movies, fanArt));
            }
        };

        GlideApp.with(context).asBitmap().load(transcodingURL).into(target);

        ImageView showImage = context.findViewById(R.id.tvShowImage);
        showImage.setVisibility(View.VISIBLE);
        showImage.setScaleType(ScaleType.FIT_XY);
        int width = context.getResources().getDimensionPixelSize(R.dimen.tv_show_image_width);
        int height = context.getResources().getDimensionPixelSize(R.dimen.tv_show_image_height);
        showImage.setMaxHeight(height);
        showImage.setMaxWidth(width);
        showImage.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        GlideApp.with(context).load(mi.getThumbNailURL()).fitCenter().into(showImage);
    }

    @Override
    public void onItemSelected(View view, int i) {
        context = (Activity) view.getContext();
        if (context.isDestroyed()) {
            return;
        }
        info = (SeriesContentInfo) adapter.getItem(i);

        ImageView imageView = view.findViewById(R.id.posterImageView);

        createTVShowDetail();
        changeBackgroundImage(imageView);
    }

    @Override
    protected void createVideoDetail(ImageView v) {
        // DO Nothing
    }
}
