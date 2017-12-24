package us.nineworlds.serenity.ui.browser.movie;

import com.arellomobile.mvp.MvpView;
import java.util.List;
import us.nineworlds.serenity.core.model.CategoryInfo;
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

    void populateCategory(List<CategoryInfo> categories, String key);

    void populateSecondaryCategory(List<SecondaryCategoryInfo> categories, String key, String category);

    void displayPosters(List<VideoContentInfo> videos);
  }

  interface MoviewBrowserPresenter {

    void onMainCategoryResponse(MainCategoryEvent event);

    void onSecondaryCategoryEvent(MovieSecondaryCategoryEvent event);

    void onMoviePosterResponse(MovieRetrievalEvent event);

    void fetchVideos(String key, String category);
  }
}
