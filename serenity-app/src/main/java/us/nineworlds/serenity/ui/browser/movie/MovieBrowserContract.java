package us.nineworlds.serenity.ui.browser.movie;

import moxy.MvpView;
import java.util.List;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.events.MainCategoryEvent;
import us.nineworlds.serenity.events.MovieRetrievalEvent;
import us.nineworlds.serenity.events.MovieSecondaryCategoryEvent;

/**
 * Created by dcarver on 10/10/17.
 */

public interface MovieBrowserContract {

  interface MovieBrowserView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void populateCategory(List<CategoryInfo> categories, String key);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void populateSecondaryCategory(List<SecondaryCategoryInfo> categories, String key, String category);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void displayPosters(List<VideoContentInfo> videos);
  }

  interface MoviewBrowserPresenter {

    void onMainCategoryResponse(MainCategoryEvent event);

    void onSecondaryCategoryEvent(MovieSecondaryCategoryEvent event);

    void onMoviePosterResponse(MovieRetrievalEvent event);

    void fetchVideos(String key, String category);
  }
}
