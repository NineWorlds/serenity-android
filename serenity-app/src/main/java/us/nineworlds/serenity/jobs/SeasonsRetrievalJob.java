package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.events.SeasonsRetrievalEvent;

public class SeasonsRetrievalJob extends InjectingJob {

  @Inject SerenityClient client;

  @Inject EventBus eventBus;

  String key;

  public SeasonsRetrievalJob(@NonNull String key) {
    this.key = key;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveSeasons(key);
    SeasonsRetrievalEvent event = new SeasonsRetrievalEvent(mediaContainer);
    eventBus.post(event);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
