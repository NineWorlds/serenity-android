package us.nineworlds.serenity.injection.modules.providers;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import javax.inject.Inject;
import javax.inject.Provider;

public class DefaultMappingTrackSelectorProvider implements Provider<MappingTrackSelector> {

  @Inject AdaptiveTrackSelection.Factory factory;

  @Override public MappingTrackSelector get() {
    return new DefaultTrackSelector(factory);
  }
}
