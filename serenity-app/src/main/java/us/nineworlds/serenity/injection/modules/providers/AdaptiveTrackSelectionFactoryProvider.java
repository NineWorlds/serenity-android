package us.nineworlds.serenity.injection.modules.providers;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import javax.inject.Inject;
import javax.inject.Provider;

public class AdaptiveTrackSelectionFactoryProvider implements Provider<AdaptiveTrackSelection.Factory> {

  @Inject DefaultBandwidthMeter bandwidthMeter;

  @Override public AdaptiveTrackSelection.Factory get() {
    return new AdaptiveTrackSelection.Factory(bandwidthMeter);
  }
}
