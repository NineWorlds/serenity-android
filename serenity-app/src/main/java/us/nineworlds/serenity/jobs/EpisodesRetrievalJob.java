package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.events.EpisodesRetrievalEvent;
import us.nineworlds.serenity.injection.InjectingJob;

public class EpisodesRetrievalJob extends InjectingJob {

  @Inject PlexappFactory client;

  @Inject EventBus eventBus;

  String key;

  public EpisodesRetrievalJob(@NonNull String key) {
    this.key = key;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    MediaContainer mediaContainer = client.retrieveEpisodes(key);
    EpisodesRetrievalEvent event = new EpisodesRetrievalEvent(mediaContainer);
    eventBus.post(event);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
      int maxRunCount) {
    return null;
  }
}
