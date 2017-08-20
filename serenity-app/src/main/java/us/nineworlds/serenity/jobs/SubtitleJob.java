package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.events.SubtitleEvent;

public class SubtitleJob extends InjectingJob {

  @Inject EventBus eventBus;

  @Inject PlexappFactory client;

  String metaDataKey;

  public SubtitleJob(String metaDataKey) {
    this.metaDataKey = metaDataKey;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    MediaContainer mediaContainer = client.retrieveMovieMetaData(metaDataKey);
    eventBus.post(new SubtitleEvent(mediaContainer));
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
