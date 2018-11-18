package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.events.SubtitleEvent;

public class SubtitleJob extends InjectingJob {

  EventBus eventBus = EventBus.getDefault();

  @Inject SerenityClient client;

  String metaDataKey;

  public SubtitleJob(String metaDataKey) {
    this.metaDataKey = metaDataKey;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveMovieMetaData(metaDataKey);
    eventBus.post(new SubtitleEvent(mediaContainer));
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
