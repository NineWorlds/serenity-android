package us.nineworlds.serenity.injection.modules;

import android.content.Context;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import us.nineworlds.serenity.injection.ApplicationContext;
import us.nineworlds.serenity.ui.video.player.EventLogger;

@Module(library = true)
public class VideoModule {

  private static final String userAgent = "SerenityAndroid";

  @Provides @Singleton DefaultBandwidthMeter providesDefaultBandwidthMeter() {
    return new DefaultBandwidthMeter();
  }

  @Provides DataSource.Factory providesMediaDataSource(@ApplicationContext Context context,
      DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultDataSourceFactory(context, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
  }

  @Provides HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
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
}
