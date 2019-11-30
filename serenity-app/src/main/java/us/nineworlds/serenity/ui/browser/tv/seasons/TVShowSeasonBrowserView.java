package us.nineworlds.serenity.ui.browser.tv.seasons;

import moxy.MvpView;
import java.util.List;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public interface TVShowSeasonBrowserView extends MvpView {

  @StateStrategyType(AddToEndSingleStrategy.class)
  void fetchEpisodes(String key);

  @StateStrategyType(AddToEndSingleStrategy.class)
  void updateEpisodes(List<VideoContentInfo> episodes);

  @StateStrategyType(AddToEndSingleStrategy.class)
  void populateSeasons(List<SeriesContentInfo> seasons);
}
