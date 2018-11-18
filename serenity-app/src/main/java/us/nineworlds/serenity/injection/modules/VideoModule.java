package us.nineworlds.serenity.injection.modules;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import toothpick.config.Module;
import us.nineworlds.serenity.injection.modules.providers.AdaptiveTrackSelectionFactoryProvider;
import us.nineworlds.serenity.injection.modules.providers.DefaultMappingTrackSelectorProvider;
import us.nineworlds.serenity.injection.modules.providers.EventLoggerProvider;
import us.nineworlds.serenity.injection.modules.providers.HttpDataSourceFactoryProvider;
import us.nineworlds.serenity.ui.video.player.EventLogger;
import us.nineworlds.serenity.ui.video.player.ExoplayerPresenter;

public class VideoModule extends Module {

  public VideoModule() {
    super();
    bind(DefaultBandwidthMeter.class).toInstance(new DefaultBandwidthMeter());
    bind(HttpDataSource.Factory.class).toProvider(HttpDataSourceFactoryProvider.class);
    bind(AdaptiveTrackSelection.Factory.class).toProvider(AdaptiveTrackSelectionFactoryProvider.class);
    bind(MappingTrackSelector.class).toProvider(DefaultMappingTrackSelectorProvider.class);
    bind(EventLogger.class).toProvider(EventLoggerProvider.class);
    bind(ExoplayerPresenter.class).toInstance(new ExoplayerPresenter());
  }
}
