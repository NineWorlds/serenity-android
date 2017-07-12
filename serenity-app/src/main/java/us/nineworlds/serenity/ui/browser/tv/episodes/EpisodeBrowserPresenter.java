package us.nineworlds.serenity.ui.browser.tv.episodes;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import net.ganin.darv.DpadAwareRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.events.EpisodesRetrievalEvent;
import us.nineworlds.serenity.injection.SerenityObjectGraph;

@InjectViewState
public class EpisodeBrowserPresenter extends MvpPresenter<EpisodeBrowserView> {

    @Inject
    EventBus eventBus;

    public EpisodeBrowserPresenter() {
        super();
        SerenityObjectGraph.getInstance().inject(this);
    }

    @Override
    public void attachView(EpisodeBrowserView view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(EpisodeBrowserView view) {
        super.detachView(view);
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEpisodeResponse(EpisodesRetrievalEvent event) {
        EpisodeMediaContainer episodes = new EpisodeMediaContainer(event.getMediaContainer());
        List<VideoContentInfo> videos = episodes.createVideos();
        getViewState().updateGallery(videos);
    }


}
