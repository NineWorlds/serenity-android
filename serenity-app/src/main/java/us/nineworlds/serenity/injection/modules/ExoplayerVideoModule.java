package us.nineworlds.serenity.injection.modules;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.trackselection.TrackSelector;
import toothpick.config.Module;
import us.nineworlds.serenity.injection.modules.providers.DataSourceFactoryProvider;
import us.nineworlds.serenity.injection.modules.providers.DefaultMappingTrackSelectorProvider;
import us.nineworlds.serenity.injection.modules.providers.EventLoggerProvider;
import us.nineworlds.serenity.injection.modules.providers.HttpDataSourceFactoryProvider;
import us.nineworlds.serenity.ui.video.player.EventLogger;
import us.nineworlds.serenity.ui.video.player.ExoplayerPresenter;

public class ExoplayerVideoModule extends Module {

  @OptIn(markerClass = UnstableApi.class)
  public ExoplayerVideoModule() {
    super();

    bind(HttpDataSource.Factory.class).toProvider(HttpDataSourceFactoryProvider.class);
    bind(TrackSelector.class).toProvider(DefaultMappingTrackSelectorProvider.class);
    bind(EventLogger.class).toProvider(EventLoggerProvider.class);
    bind(ExoplayerPresenter.class).to(ExoplayerPresenter.class);
    bind(DataSource.Factory.class).toProvider(DataSourceFactoryProvider.class);
  }
}
