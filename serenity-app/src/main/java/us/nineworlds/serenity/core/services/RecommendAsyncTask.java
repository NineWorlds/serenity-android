package us.nineworlds.serenity.core.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.app.TaskStackBuilder;
import java.io.IOException;
import java.net.URLEncoder;
import javax.inject.Inject;
import timber.log.Timber;
import toothpick.Toothpick;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.RecommendationBuilder;
import us.nineworlds.serenity.core.SerenityRecommendationContentProvider;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.ui.video.player.RecommendationPlayerActivity;

public class RecommendAsyncTask extends AsyncTask {
  private final VideoContentInfo video;
  private final Context context;

  @Inject protected SerenityClient factory;
  @Inject protected AndroidHelper androidHelper;

  public RecommendAsyncTask(VideoContentInfo video, Context context) {
    this.video = video;
    this.context = context;
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  @Override public Object doInBackground(Object... params) {
    RecommendationBuilder builder = new RecommendationBuilder();
    if (androidHelper.buildNumber() <= Build.VERSION_CODES.N_MR1) {
      try {
        Notification notification = null;
        if (video.getSeriesTitle() == null) {
          notification = buildMovieRecommendation();
        } else {
          notification = buildSeriesRecommendation();
        }
        return notification;
      } catch (NumberFormatException | IOException ex) {
        Timber.e(ex, "Error building recommendation: " + builder.toString());
      }
    }
    return null;
  }

  private Notification buildMovieRecommendation() throws IOException {
    RecommendationBuilder builder = new RecommendationBuilder();
    PendingIntent intent = buildPendingIntent(video);
    String backgroundURL = factory.createImageURL(video.getBackgroundURL(), 1920, 1080);

    float viewedPercentage = video.viewedPercentage();
    int priority = videoPriority(viewedPercentage);
    int color = context.getResources().getColor(us.nineworlds.serenity.R.color.holo_color);
    String summary = video.getSummary();
    if (video.getTagLine() != null) {
      summary = video.getTagLine();
    }

    Notification notification = builder.setContext(context)
        .setBackgroundContentUri(
            SerenityRecommendationContentProvider.CONTENT_URI + URLEncoder.encode(video.getBackgroundURL(), "UTF-8"))
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
    String backgroundURL = factory.createImageURL(video.getBackgroundURL(), 1920, 1080);

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
            SerenityRecommendationContentProvider.CONTENT_URI + URLEncoder.encode(video.getBackgroundURL(), "UTF-8"))
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
