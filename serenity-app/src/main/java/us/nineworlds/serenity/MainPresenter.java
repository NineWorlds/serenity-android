package us.nineworlds.serenity;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;
import javax.inject.Inject;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.jobs.GlideClearCacheJob;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

  @Inject JobManager jobManager;

  public MainPresenter() {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  public void clearCache(Context context) {
    jobManager.addJobInBackground(new GlideClearCacheJob(context));
  }
}
