package us.nineworlds.serenity.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.events.TVCategoryEvent;
import us.nineworlds.serenity.injection.InjectingJob;

public class TVCategoryJob extends InjectingJob {

    @Inject
    EventBus eventBus;

    @Inject
    PlexappFactory client;

    String key;

    public TVCategoryJob(String key) {
        this.key = key;
    }

    @Override
    public void onAdded() {

    }

    @VisibleForTesting
    public String getKey() {
        return key;
    }

    @Override
    public void onRun() throws Throwable {
        MediaContainer mediaContainer = client.retrieveSections(key);
        eventBus.post(new TVCategoryEvent(mediaContainer, key));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
