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
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.injection.modules;

import javax.inject.Singleton;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.AndroidTV;
import us.nineworlds.serenity.GDMReceiver;
import us.nineworlds.serenity.GalleryOnItemSelectedListener;
import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.MainMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.MainMenuTextViewAdapter;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.StartupBroadcastReceiver;
import us.nineworlds.serenity.core.OkHttpStack;
import us.nineworlds.serenity.core.OnDeckRecommendations;
import us.nineworlds.serenity.core.RecommendationBuilder;
import us.nineworlds.serenity.core.SerenityRecommendationContentProvider;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.imageloader.OKHttpImageLoader;
import us.nineworlds.serenity.core.imageloader.SerenityImageLoader;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.core.model.impl.SeasonsMediaContainer;
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer;
import us.nineworlds.serenity.core.model.impl.SubtitleMediaContainer;
import us.nineworlds.serenity.core.services.CategoryRetrievalIntentService;
import us.nineworlds.serenity.core.services.CompletedVideoRequest;
import us.nineworlds.serenity.core.services.MovieSearchIntentService;
import us.nineworlds.serenity.core.services.MoviesRetrievalIntentService;
import us.nineworlds.serenity.core.services.MusicAlbumRetrievalIntentService;
import us.nineworlds.serenity.core.services.MusicRetrievalIntentService;
import us.nineworlds.serenity.core.services.OnDeckRecommendationIntentService;
import us.nineworlds.serenity.core.services.RecommendAsyncTask;
import us.nineworlds.serenity.core.services.SecondaryCategoryRetrievalIntentService;
import us.nineworlds.serenity.core.services.TVShowCategoryRetrievalIntentService;
import us.nineworlds.serenity.core.services.UnWatchVideoAsyncTask;
import us.nineworlds.serenity.core.services.UpdateProgressRequest;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.fragments.EpisodeVideoGalleryFragment;
import us.nineworlds.serenity.fragments.MovieSearchGalleryFragment;
import us.nineworlds.serenity.fragments.MovieVideoGalleryFragment;
import us.nineworlds.serenity.fragments.VideoGridFragment;
import us.nineworlds.serenity.handlers.AutoConfigureHandlerRunnable;
import us.nineworlds.serenity.injection.ApplicationContext;
import us.nineworlds.serenity.ui.adapters.MenuDrawerAdapter;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieGridPosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.MovieMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterImageAdapter;
import us.nineworlds.serenity.ui.browser.movie.MoviePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.movie.SecondaryCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.music.MusicPosterGalleryAdapter;
import us.nineworlds.serenity.ui.browser.music.MusicPosterGalleryOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.music.MusicPosterGridViewAdapter;
import us.nineworlds.serenity.ui.browser.music.albums.MusicAlbumsCoverAdapter;
import us.nineworlds.serenity.ui.browser.music.tracks.MusicTracksActivity;
import us.nineworlds.serenity.ui.browser.music.tracks.TracksAdapter;
import us.nineworlds.serenity.ui.browser.music.tracks.TracksOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.FindUnwatchedAsyncTask;
import us.nineworlds.serenity.ui.browser.tv.TVCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVSecondaryCategorySpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowBannerImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.TVShowGalleryOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowGridOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowGridOnKeyListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.tv.TVShowPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeBrowserOnLongClickListener;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.SeasonOnItemLongClickListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.SeasonsEpisodePosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonImageGalleryAdapter;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.ui.browser.tv.seasons.TVShowSeasonOnItemSelectedListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.preferences.SerenityPreferenceActivity;
import us.nineworlds.serenity.ui.search.SearchAdapter;
import us.nineworlds.serenity.ui.search.SearchableActivity;
import us.nineworlds.serenity.ui.util.ExternalPlayerResultHandler;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.PlayerResultHandler;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.video.player.MediaController;
import us.nineworlds.serenity.ui.video.player.RecommendationPlayerActivity;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import us.nineworlds.serenity.ui.video.player.VideoPlayerKeyCodeHandler;
import us.nineworlds.serenity.ui.video.player.VideoPlayerPrepareListener;
import us.nineworlds.serenity.volley.LibraryResponseListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;

