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
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.ganin.darv.DpadAwareRecyclerView;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.ImageUtils;

/**
 * Display selected TV Show Information.
 *
 * @author dcarver
 *
 */
public class TVShowGalleryOnItemSelectedListener extends BaseInjector implements DpadAwareRecyclerView.OnItemSelectedListener {

    private SerenityMultiViewVideoActivity context;
    private SeriesContentInfo info;

    @Inject
    protected PlexappFactory factory;

    public TVShowGalleryOnItemSelectedListener() {
        super();
    }

    private void createTVShowDetail(ImageView v) {

        createSummary();

        createTitle();

        ImageView imageView = (ImageView) context
                .findViewById(R.id.tvShowRating);
        ImageInfographicUtils infog = new ImageInfographicUtils(74, 40);

        int w = ImageUtils.getDPI(74, (Activity) v.getContext());
        int h = ImageUtils.getDPI(40, (Activity) v.getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        params.topMargin = 10;
        params.rightMargin = 5;
        imageView.setLayoutParams(params);

        ImageView content = infog.createTVContentRating(
                info.getContentRating(), context);
        imageView.setImageDrawable(content.getDrawable());
        imageView.setScaleType(ScaleType.FIT_XY);

        ImageView studiov = (ImageView) context.findViewById(R.id.tvShowStudio);
        if (info.getStudio() != null) {
            studiov.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams sparams = new LinearLayout.LayoutParams(
                    w, h);
            sparams.rightMargin = 5;
            sparams.topMargin = 10;
            sparams.leftMargin = 5;

            studiov.setLayoutParams(sparams);
            String studio = info.getStudio();
            studio = fixStudio(studio);
            String studioUrl = factory.getMediaTagURL("studio", studio,
                    info.getMediaTagIdentifier());
            Glide.with(context).load(studioUrl).into(studiov);
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
        TextView title = (TextView) context.findViewById(R.id.tvBrowserTitle);
        title.setText(info.getTitle());
    }

    protected void createSummary() {
        TextView summary = (TextView) context
                .findViewById(R.id.tvShowSeriesSummary);
        String plotSummary = info.getSummary();
        if (plotSummary == null) {
            summary.setText("");
        } else {
            summary.setText(plotSummary);
        }
    }

    protected void createRatings() {
        RatingBar ratingBar = (RatingBar) context
                .findViewById(R.id.tvRatingbar);
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
     *
     * Should be a background activity
     *
     * @param v
     */
    private void changeBackgroundImage(View v) {

        SeriesContentInfo mi = info;

        final View fanArt = context.findViewById(R.id.fanArt);

        String transcodingURL = factory.getImageURL(
                mi.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.movies,
                        fanArt));
            }
        };

        Glide.with(context).load(transcodingURL).asBitmap().into(target);

        ImageView showImage = (ImageView) context
                .findViewById(R.id.tvShowImage);
        showImage.setVisibility(View.VISIBLE);
        showImage.setScaleType(ScaleType.FIT_XY);
        int width = ImageUtils.getDPI(250, context);
        int height = ImageUtils.getDPI(350, context);
        showImage.setMaxHeight(height);
        showImage.setMaxWidth(width);
        showImage
                .setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        Glide.with(context).load(mi.getThumbNailURL()).into(showImage);
    }

    @Override
    public void onItemSelected(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {
        context = (SerenityMultiViewVideoActivity) view.getContext();
        if (context.isDestroyed()) {
            return;
        }
        AbstractPosterImageGalleryAdapter abstractPosterImageGalleryAdapter = (AbstractPosterImageGalleryAdapter) dpadAwareRecyclerView.getAdapter();
        info = (SeriesContentInfo) abstractPosterImageGalleryAdapter.getItem(i);

        ImageView imageView = (ImageView) view.findViewById(R.id.posterImageView);

        createTVShowDetail(imageView);
        changeBackgroundImage(imageView);

    }

    @Override
    public void onItemFocused(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {

    }
}
