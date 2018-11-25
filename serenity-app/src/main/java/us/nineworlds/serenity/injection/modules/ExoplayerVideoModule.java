package us.nineworlds.serenity.injection.modules;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import javax.inject.Provider;
import toothpick.config.Module;
import us.nineworlds.serenity.injection.modules.providers.AdaptiveTrackSelectionFactoryProvider;
import us.nineworlds.serenity.injection.modules.providers.DataSourceFactoryProvider;
import us.nineworlds.serenity.injection.modules.providers.DefaultMappingTrackSelectorProvider;
import us.nineworlds.serenity.injection.modules.providers.EventLoggerProvider;
import us.nineworlds.serenity.injection.modules.providers.HttpDataSourceFactoryProvider;
import us.nineworlds.serenity.ui.video.player.EventLogger;
import us.nineworlds.serenity.ui.video.player.ExoplayerPresenter;

public class ExoplayerVideoModule extends Module {

  public ExoplayerVideoModule() {
    super();
    bind(DefaultBandwidthMeter.class).toInstance(new DefaultBandwidthMeter());
    bind(HttpDataSource.Factory.class).toProvider(HttpDataSourceFactoryProvider.class);
    bind(AdaptiveTrackSelection.Factory.class).toProvider(AdaptiveTrackSelectionFactoryProvider.class);
    bind(MappingTrackSelector.class).toProvider(DefaultMappingTrackSelectorProvider.class);
    bind(EventLogger.class).toProvider(EventLoggerProvider.class);
    bind(ExoplayerPresenter.class).to(ExoplayerPresenter.class);
    bind(DataSource.Factory.class).toProvider(DataSourceFactoryProvider.class);
  }

  public static class DefaultBandWidthMeterProvider implements Provider<DefaultBandwidthMeter> {

    @Override public DefaultBandwidthMeter get() {
      return new DefaultBandwidthMeter();
    }
  }
}
