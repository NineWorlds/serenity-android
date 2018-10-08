/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.injection.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.AndroidTV;
import us.nineworlds.serenity.GalleryOnItemSelectedListener;
import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.MainMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.MainMenuTextViewAdapter;
import us.nineworlds.serenity.MainPresenter;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.StartupBroadcastReceiver;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.OkHttpStack;
import us.nineworlds.serenity.core.OnDeckRecommendations;
import us.nineworlds.serenity.core.RecommendationBuilder;
import us.nineworlds.serenity.core.SerenityRecommendationContentProvider;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.model.impl.CategoryMediaContainer;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.core.model.impl.SeasonsMediaContainer;
import us.nineworlds.serenity.core.model.impl.SecondaryCategoryMediaContainer;
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer;
import us.nineworlds.serenity.core.model.impl.SubtitleMediaContainer;
import us.nineworlds.serenity.core.model.impl.TVCategoryMediaContainer;
import us.nineworlds.serenity.core.services.CompletedVideoRequest;
import us.nineworlds.serenity.core.services.MovieSearchIntentService;
import us.nineworlds.serenity.core.services.MoviesRetrievalIntentService;
import us.nineworlds.serenity.core.services.OnDeckRecommendationIntentService;
import us.nineworlds.serenity.core.services.RecommendAsyncTask;
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask;
import us.nineworlds.serenity.core.services.UpdateProgressRequest;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.core.util.StringPreference;
import us.nineworlds.serenity.emby.server.EmbyServerJob;
import us.nineworlds.serenity.emby.server.api.EmbyAPIClient;
import us.nineworlds.serenity.events.ErrorMainMenuEvent;
import us.nineworlds.serenity.events.MainMenuEvent;
import us.nineworlds.serenity.fragments.EpisodeVideoGalleryFragment;
import us.nineworlds.serenity.fragments.MainMenuFragment;
import us.nineworlds.serenity.fragments.MovieSearchGalleryFragment;
import us.nineworlds.serenity.fragments.MovieVideoGalleryFragment;
import us.nineworlds.serenity.fragments.VideoGridFragment;
import us.nineworlds.serenity.handlers.AutoConfigureHandlerRunnable;
import us.nineworlds.serenity.injection.ServerClientPreference;
import us.nineworlds.serenity.injection.VideoPlayerHandler;
import us.nineworlds.serenity.jobs.AuthenticateUserJob;
import us.nineworlds.serenity.jobs.EpisodesRetrievalJob;
import us.nineworlds.serenity.jobs.GDMServerJob;
import us.nineworlds.serenity.jobs.MainMenuRetrievalJob;
import us.nineworlds.serenity.jobs.MovieCategoryJob;
import us.nineworlds.serenity.jobs.MovieRetrievalJob;
import us.nineworlds.serenity.jobs.MovieSecondaryCategoryJob;
import us.nineworlds.serenity.jobs.OnDeckRecommendationsJob;
import us.nineworlds.serenity.jobs.RetrieveAllUsersJob;
import us.nineworlds.serenity.jobs.SeasonsRetrievalJob;
import us.nineworlds.serenity.jobs.SubtitleJob;
import us.nineworlds.serenity.jobs.TVCategoryJob;
import us.nineworlds.serenity.jobs.TVCategorySecondaryJob;
import us.nineworlds.serenity.jobs.TVShowRetrievalJob;
import us.nineworlds.serenity.jobs.video.StartPlaybackJob;
import us.nineworlds.serenity.jobs.video.StopPlaybackJob;
import us.nineworlds.serenity.jobs.video.UpdatePlaybackPostionJob;
import us.nineworlds.serenity.jobs.video.WatchedStatusJob;
import us.nineworlds.serenity.server.GDMReceiver;
import us.nineworlds.serenity.ui.activity.ServerSelectionActivity;
import us.nineworlds.serenity.ui.activity.login.LoginUserActivity;
import us.nineworlds.serenity.ui.activity.login.LoginUserPresenter;
import us.nineworlds.serenity.ui.activity.login.LoginUserViewHolder;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserPresenter;
import us.nineworlds.serenity.ui.browser.movie.MovieCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieGridPosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterImageAdapter;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.SecondaryCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.FindUnwatchedAsyncTask;
import us.nineworlds.serenity.ui.browser.tv.OnKeyDownDelegate;
import us.nineworlds.serenity.ui.browser.tv.TVCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVSecondaryCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserGalleryOnItemClickListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserPresenter;
import us.nineworlds.serenity.ui.browser.tv.TVShowGalleryOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowGridOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowGridOnKeyListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.TVShowRecyclerAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserOnLongClickListener;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserPresenter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.SeasonOnItemLongClickListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.SeasonsEpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonBrowserPresenter;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonOnItemClickListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonOnItemSelectedListener;
import us.nineworlds.serenity.ui.leanback.search.CardPresenter;
import us.nineworlds.serenity.ui.leanback.search.MovieSearchFragment;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.listeners.SubtitleSpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.preferences.LeanbackSettingsActivity;
import us.nineworlds.serenity.ui.preferences.SettingsFragment;
import us.nineworlds.serenity.ui.search.SearchAdapter;
import us.nineworlds.serenity.ui.search.SearchableActivity;
import us.nineworlds.serenity.ui.util.ExternalPlayerResultHandler;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.PlayerResultHandler;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.util.VideoQueueHelper;
import us.nineworlds.serenity.ui.video.player.EventLogger;
import us.nineworlds.serenity.ui.video.player.ExoplayerPresenter;
import us.nineworlds.serenity.ui.video.player.ExoplayerVideoActivity;
import us.nineworlds.serenity.ui.video.player.MediaController;
import us.nineworlds.serenity.ui.video.player.RecommendationPlayerActivity;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.ui.video.player.VideoKeyCodeHandlerDelegate;
import us.nineworlds.serenity.ui.video.player.VideoPlayerKeyCodeHandler;
import us.nineworlds.serenity.ui.video.player.VideoPlayerPrepareListener;

