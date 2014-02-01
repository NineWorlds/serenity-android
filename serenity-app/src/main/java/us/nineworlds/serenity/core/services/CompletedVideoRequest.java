package us.nineworlds.serenity.core.services;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;
import android.os.AsyncTask;

public class CompletedVideoRequest extends AsyncTask<Void, Void, Void> {

	private String uvideoId;

	public CompletedVideoRequest(String videoId) {
		uvideoId = videoId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(Void... params) {
		PlexappFactory factory = SerenityApplication.getPlexFactory();
		factory.setProgress(uvideoId, "0");
        factory.setWatched(uvideoId);
		return null;
	}

}
