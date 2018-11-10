package us.nineworlds.serenity.injection.modules;

import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import us.nineworlds.serenity.ui.video.player.EventLogger;
import us.nineworlds.serenity.ui.video.player.ExoplayerPresenter;

@Module(library = true, includes = SerenityModule.class)
public class VideoModule {

  private static final String userAgent = "SerenityAndroid";

  @Provides @Singleton DefaultBandwidthMeter providesDefaultBandwidthMeter() {
    return new DefaultBandwidthMeter();
  }

  @Provides HttpDataSource.Factory buildHttpDataSourceFactory(OkHttpClient okHttpClient) {
    return new OkHttpDataSourceFactory(okHttpClient, userAgent, null, null);
  }

  @Provides AdaptiveTrackSelection.Factory proidesAdapativeTrackSelectionFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new AdaptiveTrackSelection.Factory(bandwidthMeter);
  }

  @Provides MappingTrackSelector providesMappingTrackSelector(AdaptiveTrackSelection.Factory factory) {
    return new DefaultTrackSelector(factory);
  }

  @Provides EventLogger providesEventLogger(MappingTrackSelector trackSelector) {
    return new EventLogger(trackSelector);
  }

  @Provides ExoplayerPresenter providesExoPlayerPresenter() {
    return new ExoplayerPresenter();
  }
}
