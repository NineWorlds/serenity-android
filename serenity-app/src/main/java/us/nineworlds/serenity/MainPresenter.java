package us.nineworlds.serenity;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import us.nineworlds.serenity.injection.SerenityObjectGraph;
import us.nineworlds.serenity.jobs.GDMServerJob;
import us.nineworlds.serenity.jobs.GlideClearCacheJob;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    @Inject
    JobManager jobManager;

    @Inject
    EventBus eventBus;

    public MainPresenter() {
        SerenityObjectGraph.getInstance().inject(this);
    }

    public void discoverServers() {
        jobManager.addJobInBackground(new GDMServerJob());
    }

    public void clearCache(Context context) {
        jobManager.addJobInBackground(new GlideClearCacheJob(context));
    }



}
