package us.nineworlds.serenity.injection.modules.providers;


import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;

import javax.inject.Inject;
import javax.inject.Provider;

import us.nineworlds.serenity.ui.video.player.EventLogger;

@UnstableApi
public class EventLoggerProvider implements Provider<EventLogger> {

  @Inject
  MappingTrackSelector mappingTrackSelector;

  @Override public EventLogger get() {
    return new EventLogger(mappingTrackSelector);
  }
}
