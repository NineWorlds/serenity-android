package us.nineworlds.serenity;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;
import java.util.Map;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;
import us.nineworlds.serenity.emby.server.EmbyServer;
import us.nineworlds.serenity.emby.server.EmbyServerJob;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.jobs.GDMServerJob;
import us.nineworlds.serenity.jobs.GlideClearCacheJob;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

  @Inject JobManager jobManager;

  public MainPresenter() {
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }

  public void clearCache(Context context) {
    jobManager.addJobInBackground(new GlideClearCacheJob(context));
  }

}
