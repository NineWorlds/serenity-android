package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.events.SeasonsRetrievalEvent;
import us.nineworlds.serenity.events.TVShowRetrievalEvent;
import us.nineworlds.serenity.injection.InjectingJob;

public class SeasonsRetrievalJob extends InjectingJob {

    @Inject
    PlexappFactory client;

    @Inject
    EventBus eventBus;

    String key;

    public SeasonsRetrievalJob(@NonNull String key) {
        this.key = key;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        MediaContainer mediaContainer = client.retrieveSeasons(key);
        SeasonsRetrievalEvent event = new SeasonsRetrievalEvent(mediaContainer);
        eventBus.post(event);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
