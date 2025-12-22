package us.nineworlds.serenity.injection.modules.providers;

import android.content.Context;


import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;

import javax.inject.Inject;
import javax.inject.Provider;

import us.nineworlds.serenity.common.android.injection.ApplicationContext;

public class DefaultMappingTrackSelectorProvider implements Provider<MappingTrackSelector> {

  @Inject
  @ApplicationContext
  Context context;

    @UnstableApi
    @Override public MappingTrackSelector get() {
    return new DefaultTrackSelector(context, new AdaptiveTrackSelection.Factory());
  }
}
