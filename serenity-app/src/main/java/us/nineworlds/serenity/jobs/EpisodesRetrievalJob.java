package us.nineworlds.serenity.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.events.EpisodesRetrievalEvent;

public class EpisodesRetrievalJob extends InjectingJob {

  @Inject SerenityClient client;

  EventBus eventBus = EventBus.getDefault();

  @Inject Logger logger;

  String key;

  public EpisodesRetrievalJob(@NonNull String key) {
    this.key = key;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveEpisodes(key);
    EpisodesRetrievalEvent event = new EpisodesRetrievalEvent(mediaContainer);
    eventBus.post(event);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    logger.error("Episode Retrieval Error: ", throwable);
    return null;
  }
}
