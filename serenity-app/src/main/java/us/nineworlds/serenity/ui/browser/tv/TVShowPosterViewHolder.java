package us.nineworlds.serenity.ui.browser.tv;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

public class TVShowPosterViewHolder extends TVShowViewHolder {

  public TVShowPosterViewHolder(View itemView) {
    super(itemView);
  }

  @Override public void createImage(SeriesContentInfo pi, int imageWidth, int imageHeight, boolean isPoster) {
    initPosterMetaData(pi, imageWidth, imageHeight, isPoster);
    itemView.setLayoutParams(new RecyclerView.LayoutParams(imageWidth, imageHeight));
  }
}
