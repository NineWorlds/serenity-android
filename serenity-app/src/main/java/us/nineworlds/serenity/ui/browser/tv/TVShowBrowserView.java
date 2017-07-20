package us.nineworlds.serenity.ui.browser.tv;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

@StateStrategyType(SkipStrategy.class)
public interface TVShowBrowserView extends MvpView {

    void updateCategories(List<CategoryInfo> categories);

    @StateStrategyType(SkipStrategy.class)
    void displayShows(List<SeriesContentInfo> series, String category);

    void populateSecondaryCategories(List<SecondaryCategoryInfo> catagories, String category);

    @StateStrategyType(SkipStrategy.class)
    void requestUpdatedVideos(String key, String category);
}
