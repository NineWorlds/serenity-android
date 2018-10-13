package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;

public class TVShowPosterViewHolder extends TVShowViewHolder {

  public TVShowPosterViewHolder(View itemView) {
    super(itemView);
  }

  @Override public void createImage(SeriesContentInfo pi, int imageWidth, int imageHeight) {
    int width = ImageUtils.getDPI(130, (Activity) itemView.getContext());
    int height = ImageUtils.getDPI(200, (Activity) itemView.getContext());

    initPosterMetaData(pi, width, height, true);

    itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));
  }
}
