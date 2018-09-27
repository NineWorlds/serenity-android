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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.BackgroundBitmapDisplayer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.util.ImageUtils;

import static butterknife.ButterKnife.findById;

public class EpisodePosterOnItemSelectedListener extends AbstractVideoOnItemSelectedListener {

  @BindView(R.id.video_poster) ImageView posterImage;
  @BindView(R.id.video_details_container) View cardView;
  @BindView(R.id.movieActionBarPosterTitle) TextView seriesTitle;
  @BindView(R.id.movieSummary) TextView episodeSummary;
  @BindView(R.id.movieBrowserPosterTitle) TextView title;
  @BindView(R.id.videoTextExtra) TextView videoTextExtra;
  @BindView(R.id.fanArt) View fanArt;
  @BindView(R.id.categoryFilter) View categoryFilter;
  @BindView(R.id.categoryFilter2) View categoryFilter2;
  @BindView(R.id.movieCategoryName) View categoryName;
  @BindView(R.id.subtitleFilter) TextView subtFilter;
  @BindView(R.id.videoSubtitle) Spinner subtitleSpinner;

  ImageView posterImageView;

  private static final String DISPLAY_DATE_FORMAT = "MMMM d, yyyy";
  private static final String DATE_FORMAT = "yyyy-MM-dd";

  private String prevTitle;
  private boolean fadeIn = true;
  private int fadeInCount = 0;

  public EpisodePosterOnItemSelectedListener() {
  }

  @Override public void createVideoDetail(ImageView v) {
    Activity context = getActivity(v.getContext());
    if (cardView != null) {
      cardView.setVisibility(View.VISIBLE);
    }

    posterImage.setVisibility(View.VISIBLE);
    posterImage.setScaleType(ScaleType.FIT_XY);
    if (videoInfo.getParentPosterURL() != null) {
      int width = ImageUtils.getDPI(240, context);
      int height = ImageUtils.getDPI(330, context);
      posterImage.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
      Glide.with(context)
          .load(videoInfo.getParentPosterURL())
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .dontAnimate()
          .into(posterImage);
    } else if (videoInfo.getGrandParentPosterURL() != null) {
      int width = ImageUtils.getDPI(240, context);
      int height = ImageUtils.getDPI(330, context);
      posterImage.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
      Glide.with(context)
          .load(videoInfo.getGrandParentPosterURL())
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .dontAnimate()
          .into(posterImage);
    } else {
      Glide.with(context)
          .load(videoInfo.getImageURL())
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

  @Override public void changeBackgroundImage(final Activity context) {
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
      public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
        context.runOnUiThread(new BackgroundBitmapDisplayer(resource, R.drawable.tvshows, fanArt));
      }
    };

    Glide.with(context).load(transcodingURL).asBitmap().into(target);
  }

  @Override protected void createVideoMetaData(ImageView v) {
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
  public void onItemSelected(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i,
      long l) {
    Activity context = getActivity(view.getContext());

    if (context.isDestroyed()) {
      return;
    }

    ButterKnife.bind(this, context);

    if (i < 0) {
      i = 0;
    }

    EpisodePosterImageGalleryAdapter adapter =
        (EpisodePosterImageGalleryAdapter) dpadAwareRecyclerView.getAdapter();
    if (i > adapter.getItemCount()) {
      return;
    }

    position = i;

    videoInfo = (VideoContentInfo) adapter.getItem(position);
    if (videoInfo == null) {
      return;
    }

    changeBackgroundImage(context);

    posterImageView = findById(view, R.id.posterImageView);
    currentView = posterImageView;

    createVideoDetail(posterImageView);
    createVideoMetaData(posterImageView);
    createInfographicDetails(posterImageView);
  }

  @Override
  public void onItemFocused(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {

  }
}
