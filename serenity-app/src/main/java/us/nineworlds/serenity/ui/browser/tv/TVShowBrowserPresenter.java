package us.nineworlds.serenity.ui.browser.tv;

import android.support.annotation.NonNull;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.birbit.android.jobqueue.JobManager;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.SecondaryCategoryMediaContainer;
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer;
import us.nineworlds.serenity.core.model.impl.TVCategoryMediaContainer;
import us.nineworlds.serenity.events.TVCategoryEvent;
import us.nineworlds.serenity.events.TVCategorySecondaryEvent;
import us.nineworlds.serenity.events.TVShowRetrievalEvent;
import us.nineworlds.serenity.jobs.TVCategoryJob;
import us.nineworlds.serenity.jobs.TVShowRetrievalJob;

@InjectViewState
@StateStrategyType(SkipStrategy.class)
public class TVShowBrowserPresenter extends MvpPresenter<TVShowBrowserView> {

  @Inject EventBus eventBus;
  @Inject JobManager jobManager;

  public TVShowBrowserPresenter() {
    super();
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }

  @Override public void attachView(TVShowBrowserView view) {
    super.attachView(view);
    eventBus.register(this);
  }

  @Override public void detachView(TVShowBrowserView view) {
    super.detachView(view);
    eventBus.unregister(this);
  }

  public void fetchTVCategories(@NonNull String key) {
    jobManager.addJobInBackground(new TVCategoryJob(key));
  }

  public void fetchTVShows(@NonNull String key, @NonNull String category) {
    TVShowRetrievalJob tvShowRetrievalJob = new TVShowRetrievalJob(key, category);
    jobManager.addJobInBackground(tvShowRetrievalJob);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onTVCategoryResponse(TVCategoryEvent event) {
    TVCategoryMediaContainer categoryMediaContainer = new TVCategoryMediaContainer(event.getMediaContainer());
    List<CategoryInfo> categories = categoryMediaContainer.createCategories();
    getViewState().updateCategories(categories);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onTVShowResponse(TVShowRetrievalEvent event) {
    List<SeriesContentInfo> tvShowList = new SeriesMediaContainer(event.getMediaContainer()).createSeries();
    getViewState().displayShows(tvShowList, event.getCategory());
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onTVCategorySecondaryResponse(TVCategorySecondaryEvent event) {
    SecondaryCategoryMediaContainer scMediaContainer =
        new SecondaryCategoryMediaContainer(event.getMediaContainer(), event.getCategory());

    List<SecondaryCategoryInfo> secondaryCategories = scMediaContainer.createCategories();
    getViewState().populateSecondaryCategories(secondaryCategories, event.getCategory());
  }
}
