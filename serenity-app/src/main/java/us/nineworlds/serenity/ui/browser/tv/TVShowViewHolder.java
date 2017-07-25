package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.ganin.darv.DpadAwareRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;

public class TVShowViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.posterImageView)
    protected ImageView posterImageView;

    @BindView(R.id.posterWatchedIndicator)
    protected ImageView watchedView;

    @BindView(R.id.badge_count)
    protected TextView badgeCount;

    @BindView(R.id.posterInprogressIndicator)
    protected ProgressBar inprogressIndicatorView;

    public TVShowViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void reset() {
        watchedView.setVisibility(View.INVISIBLE);
        badgeCount.setVisibility(View.GONE);
        inprogressIndicatorView.setVisibility(View.INVISIBLE);
    }

    public void createImage(SeriesContentInfo pi, int imageWidth, int imageHeight) {
        int width = ImageUtils.getDPI(imageWidth, (Activity) itemView.getContext());
        int height = ImageUtils.getDPI(imageHeight, (Activity) itemView.getContext());

        initPosterMetaData(pi, width, height, false);

        itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width, height));
    }

    protected void initPosterMetaData(SeriesContentInfo pi, int width, int height, boolean isPoster) {
        posterImageView.setBackgroundResource(R.drawable.gallery_item_background);
        posterImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        posterImageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        posterImageView.setMaxHeight(height);
        posterImageView.setMaxWidth(width);

        if (isPoster) {
            loadImage(pi.getThumbNailURL());
        } else {
            loadImage(pi.getImageURL());
        }
    }

    protected void loadImage(String url) {
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(posterImageView.getContext(), android.R.color.black));
        Glide.with(posterImageView.getContext()).load(url).placeholder(colorDrawable).dontAnimate().into(posterImageView);
    }

    public void toggleWatchedIndicator(SeriesContentInfo pi) {
        int watched = 0;
        if (pi.getShowsWatched() != null) {
            watched = Integer.parseInt(pi.getShowsWatched());
        }

        watchedView.setVisibility(View.INVISIBLE);

        if (pi.isPartiallyWatched()) {
            toggleProgressIndicator(watched, pi.totalShows(), watchedView);
        }

        badgeCount.setText(pi.getShowsUnwatched());

        if (pi.isWatched()) {
            watchedView.setImageResource(R.drawable.overlaywatched);
            watchedView.setVisibility(View.VISIBLE);
            badgeCount.setVisibility(View.GONE);
        } else {
            badgeCount.setVisibility(View.VISIBLE);
        }
    }

    protected void toggleProgressIndicator(int dividend, int divisor, ImageView watchedView) {
        final float percentWatched = Float.valueOf(dividend) / Float.valueOf(divisor);

        int progress = Float.valueOf(percentWatched * 100).intValue();
        if (progress < 10) {
            progress = 10;
        }
        inprogressIndicatorView.setProgress(progress);
        inprogressIndicatorView.setVisibility(View.VISIBLE);
        watchedView.setVisibility(View.INVISIBLE);
    }

}
