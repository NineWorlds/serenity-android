package us.nineworlds.serenity.ui.browser.movie;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.CategoryMediaContainer;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.core.model.impl.SecondaryCategoryMediaContainer;
import us.nineworlds.serenity.events.MainCategoryEvent;
import us.nineworlds.serenity.events.MovieRetrievalEvent;
import us.nineworlds.serenity.events.MovieSecondaryCategoryEvent;
import us.nineworlds.serenity.injection.SerenityObjectGraph;
import us.nineworlds.serenity.jobs.MovieRetrievalJob;

@InjectViewState public class MovieBrowserPresenter extends MvpPresenter<MovieBrowserView> {

  @Inject EventBus eventBus;

  @Inject JobManager jobManager;

  public MovieBrowserPresenter() {
    super();
    SerenityObjectGraph.getInstance().inject(this);
  }

  @Override public void attachView(MovieBrowserView view) {
    super.attachView(view);
    eventBus.register(this);
  }

  @Override public void detachView(MovieBrowserView view) {
    super.detachView(view);
    eventBus.unregister(this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMainCategoryResponse(MainCategoryEvent event) {
    CategoryMediaContainer categoryMediaContainer =
        new CategoryMediaContainer(event.getMediaContainer());
    List<CategoryInfo> categories = categoryMediaContainer.createCategories();
    getViewState().populateCategory(categories, event.getKey());
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onSecondaryCategoryEvent(MovieSecondaryCategoryEvent event) {
    SecondaryCategoryMediaContainer scMediaContainer =
        new SecondaryCategoryMediaContainer(event.getMediaContainer(), event.getCategory());

    List<SecondaryCategoryInfo> secondaryCategories = scMediaContainer.createCategories();

    getViewState().populateSecondaryCategory(secondaryCategories, event.getKey(),
        event.getCategory());
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMoviePosterResponse(MovieRetrievalEvent event) {
    MovieMediaContainer movies = new MovieMediaContainer(event.getMediaContainer());
    List<VideoContentInfo> posterList = movies.createVideos();
    getViewState().displayPosters(posterList);
  }

  public void fetchVideos(String key, String category) {
    MovieRetrievalJob movieRetrievalJob = new MovieRetrievalJob(key, category);
    jobManager.addJobInBackground(movieRetrievalJob);
  }
}
