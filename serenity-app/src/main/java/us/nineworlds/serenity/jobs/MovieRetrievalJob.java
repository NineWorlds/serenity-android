package us.nineworlds.serenity.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.common.rest.Types;
import us.nineworlds.serenity.events.MovieRetrievalEvent;

public class MovieRetrievalJob extends InjectingJob {

  @Inject SerenityClient client;

  EventBus eventBus = EventBus.getDefault();

  String key;
  String category;

  public MovieRetrievalJob(@NonNull String key, String category) {
    this.key = key;
    this.category = category;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveItemByIdCategory(key, category, Types.MOVIES);
    MovieRetrievalEvent event = new MovieRetrievalEvent(mediaContainer);

    eventBus.post(event);
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
