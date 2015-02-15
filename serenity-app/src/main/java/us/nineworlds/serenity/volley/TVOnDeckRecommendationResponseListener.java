package us.nineworlds.serenity.volley;

import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.services.RecommendAsyncTask;
import android.content.Context;

import com.android.volley.Response;

public class TVOnDeckRecommendationResponseListener implements
Response.Listener<MediaContainer> {

	Context context;

	public TVOnDeckRecommendationResponseListener(Context context) {
		this.context = context;
	}

	@Override
	public void onResponse(MediaContainer mc) {
		EpisodeMediaContainer episodeContainer = new EpisodeMediaContainer(mc);
		List<VideoContentInfo> episodes = episodeContainer.createVideos();
		for (VideoContentInfo episode : episodes) {
			new RecommendAsyncTask(episode, context).execute();
		}
	}
}
