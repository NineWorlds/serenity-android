package us.nineworlds.serenity.ui.views.viewholders;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.widgets.RoundedImageView;

public abstract class AbstractPosterImageViewHolder<T extends ContentInfo> extends RecyclerView.ViewHolder {

    @BindView(R.id.posterInprogressIndicator)
    public ProgressBar posterInprogressIndicator;

    @BindView(R.id.posterWatchedIndicator)
    public ImageView watchedView;

    @BindView(R.id.infoGraphicMeta) @Nullable
    public LinearLayout infoGraphicMetaContainer;

    @BindView(R.id.posterImageView)
    public RoundedImageView posterImageView;


    public AbstractPosterImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Resets the view back to the initial state
     */
    public abstract void reset();

    public abstract void createImage(T contentInfo, int width, int height);

    public abstract void toggleWatchedIndicator(T contentInfo);

    public void loadImage(String url) {
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(posterImageView.getContext(), android.R.color.black));
        Glide.with(posterImageView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(colorDrawable).dontAnimate().into(posterImageView);
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
