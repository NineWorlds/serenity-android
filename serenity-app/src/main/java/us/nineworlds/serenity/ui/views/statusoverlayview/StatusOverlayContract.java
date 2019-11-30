package us.nineworlds.serenity.ui.views.statusoverlayview;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import us.nineworlds.serenity.core.model.VideoContentInfo;

/**
 * Created by dcarver on 10/10/17.
 */

public interface StatusOverlayContract {
  interface StatusOverlayView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void reset();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void initMvp();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void toggleProgressIndicator(int dividend, int divisor);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void populatePosterImage(String url);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void toggleWatchedIndicator(VideoContentInfo contentInfo);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void createImage(VideoContentInfo pi, int imageWidth, int imageHeight);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void refresh();
  }

  interface StatusOverlayPresenter {

    void refresh();

    void setVideoContentInfo(VideoContentInfo videoContentInfo);
  }
}
