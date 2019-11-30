package us.nineworlds.serenity.ui.browser.tv.episodes;

import moxy.MvpView;
import java.util.List;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public interface EpisodeBrowserView extends MvpView {

  @StateStrategyType(AddToEndSingleStrategy.class)
  void fetchEpisodes(String key);

  @StateStrategyType(AddToEndSingleStrategy.class)
  void updateGallery(List<VideoContentInfo> episodes);
}
