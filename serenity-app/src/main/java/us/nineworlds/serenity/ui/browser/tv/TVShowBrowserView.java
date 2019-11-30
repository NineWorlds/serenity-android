package us.nineworlds.serenity.ui.browser.tv;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import java.util.List;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

@StateStrategyType(SkipStrategy.class) public interface TVShowBrowserView extends MvpView {

  @StateStrategyType(AddToEndSingleStrategy.class)
  void updateCategories(List<CategoryInfo> categories);

  @StateStrategyType(SkipStrategy.class) void displayShows(List<SeriesContentInfo> series,
      String category);

  @StateStrategyType(AddToEndSingleStrategy.class)
  void populateSecondaryCategories(List<SecondaryCategoryInfo> catagories, String category);

  @StateStrategyType(SkipStrategy.class) void requestUpdatedVideos(String key, String category);
}
