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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.app.Activity;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Deprecated
public class EpisodePosterOnItemSelectedListener extends AbstractVideoOnItemSelectedListener {

    ImageView posterImage;
    View cardView;
    TextView seriesTitle;
    TextView episodeSummary;
    TextView title;
    TextView videoTextExtra;
    View fanArt;
    View categoryFilter;
    View categoryFilter2;
    View categoryName;
    TextView subtFilter;
    Spinner subtitleSpinner;

    ImageView posterImageView;

    private static final String DISPLAY_DATE_FORMAT = "MMMM d, yyyy";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private String prevTitle;
    private boolean fadeIn = true;
    private int fadeInCount = 0;

    AbstractPosterImageGalleryAdapter adapter;

    public EpisodePosterOnItemSelectedListener(AbstractPosterImageGalleryAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    @Override
    public void createVideoDetail(ImageView v) {
        Activity context = getActivity(v.getContext());
        if (cardView != null) {
            cardView.setVisibility(View.VISIBLE);
        }

        posterImage.setVisibility(View.VISIBLE);
        posterImage.setScaleType(ScaleType.FIT_XY);
        int width = context.getResources().getDimensionPixelSize(R.dimen.episode_detail_image_width);
        int height = context.getResources().getDimensionPixelSize(R.dimen.episode_detail_image_height);
        if (videoInfo.getParentPosterURL() != null) {
            posterImage.setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
            GlideApp.with(context)
                    .load(videoInfo.getParentPosterURL())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(posterImage);
        } else if (videoInfo.getGrandParentPosterURL() != null) {
            posterImage.setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
            GlideApp.with(context)
                    .load(videoInfo.getGrandParentPosterURL())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(posterImage);
        } else {
            GlideApp.with(context)
                    .load(videoInfo.getImageURL())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(posterImage);
        }

        if (videoInfo.getSeriesTitle() != null) {
            if (!videoInfo.getSeriesTitle().equals(prevTitle)) {
                fadeIn = true;
            } else {
                fadeInCount += 1;
                fadeIn = false;
            }
            seriesTitle.setVisibility(View.VISIBLE);
            seriesTitle.setText(videoInfo.getSeriesTitle());
            prevTitle = videoInfo.getSeriesTitle();
        } else {
            seriesTitle.setVisibility(View.GONE);
        }

        episodeSummary.setText(videoInfo.getSummary());

        String epTitle = videoInfo.getTitle();
        String season = videoInfo.getSeason();
        String episode = videoInfo.getEpisode();

        if (season != null || episode != null) {
            epTitle = epTitle + " - ";
        }

        if (season != null) {
            epTitle = epTitle + season + " ";
        }

        if (episode != null) {
            epTitle = epTitle + episode;
        }

        title.setText(epTitle);
        videoTextExtra.setVisibility(View.INVISIBLE);
        if (videoInfo.getOriginalAirDate() != null) {
            try {
                Date airDate = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(
                        videoInfo.getOriginalAirDate());
                SimpleDateFormat format = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
                String formatedDate = format.format(airDate);
                videoTextExtra.setVisibility(View.VISIBLE);
                videoTextExtra.setText("Aired " + formatedDate);
            } catch (ParseException ex) {
                Log.i(getClass().getName(), "Unable to parse date");
            }
        }
    }

    @Override
    public void changeBackgroundImage(final Activity context) {
        if (fadeIn == true || fadeInCount == 0) {
            super.changeBackgroundImage(context);
            fadeIn = false;
            fadeInCount += 1;
            return;
        }

        VideoContentInfo ei = videoInfo;

        if (ei.getBackgroundURL() == null) {
            return;
        }

        String transcodingURL = serenityClient.createImageURL(videoInfo.getBackgroundURL(), 1280, 720);

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(1280, 720) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.tvshows, fanArt));
            }
        };

        GlideApp.with(context).asBitmap().load(transcodingURL).into(target);
    }

    @Override
    protected void createVideoMetaData(ImageView v) {
        Activity context = getActivity(v.getContext());
        super.createVideoMetaData(v);

        categoryFilter.setVisibility(View.GONE);
        categoryFilter2.setVisibility(View.GONE);
        categoryName.setVisibility(View.GONE);
        subtFilter = (TextView) context.findViewById(R.id.subtitleFilter);
        subtFilter.setVisibility(View.GONE);
        subtitleSpinner.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(View view, int i) {
        Activity context = getActivity(view.getContext());

        if (context.isDestroyed()) {
            return;
        }

        posterImage = context.findViewById(R.id.video_poster);
        cardView = context.findViewById(R.id.video_details_container);
        seriesTitle = context.findViewById(R.id.movieActionBarPosterTitle);
        episodeSummary = context.findViewById(R.id.movieSummary);
        title = context.findViewById(R.id.movieBrowserPosterTitle);
        videoTextExtra = context.findViewById(R.id.videoTextExtra);
        fanArt = context.findViewById(R.id.fanArt);
        categoryFilter = context.findViewById(R.id.categoryFilter);
        categoryFilter2 = context.findViewById(R.id.categoryFilter2);
        categoryName = context.findViewById(R.id.movieCategoryName);
        subtFilter = context.findViewById(R.id.subtitleFilter);
        subtitleSpinner = context.findViewById(R.id.videoSubtitle);

        if (i < 0) {
            i = 0;
        }

        if (i > adapter.getItemCount()) {
            return;
        }

        position = i;

        videoInfo = (VideoContentInfo) adapter.getItem(position);
        if (videoInfo == null) {
            return;
        }

        changeBackgroundImage(context);

        posterImageView = view.findViewById(R.id.posterImageView);
        currentView = posterImageView;

        createVideoDetail(posterImageView);
        createVideoMetaData(posterImageView);
        createInfographicDetails(posterImageView);
    }
}
