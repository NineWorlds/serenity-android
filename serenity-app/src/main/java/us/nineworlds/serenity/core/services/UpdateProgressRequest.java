package us.nineworlds.serenity.core.services;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;
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
	private String videoId;
	
	public UpdateProgressRequest(long position, String videoId) {
		this.position = position;
		this.videoId = videoId;
	}

	@Override
	protected Void doInBackground(Void... params) {
		PlexappFactory factory = SerenityApplication.getPlexFactory();
		factory.setProgress(videoId, Long.valueOf(position).toString());
		return null;
	}
}