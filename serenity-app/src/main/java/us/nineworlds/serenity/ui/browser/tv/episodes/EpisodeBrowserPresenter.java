package us.nineworlds.serenity.ui.browser.tv.episodes;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import com.birbit.android.jobqueue.JobManager;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.events.EpisodesRetrievalEvent;
import us.nineworlds.serenity.jobs.EpisodesRetrievalJob;

@InjectViewState
@StateStrategyType(SkipStrategy.class)
public class EpisodeBrowserPresenter extends MvpPresenter<EpisodeBrowserView> {

  EventBus eventBus = EventBus.getDefault();

  @Inject JobManager jobManager;

  public EpisodeBrowserPresenter() {
    super();
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  @Override public void attachView(EpisodeBrowserView view) {
    super.attachView(view);
    eventBus.register(this);
  }

  @Override public void detachView(EpisodeBrowserView view) {
    super.detachView(view);
    eventBus.unregister(this);
  }

  public void retrieveEpisodes(String key) {
    EpisodesRetrievalJob episodesRetrievalJob = new EpisodesRetrievalJob(key);
    jobManager.addJobInBackground(episodesRetrievalJob);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onEpisodeResponse(EpisodesRetrievalEvent event) {
    EpisodeMediaContainer episodes = new EpisodeMediaContainer(event.getMediaContainer());
    List<VideoContentInfo> videos = episodes.createVideos();
    getViewState().updateGallery(videos);
  }
}
