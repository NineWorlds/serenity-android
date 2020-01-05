package us.nineworlds.serenity.ui.views.statusoverlayview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import moxy.MvpPresenter;
import moxy.presenter.InjectPresenter;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import us.nineworlds.serenity.GlideApp;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.mvp.MvpFrameLayout;
import us.nineworlds.serenity.widgets.RoundedImageView;

public class StatusOverlayFrameLayout extends MvpFrameLayout implements StatusOverlayContract.StatusOverlayView {

  @InjectPresenter StatusOverlayPresenter presenter;

  @BindView(R.id.posterImageView) RoundedImageView roundedImageView;
  @BindView(R.id.posterOverlayTitle) TextView posterOverlayTitle;
  @BindView(R.id.metaOverlay) TextView metaOverlay;
  @BindView(R.id.posterWatchedIndicator) ImageView posterWatchedIndicator;
  @BindView(R.id.infoGraphicMeta) LinearLayout infoGraphicMetaContainer;
  @BindView(R.id.subtitleIndicator) ImageView subtitleIndicator;
  @BindView(R.id.trailerIndicator) ImageView trailerIndicator;
  @BindView(R.id.posterInprogressIndicator) ProgressBar posterInProgressIndicator;
  Drawable placeHolder = null;

  public StatusOverlayFrameLayout(@NonNull Context context) {
    super(context);
    init();
  }

  public StatusOverlayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public StatusOverlayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public StatusOverlayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr,
      @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  protected void init() {
    inflate(getContext(), R.layout.status_overlay_view, this);
    ButterKnife.bind(this, this);

    int colorTint = ContextCompat.getColor(getContext(), R.color.lb_tv_white);
    placeHolder = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_image_placeholder));
    DrawableCompat.setTint(placeHolder, colorTint);
  }

  @Override protected MvpPresenter getPresenter() {
    return presenter;
  }

  @Override public void setTag(Object tag) {
    super.setTag(tag);
    if (tag instanceof VideoContentInfo) {
      VideoContentInfo videoContentInfo = (VideoContentInfo) tag;
      presenter.setVideoContentInfo(videoContentInfo);
    }
  }

  @Override public void reset() {
    posterWatchedIndicator.setVisibility(View.INVISIBLE);
    posterInProgressIndicator.setVisibility(View.INVISIBLE);
  }

  @Override public void initMvp() {
    if (presenter == null) {
      getMvpDelegate().onCreate();
      getMvpDelegate().onAttach();
    }
  }

  @Override public void toggleProgressIndicator(int dividend, int divisor) {
    final float percentWatched = Float.valueOf(dividend) / Float.valueOf(divisor);

    int progress = Float.valueOf(percentWatched * 100).intValue();
    if (progress < 10) {
      progress = 10;
    }
    posterInProgressIndicator.setProgress(progress);
    posterInProgressIndicator.setVisibility(View.VISIBLE);
    posterWatchedIndicator.setVisibility(View.INVISIBLE);
  }

  protected void initPosterMetaData(VideoContentInfo pi, int width, int height) {
    roundedImageView.setBackgroundResource(R.drawable.gallery_item_background);
    roundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    roundedImageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
    roundedImageView.setMaxHeight(height);
    roundedImageView.setMaxWidth(width);

    populatePosterImage(pi.getImageURL());
  }

  @Override public void populatePosterImage(String url) {
    ColorDrawable colorDrawable =
        new ColorDrawable(ContextCompat.getColor(roundedImageView.getContext(), android.R.color.black));

    GlideApp.with(getContext())
        .load(url)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(placeHolder)
        .dontAnimate()
        .into(roundedImageView);
  }

  @Override public void toggleWatchedIndicator(VideoContentInfo contentInfo) {
    posterWatchedIndicator.setVisibility(View.INVISIBLE);

    if (contentInfo.isPartiallyWatched()) {
      ImageUtils.toggleProgressIndicator(this, contentInfo.getResumeOffset(), contentInfo.getDuration());
      return;
    }

    if (contentInfo.isWatched()) {
      posterWatchedIndicator.setImageResource(R.drawable.overlaywatched);
      posterWatchedIndicator.setVisibility(View.VISIBLE);
    }
  }

  @Override public void createImage(VideoContentInfo pi, int imageWidth, int imageHeight) {
    initPosterMetaData(pi, imageWidth, imageHeight);

    setLayoutParams(new RecyclerView.LayoutParams(imageWidth, imageHeight));
  }

  @Override public void refresh() {
    presenter.refresh();
  }
}
