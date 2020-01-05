package us.nineworlds.serenity.ui.views.viewholders;

import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.widgets.RoundedImageView;

public abstract class AbstractPosterImageViewHolder<T extends ContentInfo>
    extends RecyclerView.ViewHolder {

  @BindView(R.id.posterInprogressIndicator) public ProgressBar posterInprogressIndicator;
  @BindView(R.id.posterWatchedIndicator) public ImageView watchedView;
  @BindView(R.id.infoGraphicMeta) @Nullable public LinearLayout infoGraphicMetaContainer;
  @BindView(R.id.posterImageView) public RoundedImageView posterImageView;

  Drawable placeHolder = null;

  boolean isZoomedOut = false;

  public boolean isZoomedOut() {
    return this.isZoomedOut;
  }

  public void setZoomedOut(boolean isZoomedOut) {
    this.isZoomedOut = isZoomedOut;
  }

  public AbstractPosterImageViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    int colorTint = ContextCompat.getColor(itemView.getContext(), R.color.lb_tv_white);
    placeHolder = DrawableCompat.wrap(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_image_placeholder));
    DrawableCompat.setTint(placeHolder, colorTint);
  }

  public View getItemView() {
    return itemView;
  }

  public abstract void reset();

  public abstract void createImage(T contentInfo, int width, int height, boolean isPoster);

  public abstract void toggleWatchedIndicator(T contentInfo);

  public void loadImage(String url) {
    GlideApp.with(posterImageView.getContext())
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(placeHolder)
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
