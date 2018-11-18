package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.events.TVCategoryEvent;

public class TVCategoryJob extends InjectingJob {

  EventBus eventBus = EventBus.getDefault();

  @Inject SerenityClient client;

  String key;

  public TVCategoryJob(String key) {
    this.key = key;
  }

  @Override public void onAdded() {

  }

  @VisibleForTesting public String getKey() {
    return key;
  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveSeriesCategoryById(key);
    eventBus.post(new TVCategoryEvent(mediaContainer, key));
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
