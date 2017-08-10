package us.nineworlds.serenity.ui.views.statusoverlayview;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import us.nineworlds.serenity.core.model.VideoContentInfo;

@InjectViewState
@StateStrategyType(SkipStrategy.class)
public class StatusOverlayPresenter extends MvpPresenter<StatusOverlayView> {

  VideoContentInfo videoContentInfo;

  public void setVideoContentInfo(VideoContentInfo videoContentInfo) {
    this.videoContentInfo = videoContentInfo;
  }
}
