package us.nineworlds.serenity.ui.views.viewholders;

import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.widgets.RoundedImageView;

public abstract class AbstractPosterImageViewHolder<T extends ContentInfo>
    extends RecyclerView.ViewHolder {

  public ProgressBar posterInprogressIndicator;
  public ImageView watchedView;
  @Nullable public LinearLayout infoGraphicMetaContainer;
  public RoundedImageView posterImageView;

  boolean isZoomedOut = false;

  public boolean isZoomedOut() {
    return this.isZoomedOut;
  }

  public void setZoomedOut(boolean isZoomedOut) {
    this.isZoomedOut = isZoomedOut;
  }

  public AbstractPosterImageViewHolder(View itemView) {
    super(itemView);
    infoGraphicMetaContainer = itemView.findViewById(R.id.infoGraphicMeta);
    posterImageView = itemView.findViewById(R.id.posterImageView);
    posterInprogressIndicator = itemView.findViewById(R.id.posterInprogressIndicator);
    watchedView = itemView.findViewById(R.id.posterWatchedIndicator);
  }

  public View getItemView() {
    return itemView;
  }

  public abstract void reset();

  public abstract void createImage(T contentInfo, int width, int height, RecyclerView recyclerView);

  public abstract void toggleWatchedIndicator(T contentInfo);

  public void loadImage(String url) {
    ColorDrawable colorDrawable = new ColorDrawable(
        ContextCompat.getColor(posterImageView.getContext(), android.R.color.black));
    GlideApp.with(posterImageView.getContext())
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(colorDrawable)
        .dontAnimate()
        .into(posterImageView);
  }

  public void toggleProgressIndicator(int dividend, int divisor) {
    final float percentWatched = Float.valueOf(dividend) / Float.valueOf(divisor);

    int progress = Float.valueOf(percentWatched * 100).intValue();
    if (progress < 10) {
      progress = 10;
    }
    posterInprogressIndicator.setProgress(progress);
    posterInprogressIndicator.setVisibility(View.VISIBLE);
    watchedView.setVisibility(View.INVISIBLE);
  }
}
