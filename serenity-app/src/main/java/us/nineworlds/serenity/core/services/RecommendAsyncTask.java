package us.nineworlds.serenity.core.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import java.io.IOException;
import java.net.URLEncoder;
import javax.inject.Inject;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.RecommendationBuilder;
import us.nineworlds.serenity.core.SerenityRecommendationContentProvider;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.injection.SerenityObjectGraph;
import us.nineworlds.serenity.ui.video.player.RecommendationPlayerActivity;

//AndroidTVCodeMash2015-Recommendations

@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public class RecommendAsyncTask extends AsyncTask {
  private final VideoContentInfo video;
  private final Context context;

  @Inject protected PlexappFactory factory;

  public RecommendAsyncTask(VideoContentInfo video, Context context) {
    this.video = video;
    this.context = context;
    SerenityObjectGraph.getInstance().inject(this);
  }

  @Override public Object doInBackground(Object... params) {
    RecommendationBuilder builder = new RecommendationBuilder();
    try {
      Notification notification = null;
      if (video.getSeriesTitle() == null) {
        notification = buildMovieRecommendation();
      } else {
        notification = buildSeriesRecommendation();
      }
      return notification;
    } catch (IOException ex) {
      Log.e("OnDeckRecommendation", "Error building recommendation: " + builder.toString(), ex);
    }
    return null;
  }

  private Notification buildMovieRecommendation() throws IOException {
    RecommendationBuilder builder = new RecommendationBuilder();
    PendingIntent intent = buildPendingIntent(video);
    String backgroundURL = factory.getImageURL(video.getBackgroundURL(), 1920, 1080);

    float viewedPercentage = video.viewedPercentage();
    int priority = videoPriority(viewedPercentage);
    int color = context.getResources().getColor(us.nineworlds.serenity.R.color.holo_color);
    String summary = video.getSummary();
    if (video.getTagLine() != null) {
      summary = video.getTagLine();
    }

    Notification notification = builder.setContext(context)
        .setBackgroundContentUri(
            SerenityRecommendationContentProvider.CONTENT_URI + URLEncoder.encode(
                video.getBackgroundURL(), "UTF-8"))
        .setBackground(backgroundURL)
        .setTitle(video.getTitle())
        .setImage(video.getImageURL())
        .setId(Integer.parseInt(video.id()))
        .setPriority(priority)
        .setDescription(summary)
        .setColor(color)
        .setSmallIcon(R.drawable.androidtv_icon_mono)
        .setIntent(intent)
        .build();
    return notification;
  }

  private Notification buildSeriesRecommendation() throws IOException {
    RecommendationBuilder builder = new RecommendationBuilder();
    PendingIntent intent = buildPendingIntent(video);
    String backgroundURL = factory.getImageURL(video.getBackgroundURL(), 1920, 1080);

    float viewedPercentage = video.viewedPercentage();
    int priority = videoPriority(viewedPercentage);
    int color = context.getResources().getColor(us.nineworlds.serenity.R.color.holo_color);
    String title = video.getSeriesTitle();

    String season = video.getSeason();
    String episode = video.getEpisode();

    String summary = video.getTitle();
    if (season != null || episode != null) {
      summary = summary + " - ";
    }

    if (season != null) {
      summary = summary + season + " ";
    }

    if (episode != null) {
      summary = summary + episode;
    }
    summary = summary.trim();

    Notification notification = builder.setContext(context)
        .setBackground(backgroundURL)
        .setBackgroundContentUri(
            SerenityRecommendationContentProvider.CONTENT_URI + URLEncoder.encode(
                video.getBackgroundURL(), "UTF-8"))
        .setTitle(title)
        .setImage(video.getImageURL())
        .setId(Integer.parseInt(video.id()))
        .setPriority(priority)
        .setDescription(summary)
        .setColor(color)
        .setSmallIcon(R.drawable.androidtv_icon_mono)
        .setIntent(intent)
        .build();
    return notification;
  }

  protected int videoPriority(float viewedPercentage) {
    if (viewedPercentage > 0.80) {
      return Notification.PRIORITY_MAX;
    }

    if (viewedPercentage > 0.40) {
      return Notification.PRIORITY_HIGH;
    }

    if (viewedPercentage > 0.10) {
      return Notification.PRIORITY_DEFAULT;
    }

    return Notification.PRIORITY_LOW;
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

    PendingIntent pintent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    return pintent;
  }
}
