package us.nineworlds.serenity.core.services;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.SerenityObjectGraph;
import android.os.AsyncTask;

public class UpdateProgressRequest extends AsyncTask<Void, Void, Void> {

	private final long position;
	private final VideoContentInfo video;

	@Inject
	PlexappFactory factory;

	public UpdateProgressRequest(long position, VideoContentInfo video) {
		this.position = position;
		this.video = video;
		SerenityObjectGraph.getInstance().inject(this);
	}

	@Override
	protected Void doInBackground(Void... params) {
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