package us.nineworlds.serenity.injection.modules.providers;


import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.okhttp.OkHttpDataSource;

import javax.inject.Inject;
import javax.inject.Provider;

import okhttp3.OkHttpClient;

public class HttpDataSourceFactoryProvider implements Provider<HttpDataSource.Factory> {

  private static final String userAgent = "SerenityAndroid";

  @Inject OkHttpClient okHttpClient;

  @Override public HttpDataSource.Factory get() {
    return new OkHttpDataSource.Factory(okHttpClient).setUserAgent(userAgent);
  }
}
