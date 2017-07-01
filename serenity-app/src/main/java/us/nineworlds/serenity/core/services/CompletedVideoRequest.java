package us.nineworlds.serenity.core.services;

import android.os.AsyncTask;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.injection.SerenityObjectGraph;

public class CompletedVideoRequest extends AsyncTask<Void, Void, Void> {

    @Inject
    PlexappFactory factory;

    private final String uvideoId;

    public CompletedVideoRequest(String videoId) {
        uvideoId = videoId;
        SerenityObjectGraph.getInstance().inject(this);
    }

    @Override
    protected Void doInBackground(Void... params) {
        factory.setProgress(uvideoId, "0");
        factory.setWatched(uvideoId);
        return null;
    }

}