@Module(includes = {SerenityModule.class, VideoModule.class}, injects = {
    GDMReceiver.class, MainMenuFragment.class, OnDeckRecommendations.class,
    MediaController.class, MainActivity.class, SerenityApplication.class, StartupBroadcastReceiver.class,
    RecommendationPlayerActivity.class, SearchableActivity.class,
    MovieBrowserActivity.class, TVShowBrowserActivity.class, TVShowSeasonBrowserActivity.class,
    AutoConfigureHandlerRunnable.class, EpisodeMenuDrawerOnItemClickedListener.class, ImageInfographicUtils.class,
    MainMenuDrawerOnItemClickedListener.class, MovieGridPosterOnItemSelectedListener.class,
    MovieMenuDrawerOnItemClickedListener.class, PlayerResultHandler.class, ExternalPlayerResultHandler.class,
    SeasonOnItemLongClickListener.class, MovieMediaContainer.class,
    SeriesMediaContainer.class, SubtitleMediaContainer.class, EpisodeMediaContainer.class, SeasonsMediaContainer.class,
    GalleryVideoOnItemClickListener.class, GalleryVideoOnItemLongClickListener.class,
    EpisodeBrowserOnLongClickListener.class, EpisodePosterOnItemSelectedListener.class,
    MoviePosterOnItemSelectedListener.class, TVShowGalleryOnItemSelectedListener.class,
    TVShowGridOnItemSelectedListener.class, TVShowGridOnKeyListener.class, TVShowMenuDrawerOnItemClickedListener.class,
    TVShowSeasonMenuDrawerOnItemClickedListener.class, TVShowSeasonOnItemSelectedListener.class,
    VideoPlayerIntentUtils.class, VideoPlayerPrepareListener.class, MoviesRetrievalIntentService.class,
    MovieSearchIntentService.class, MainMenuTextViewAdapter.class, EpisodePosterImageGalleryAdapter.class,
    SeasonsEpisodePosterImageGalleryAdapter.class, MoviePosterImageAdapter.class, SearchAdapter.class,
    TVShowRecyclerAdapter.class, MenuDrawerAdapter.class, TVShowSeasonImageGalleryAdapter.class,
    OnDeckRecommendationIntentService.class, CompletedVideoRequest.class, FindUnwatchedAsyncTask.class,
    UnWatchVideoAsyncTask.class, UpdateProgressRequest.class, WatchedVideoAsyncTask.class,
    GalleryOnItemSelectedListener.class, AndroidTV.class, RecommendAsyncTask.class,
    RecommendationBuilder.class, TVShowPosterImageGalleryAdapter.class, VideoPlayerKeyCodeHandler.class,
    SerenitySurfaceViewVideoActivity.class, OkHttpStack.class, SerenityRecommendationContentProvider.class,
    AndroidHelper.class, SecondaryCategorySpinnerOnItemSelectedListener.class,
    MovieCategorySpinnerOnItemSelectedListener.class, TVCategorySpinnerOnItemSelectedListener.class,
    TVSecondaryCategorySpinnerOnItemSelectedListener.class, MovieVideoGalleryFragment.class, VideoGridFragment.class,
    EpisodeBrowserActivity.class, EpisodeVideoGalleryFragment.class, MenuMediaContainer.class,
    MovieSearchGalleryFragment.class, CardPresenter.class, MovieSearchFragment.class, CategoryMediaContainer.class,
    SecondaryCategoryMediaContainer.class, TVCategoryMediaContainer.class, ServerConfig.class,
    SubtitleSpinnerOnItemSelectedListener.class, VideoQueueHelper.class,
    us.nineworlds.serenity.ui.browser.tv.seasons.EpisodePosterOnItemClickListener.class,
    MainMenuEvent.class, OnDeckRecommendationsJob.class, EpisodesRetrievalJob.class, MainMenuRetrievalJob.class,
    MovieCategoryJob.class, MovieRetrievalJob.class, MovieSecondaryCategoryJob.class, SeasonsRetrievalJob.class,
    SubtitleJob.class, TVCategoryJob.class, TVCategorySecondaryJob.class, TVShowRetrievalJob.class, GDMServerJob.class,
    TVShowBrowserPresenter.class, MovieBrowserPresenter.class, EpisodeBrowserPresenter.class,
    TVShowSeasonBrowserPresenter.class, MainPresenter.class, OnKeyDownDelegate.class, ErrorMainMenuEvent.class,
    ExoplayerVideoActivity.class, ExoplayerPresenter.class, EventLogger.class, VideoKeyCodeHandlerDelegate.class,
    UpdatePlaybackPostionJob.class, WatchedStatusJob.class, EmbyServerJob.class, ServerSelectionActivity.class,
    LoginUserActivity.class, LoginUserPresenter.class, LoginUserViewHolder.class, RetrieveAllUsersJob.class,
    AuthenticateUserJob.class, StartPlaybackJob.class, StopPlaybackJob.class,
    TVShowBrowserGalleryOnItemClickListener.class,
    TVShowSeasonOnItemClickListener.class, LeanbackSettingsActivity.class, SettingsFragment.PrefsFragment.class
}, library = true)
public class AndroidModule {

