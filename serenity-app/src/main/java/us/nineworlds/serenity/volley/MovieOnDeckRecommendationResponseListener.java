package us.nineworlds.serenity.volley;

import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.core.services.RecommendAsyncTask;
import android.content.Context;

import com.android.volley.Response;

//AndroidTVCodeMash2015-Recommendations
public class MovieOnDeckRecommendationResponseListener implements
		Response.Listener<MediaContainer> {

	Context context;

	public MovieOnDeckRecommendationResponseListener(Context context) {
		this.context = context;
	}

	@Override
	public void onResponse(MediaContainer mc) {
		MovieMediaContainer movieContainer = new MovieMediaContainer(mc);
		List<VideoContentInfo> movies = movieContainer.createVideos();
		for (VideoContentInfo movie : movies) {
			new RecommendAsyncTask(movie, context).execute();
		}
	}
}
