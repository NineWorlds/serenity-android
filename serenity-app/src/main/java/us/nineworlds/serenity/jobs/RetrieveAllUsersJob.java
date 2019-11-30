package us.nineworlds.serenity.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.RetryConstraint;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.common.rest.SerenityUser;
import us.nineworlds.serenity.events.users.AllUsersEvent;

public class RetrieveAllUsersJob extends InjectingJob {

  @Inject SerenityClient client;
  EventBus eventBus = EventBus.getDefault();

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    List<SerenityUser> serenityUsers = client.allAvailableUsers();
    eventBus.post(new AllUsersEvent(serenityUsers));
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