@Module(includes = SerenityModule.class, injects = { GDMReceiver.class,
		OnDeckRecommendations.class, MusicTracksActivity.class,
		GalleryVideoOnItemLongClickListener.class,
		GridVideoOnItemLongClickListener.class, MediaController.class,
		SerenityGallery.class, MainActivity.class, SerenityApplication.class,
		StartupBroadcastReceiver.class, RecommendationPlayerActivity.class,
		EpisodeBrowserActivity.class, SearchableActivity.class,
		MovieBrowserActivity.class, TVShowBrowserActivity.class,
		TVShowSeasonBrowserActivity.class, AutoConfigureHandlerRunnable.class,
		EpisodeMenuDrawerOnItemClickedListener.class,
		ImageInfographicUtils.class, LibraryResponseListener.class,
		MainMenuDrawerOnItemClickedListener.class,
		MovieGridPosterOnItemSelectedListener.class,
		MovieMenuDrawerOnItemClickedListener.class,
		MusicPosterGalleryOnItemSelectedListener.class,
		PlayerResultHandler.class, ExternalPlayerResultHandler.class,
		SeasonOnItemLongClickListener.class, MenuMediaContainer.class,
		MovieMediaContainer.class, SeriesMediaContainer.class,
		SubtitleMediaContainer.class, EpisodeMediaContainer.class,
		SeasonsMediaContainer.class, GalleryVideoOnItemClickListener.class,
		GridVideoOnItemClickListener.class,
		GalleryVideoOnItemLongClickListener.class,
		EpisodeBrowserOnLongClickListener.class,
		GridVideoOnItemLongClickListener.class,
		EpisodePosterOnItemSelectedListener.class,
		MoviePosterOnItemSelectedListener.class, SerenityImageLoader.class,
		TracksOnItemSelectedListener.class,
		TVShowGalleryOnItemSelectedListener.class,
		TVShowGridOnItemSelectedListener.class, TVShowGridOnKeyListener.class,
		TVShowMenuDrawerOnItemClickedListener.class,
		TVShowSeasonMenuDrawerOnItemClickedListener.class,
		TVShowSeasonOnItemSelectedListener.class, VideoPlayerIntentUtils.class,
		VideoPlayerPrepareListener.class, CategoryRetrievalIntentService.class,
		TVShowCategoryRetrievalIntentService.class,
		MoviesRetrievalIntentService.class, MovieSearchIntentService.class,
		MusicAlbumRetrievalIntentService.class,
		MusicRetrievalIntentService.class,
		SecondaryCategoryRetrievalIntentService.class,
		MainMenuTextViewAdapter.class, EpisodePosterImageGalleryAdapter.class,
		SeasonsEpisodePosterImageGalleryAdapter.class,
		MoviePosterImageAdapter.class, SearchAdapter.class,
		TVShowBannerImageGalleryAdapter.class, MenuDrawerAdapter.class,
		MusicAlbumsCoverAdapter.class, MusicPosterGalleryAdapter.class,
		MusicPosterGridViewAdapter.class,
		TVShowSeasonImageGalleryAdapter.class,
		OnDeckRecommendationIntentService.class, CompletedVideoRequest.class,
		FindUnwatchedAsyncTask.class, TracksAdapter.class,
		UnWatchVideoAsyncTask.class, UpdateProgressRequest.class,
		WatchedVideoAsyncTask.class, GalleryOnItemSelectedListener.class,
		SerenityImageLoader.class, SerenityPreferenceActivity.class,
		AndroidTV.class, RecommendAsyncTask.class, RecommendationBuilder.class,
		TVShowPosterImageGalleryAdapter.class, VideoPlayerKeyCodeHandler.class,
		SerenitySurfaceViewVideoActivity.class, OkHttpStack.class,
		SerenityRecommendationContentProvider.class, OKHttpImageLoader.class,
		AndroidHelper.class,
		SecondaryCategorySpinnerOnItemSelectedListener.class,
		MovieCategorySpinnerOnItemSelectedListener.class,
		TVCategorySpinnerOnItemSelectedListener.class,
		TVSecondaryCategorySpinnerOnItemSelectedListener.class,
		VolleyUtils.class, MovieVideoGalleryFragment.class,
		VideoGridFragment.class, EpisodeBrowserActivity.class,
		EpisodeVideoGalleryFragment.class, MenuMediaContainer.class,
		MovieSearchGalleryFragment.class }, library = true)
public class AndroidModule {

	private final Context applicationContext;

	public AndroidModule(Application application) {
		applicationContext = application.getApplicationContext();
	}

	@Provides
	@Singleton
	PlexappFactory providesPlexFactory() {
		IConfiguration serverConfig = ServerConfig
				.getInstance(applicationContext);

		return PlexappFactory.getInstance(serverConfig);
	}

	@Provides
	@ApplicationContext
	Context providesApplicationContext() {
		return applicationContext;
	}

	@Provides
	@Singleton
	SharedPreferences providesSharedPreferences() {
		return PreferenceManager
				.getDefaultSharedPreferences(applicationContext);
	}

	@Provides
	@Singleton
	AndroidHelper providesAndroidHelper() {
		return new AndroidHelper();
	}

	@Provides
	@Singleton
	Resources providesResources() {
		return applicationContext.getResources();
	}

	@Provides
	MediaPlayer providesMediaPlayer() {
		return new MediaPlayer();
	}

	@Provides
	@Singleton
	VolleyUtils providesVolleyUtils() {
		return new VolleyUtils();
	}

}
