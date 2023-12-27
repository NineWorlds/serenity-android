package us.nineworlds.serenity.injection.modules.providers;

import android.content.Context;

import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import javax.inject.Inject;
import javax.inject.Provider;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;

public class DataSourceFactoryProvider implements Provider<DataSource.Factory> {

  @Inject @ApplicationContext Context context;
  DefaultBandwidthMeter bandwidthMeter;
  @Inject HttpDataSource.Factory httpDataSourceFactory;

  @Override public DataSource.Factory get() {
    bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(context);

    return new DefaultDataSourceFactory(context, bandwidthMeter, httpDataSourceFactory);
  }
}
