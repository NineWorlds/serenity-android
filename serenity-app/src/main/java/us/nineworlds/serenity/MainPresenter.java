package us.nineworlds.serenity;

import android.content.Context;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import com.birbit.android.jobqueue.JobManager;
import javax.inject.Inject;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.jobs.GlideClearCacheJob;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

  @Inject JobManager jobManager;

  @Inject
  SerenityClient client;

  public MainPresenter() {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  public void clearCache(Context context) {
    jobManager.addJobInBackground(new GlideClearCacheJob(context));
  }

  public void showOrHideUserSelection() {
    if (client.supportsMultipleUsers()) {
      getViewState().showMultipleUsersOption();
    } else {
      getViewState().hideMultipleUsersOption();
    }
  }

}
