package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.view.View;

import net.ganin.darv.DpadAwareRecyclerView;

import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;


public class TVShowPosterViewHolder extends TVShowViewHolder {

    public TVShowPosterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void createImage(SeriesContentInfo pi, int imageWidth, int imageHeight) {
        int width = ImageUtils.getDPI(130, (Activity) itemView.getContext());
        int height = ImageUtils.getDPI(200, (Activity) itemView.getContext());

        initPosterMetaData(pi, width, height, true);

        itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width, height));
    }

}
