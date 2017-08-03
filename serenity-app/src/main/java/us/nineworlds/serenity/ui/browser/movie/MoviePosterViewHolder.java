package us.nineworlds.serenity.ui.browser.movie;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.viewholders.AbstractPosterImageViewHolder;

public class MoviePosterViewHolder extends AbstractPosterImageViewHolder<VideoContentInfo> {

  public MoviePosterViewHolder(View itemView) {
    super(itemView);
  }

  @Override public void reset() {
    watchedView.setVisibility(View.INVISIBLE);
    posterInprogressIndicator.setVisibility(View.INVISIBLE);
  }

  public void createImage(VideoContentInfo pi, int imageWidth, int imageHeight) {
    int width = ImageUtils.getDPI(imageWidth, (Activity) itemView.getContext());
    int height = ImageUtils.getDPI(imageHeight, (Activity) itemView.getContext());

    initPosterMetaData(pi, width, height);

    itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width, height));
  }

  protected void initPosterMetaData(VideoContentInfo pi, int width, int height) {
    posterImageView.setBackgroundResource(R.drawable.gallery_item_background);
    posterImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    posterImageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
    posterImageView.setMaxHeight(height);
    posterImageView.setMaxWidth(width);

    loadImage(pi.getImageURL());
  }

  public void toggleWatchedIndicator(VideoContentInfo pi) {
    watchedView.setVisibility(View.INVISIBLE);

    if (pi.isPartiallyWatched()) {
      ImageUtils.toggleProgressIndicator(itemView, pi.getResumeOffset(), pi.getDuration());
    } else if (pi.isWatched()) {
      watchedView.setImageResource(R.drawable.overlaywatched);
      watchedView.setVisibility(View.VISIBLE);
    }
  }
}
