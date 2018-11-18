package us.nineworlds.serenity.injection.modules.providers;

import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import javax.inject.Inject;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

public class HttpDataSourceFactoryProvider implements Provider<HttpDataSource.Factory> {

  private static final String userAgent = "SerenityAndroid";

  @Inject OkHttpClient okHttpClient;

  @Override public HttpDataSource.Factory get() {
    return new OkHttpDataSourceFactory(okHttpClient, userAgent, null, null);
  }
}
