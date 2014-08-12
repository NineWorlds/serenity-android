package us.nineworlds.serenity.core.services;

import java.io.IOException;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.RecommendationBuilder;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.ui.video.player.RecommendationPlayerActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class RecommendAsyncTask extends AsyncTask {
	private final VideoContentInfo video;
	private final Context context;
	private final PlexappFactory factory = SerenityApplication.getPlexFactory();

	public RecommendAsyncTask(VideoContentInfo video, Context context) {
		this.video = video;
		this.context = context;
	}

	@Override
	protected Object doInBackground(Object... params) {
		RecommendationBuilder builder = new RecommendationBuilder();
		try {
			PendingIntent intent = buildPendingIntent(video);
			String backgroundURL = factory.getImageURL(
					video.getBackgroundURL(), 1920, 1080);

			int priority = 0;
			if (video.viewedPercentage() > 0.0f) {
				priority = Math.round(100 * video.viewedPercentage());
			}

			Notification notification = builder.setContext(context)
					.setBackground(backgroundURL).setTitle(video.getTitle())
					.setImage(video.getImageURL())
					.setId(Integer.parseInt(video.id())).setPriority(priority)
					.setDescription(video.getSummary())
					.setSmallIcon(R.drawable.androidtv_icon_mono)
					.setIntent(intent).build();
			return notification;
		} catch (IOException ex) {
			Log.e("OnDeckRecommendation", "Error building recommendation: "
					+ builder.toString(), ex);
		}
		return null;
	}

	public PendingIntent buildPendingIntent(VideoContentInfo video) {
		Intent intent = new Intent(context, RecommendationPlayerActivity.class);
		if (video instanceof MoviePosterInfo) {
			intent.putExtra("serenity_video", (MoviePosterInfo) video);
		}

		if (video instanceof EpisodePosterInfo) {
			intent.putExtra("serenity_video", (EpisodePosterInfo) video);
		}

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(intent);

		// Ensure a unique PendingIntents, otherwise all recommendations end
		// up with the same
		// PendingIntent
		intent.setAction(video.id());

		PendingIntent pintent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return pintent;
	}
}
