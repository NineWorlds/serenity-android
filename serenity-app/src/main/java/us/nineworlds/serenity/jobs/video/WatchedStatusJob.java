package us.nineworlds.serenity.jobs.video;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.rest.SerenityClient;

public class WatchedStatusJob extends InjectingJob {

  @Inject SerenityClient factory;

  private String videoId;

  public WatchedStatusJob(String videoId) {
    super();
    this.videoId = videoId;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    factory.watched(videoId);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
