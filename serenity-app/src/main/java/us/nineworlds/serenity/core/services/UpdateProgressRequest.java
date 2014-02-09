package us.nineworlds.serenity.core.services;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.os.AsyncTask;

/**
 * A task that updates the progress position of a video while it is being
 * played.
 * 
 * @author dcarver
 * 
 */
public class UpdateProgressRequest extends AsyncTask<Void, Void, Void> {
	
	private long position;
	private VideoContentInfo video;
	
	public UpdateProgressRequest(long position, VideoContentInfo video) {
		this.position = position;
		this.video = video;
	}

	@Override
	protected Void doInBackground(Void... params) {
		PlexappFactory factory = SerenityApplication.getPlexFactory();
        final String id = video.id();
        if (video.isWatched()) {
            factory.setWatched(id);
            factory.setProgress("0", id);
        } else {
            factory.setProgress(id, Long.valueOf(position).toString());
        }
		return null;
	}
}