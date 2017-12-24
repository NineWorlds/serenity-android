package us.nineworlds.serenity.ui.views.statusoverlayview;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import us.nineworlds.serenity.core.model.VideoContentInfo;

@InjectViewState
@StateStrategyType(SkipStrategy.class)
public class StatusOverlayPresenter extends MvpPresenter<StatusOverlayContract.StatusOverlayView>
    implements StatusOverlayContract.StatusOverlayPresenter {

  VideoContentInfo videoContentInfo;

  @Override public void setVideoContentInfo(VideoContentInfo videoContentInfo) {
    this.videoContentInfo = videoContentInfo;
  }

  @Override public void refresh() {
    getViewState().reset();
    getViewState().toggleWatchedIndicator(videoContentInfo);
    getViewState().populatePosterImage(videoContentInfo.getImageURL());
  }
}
