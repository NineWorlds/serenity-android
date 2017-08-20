package us.nineworlds.serenity.ui.browser.tv.seasons;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.SeasonsMediaContainer;
import us.nineworlds.serenity.events.EpisodesRetrievalEvent;
import us.nineworlds.serenity.events.SeasonsRetrievalEvent;
import us.nineworlds.serenity.jobs.EpisodesRetrievalJob;
import us.nineworlds.serenity.jobs.SeasonsRetrievalJob;

@InjectViewState
public class TVShowSeasonBrowserPresenter extends MvpPresenter<TVShowSeasonBrowserView> {

  @Inject EventBus eventBus;

  @Inject JobManager jobManager;

  public TVShowSeasonBrowserPresenter() {
    super();
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }

  @Override public void attachView(TVShowSeasonBrowserView view) {
    super.attachView(view);
    eventBus.register(this);
  }

  @Override public void detachView(TVShowSeasonBrowserView view) {
    super.detachView(view);
    eventBus.unregister(this);
  }

  protected void retrieveSeasons(String key) {
    SeasonsRetrievalJob seasonsRetrievalJob = new SeasonsRetrievalJob(key);
    jobManager.addJobInBackground(seasonsRetrievalJob);
  }

  public void retrieveEpisodes(String key) {
    EpisodesRetrievalJob episodesRetrievalJob = new EpisodesRetrievalJob(key);
    jobManager.addJobInBackground(episodesRetrievalJob);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onEpisodeResponse(EpisodesRetrievalEvent event) {
    EpisodeMediaContainer episodes = new EpisodeMediaContainer(event.getMediaContainer());
    List<VideoContentInfo> videos = episodes.createVideos();
    getViewState().updateEpisodes(videos);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onSeasonsRetrievalResponse(SeasonsRetrievalEvent event) {
    List<SeriesContentInfo> seasonList = new SeasonsMediaContainer(event.getMediaContainer()).createSeries();
    getViewState().populateSeasons(seasonList);
  }
}
