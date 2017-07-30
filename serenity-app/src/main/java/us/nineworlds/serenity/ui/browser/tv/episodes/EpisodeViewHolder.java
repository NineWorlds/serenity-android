package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.ganin.darv.DpadAwareRecyclerView;

import butterknife.BindView;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.viewholders.AbstractPosterImageViewHolder;

public class EpisodeViewHolder extends AbstractPosterImageViewHolder<VideoContentInfo> {

    @BindView(R.id.metaOverlay)
    TextView metaData;

    @BindView(R.id.posterOverlayTitle)
    TextView title;

    public EpisodeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void reset() {
        posterInprogressIndicator.setVisibility(View.INVISIBLE);
        watchedView.setVisibility(View.INVISIBLE);
        metaData.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
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

    @Override
    public void loadImage(String url) {
        super.loadImage(url);
    }

    public void toggleWatchedIndicator(VideoContentInfo pi) {
        watchedView.setVisibility(View.INVISIBLE);

        if (pi.isPartiallyWatched()) {
            ImageUtils.toggleProgressIndicator(itemView, pi.getResumeOffset(), pi.getDuration());
            return;
        }

        if (pi.isWatched()) {
            watchedView.setImageResource(R.drawable.overlaywatched);
            watchedView.setVisibility(View.VISIBLE);
        }
    }

    public void updateSeasonsTitle(VideoContentInfo pi) {
        String metaText = "";
        if (pi.getSeason() != null) {
            metaText = pi.getSeason() + " ";
        }

        if (pi.getEpisode() != null) {
            metaText = metaText + pi.getEpisode();
        }

        if (metaText.length() > 0) {
            metaData.setText(metaText);
            metaData.setVisibility(View.VISIBLE);
        }

        title.setText(pi.getTitle());
        title.setVisibility(View.VISIBLE);
    }

}
