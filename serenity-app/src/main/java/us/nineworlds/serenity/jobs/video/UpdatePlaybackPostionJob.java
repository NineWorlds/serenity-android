package us.nineworlds.serenity.jobs.video;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public class UpdatePlaybackPostionJob extends InjectingJob {

  @Inject PlexappFactory factory;

  private VideoContentInfo video;

  public UpdatePlaybackPostionJob(VideoContentInfo video) {
    super();
    this.video = video;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    String videoId = video.id();
    if (video.isWatched()) {
      factory.setWatched(videoId);
      factory.setProgress(videoId, "0");
    } else {
      factory.setProgress(videoId, Long.valueOf(video.getResumeOffset()).toString());
    }
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
