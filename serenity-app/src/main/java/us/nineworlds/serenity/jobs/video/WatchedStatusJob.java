package us.nineworlds.serenity.jobs.video;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.common.android.injection.InjectingJob;

public class WatchedStatusJob extends InjectingJob {

  @Inject PlexappFactory factory;

  private String videoId;

  public WatchedStatusJob(String videoId) {
    super();
    this.videoId = videoId;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    factory.setWatched(videoId);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