  private final Context applicationContext;
  private SerenityClient embyAPIClient;
  private SerenityClient plexClient;

  public AndroidModule(Application application) {
    this.applicationContext = application;
  }

  @Provides SerenityClient providesSerenityClient(@ServerClientPreference StringPreference serverClientPreference) {
    if (plexClient == null) {
      IConfiguration serverConfig = ServerConfig.getInstance(applicationContext);
      plexClient = PlexappFactory.getInstance(serverConfig);
    }
    if (embyAPIClient == null) {
      embyAPIClient = new EmbyAPIClient(applicationContext, "http://localhost:8096/");
    }

    if ("Emby".equals(serverClientPreference.get())) {
      return embyAPIClient;
    }

    return plexClient;
  }

  @Provides @ApplicationContext Context providesApplicationContext() {
    return applicationContext;
  }

  @Provides @Singleton SharedPreferences providesSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(applicationContext);
  }

  @Provides @Singleton AndroidHelper providesAndroidHelper() {
    return new AndroidHelper();
  }

  @Provides @Singleton Resources providesResources() {
    return applicationContext.getResources();
  }

  @Provides MediaPlayer providesMediaPlayer() {
    return new MediaPlayer();
  }

  @Provides @Singleton JobManager providesJobManager() {
    Configuration configuration = new Configuration.Builder(applicationContext).minConsumerCount(1)
        .maxConsumerCount(5)
        .loadFactor(3)
        .consumerKeepAlive(120)
        .build();
    return new JobManager(configuration);
  }

  @Provides @Singleton EventBus providesEventBus() {
    return EventBus.getDefault();
  }

  @Provides @Singleton LocalBroadcastManager providesLocalBroadcastManager() {
    return LocalBroadcastManager.getInstance(applicationContext);
  }

  @Provides DataSource.Factory providesMediaDataSource(@ApplicationContext Context context,
      DefaultBandwidthMeter bandwidthMeter, HttpDataSource.Factory httpDataSourceFactory) {
    return new DefaultDataSourceFactory(context, bandwidthMeter, httpDataSourceFactory);
  }

  @Provides @VideoPlayerHandler Handler providesVideoPlayerHandler() {
    return new Handler();
  }

  @Provides @Singleton @ServerClientPreference StringPreference providesServerClientPrefence(
      SharedPreferences sharedPreferences) {
    return new StringPreference(sharedPreferences, "server_client", "");
  }
}
