package us.nineworlds.serenity.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.events.MainCategoryEvent;

public class MovieCategoryJob extends InjectingJob {

  EventBus eventBus = EventBus.getDefault();

  @Inject SerenityClient client;

  String key;

  public MovieCategoryJob(String key) {
    this.key = key;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveCategoriesById(key);
    eventBus.post(new MainCategoryEvent(mediaContainer, key));
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
