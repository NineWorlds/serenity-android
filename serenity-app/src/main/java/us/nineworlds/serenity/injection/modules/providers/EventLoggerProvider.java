package us.nineworlds.serenity.injection.modules.providers;

import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import javax.inject.Inject;
import javax.inject.Provider;
import us.nineworlds.serenity.ui.video.player.EventLogger;

public class EventLoggerProvider implements Provider<EventLogger> {

  @Inject MappingTrackSelector mappingTrackSelector;

  @Override public EventLogger get() {
    return new EventLogger(mappingTrackSelector);
  }
}
