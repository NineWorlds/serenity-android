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
import us.nineworlds.serenity.events.TVShowRetrievalEvent;

public class TVShowRetrievalJob extends InjectingJob {

  @Inject SerenityClient client;

  @Inject EventBus eventBus;

  String key;
  String category;

  public TVShowRetrievalJob(@NonNull String key, String category) {
    this.key = key;
    this.category = category;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveItemByIdCategory(key, category);
    TVShowRetrievalEvent event = new TVShowRetrievalEvent(mediaContainer, key, category);
    eventBus.post(event);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }

  @VisibleForTesting public String getKey() {
    return key;
  }

  @VisibleForTesting public String getCategory() {
    return category;
  }
}
