package us.nineworlds.serenity.ui.views.statusoverlayview;

import com.arellomobile.mvp.MvpView;
import us.nineworlds.serenity.core.model.VideoContentInfo;

/**
 * Created by dcarver on 10/10/17.
 */

public interface StatusOverlayContract {
  interface StatusOverlayView extends MvpView {

    void reset();

    void initMvp();

    void toggleProgressIndicator(int dividend, int divisor);

    void populatePosterImage(String url);

    void toggleWatchedIndicator(VideoContentInfo contentInfo);

    void createImage(VideoContentInfo pi, int imageWidth, int imageHeight);

    void refresh();
  }

  interface StatusOverlayPresenter {

    void refresh();

    void setVideoContentInfo(VideoContentInfo videoContentInfo);
  }
}
