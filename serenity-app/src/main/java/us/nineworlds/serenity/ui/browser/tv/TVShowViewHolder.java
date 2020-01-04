package us.nineworlds.serenity.ui.browser.tv;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.views.viewholders.AbstractPosterImageViewHolder;

public class TVShowViewHolder extends AbstractPosterImageViewHolder<SeriesContentInfo> {

  @BindView(R.id.badge_count) @Nullable public TextView badgeCount;

  public TVShowViewHolder(View itemView) {
    super(itemView);
  }

  public void reset() {
    watchedView.setVisibility(View.INVISIBLE);
    badgeCount.setVisibility(View.GONE);
    posterInprogressIndicator.setVisibility(View.INVISIBLE);
  }

  public Context getContext() {
    return itemView.getContext();
  }

  public void createImage(SeriesContentInfo pi, int imageWidth, int imageHeight, boolean isPoster) {
    initPosterMetaData(pi, imageWidth, imageHeight, isPoster);

    itemView.setLayoutParams(new RecyclerView.LayoutParams(imageWidth, imageHeight));
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

  public void toggleWatchedIndicator(SeriesContentInfo pi) {
    int watched = 0;
    watchedView.setVisibility(View.INVISIBLE);
    badgeCount.setVisibility(View.VISIBLE);

    if (pi.getShowsWatched() != null) {
      watched = Integer.parseInt(pi.getShowsWatched());
    }

    if (pi.isPartiallyWatched()) {
      toggleProgressIndicator(watched, pi.totalShows());
    }

    badgeCount.setText(pi.getShowsUnwatched());

    if (pi.isWatched()) {
      watchedView.setImageResource(R.drawable.overlaywatched);
      watchedView.setVisibility(View.VISIBLE);
      badgeCount.setVisibility(View.GONE);
    }
  }
}
