package us.nineworlds.serenity.ui.browser.tv;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

public class TVShowPosterViewHolder extends TVShowViewHolder {

  public TVShowPosterViewHolder(View itemView) {
    super(itemView);
  }

  @Override public void createImage(SeriesContentInfo pi, int imageWidth, int imageHeight) {
    initPosterMetaData(pi, imageWidth, imageHeight, true);
    itemView.setLayoutParams(new RecyclerView.LayoutParams(imageWidth, imageHeight));
  }
}
