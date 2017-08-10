package us.nineworlds.serenity.ui.views.statusoverlayview;

import com.arellomobile.mvp.MvpView;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public interface StatusOverlayView extends MvpView {

  void reset();

  void initMvp();

  void toggleProgressIndicator(int dividend, int divisor);

  void populatePosterImage(String url);

  void toggleWatchedIndicator(VideoContentInfo contentInfo);

  void createImage(VideoContentInfo pi, int imageWidth, int imageHeight);
}
