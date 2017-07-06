package us.nineworlds.serenity.ui.browser.tv;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;

import net.ganin.darv.DpadAwareRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer;
import us.nineworlds.serenity.events.TVCategoryEvent;
import us.nineworlds.serenity.events.TVShowRetrievalEvent;
import us.nineworlds.serenity.injection.SerenityObjectGraph;
import us.nineworlds.serenity.jobs.TVCategoryJob;

@InjectViewState
public class TVShowBrowserPresenter extends MvpPresenter<TVShowBrowserView> {

    @Inject
    EventBus eventBus;

    @Inject
    JobManager jobManager;

    public TVShowBrowserPresenter() {
        super();
        SerenityObjectGraph.getInstance().inject(this);
    }


    @Override
    public void attachView(TVShowBrowserView view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(TVShowBrowserView view) {
        super.detachView(view);
        eventBus.unregister(this);
    }

    public void fetchTVCategories(String key) {
        jobManager.addJobInBackground(new TVCategoryJob(key));
    }

    public String getKey(Intent intent) {
        return intent.getExtras().getString("key");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTVCategoryResponse(TVCategoryEvent event) {
        getViewState().updateCategories(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTVShowResponse(TVShowRetrievalEvent event) {
        List<SeriesContentInfo> tvShowList = new SeriesMediaContainer(event.getMediaContainer()).createSeries();
        getViewState().displayShows(tvShowList, event.getCategory());
    }


}
