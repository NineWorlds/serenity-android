package us.nineworlds.serenity.jobs.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.rest.SerenityClient;

public class StopPlaybackJob extends InjectingJob {

  @Inject SerenityClient serenityClient;

  private String videoId;

  public StopPlaybackJob(String videoId) {
    super();
    this.videoId = videoId;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    serenityClient.stopPlaying(videoId);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
