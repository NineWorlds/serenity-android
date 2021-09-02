package us.nineworlds.serenity.injection.modules.providers;

import android.content.Context;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;

import javax.inject.Inject;
import javax.inject.Provider;

import us.nineworlds.serenity.common.android.injection.ApplicationContext;

public class DefaultMappingTrackSelectorProvider implements Provider<MappingTrackSelector> {

  @Inject
  @ApplicationContext
  Context context;

  @Override public MappingTrackSelector get() {
    return new DefaultTrackSelector(context, new AdaptiveTrackSelection.Factory());
  }
}
