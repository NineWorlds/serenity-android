/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.injection.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.google.android.exoplayer2.upstream.DataSource;
import javax.inject.Inject;
import javax.inject.Provider;
import org.greenrobot.eventbus.EventBus;
import toothpick.config.Module;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.core.util.StringPreference;
import us.nineworlds.serenity.emby.server.api.EmbyAPIClient;
import us.nineworlds.serenity.injection.ServerClientPreference;
import us.nineworlds.serenity.injection.ServerIPPreference;
import us.nineworlds.serenity.injection.ServerPortPreference;
import us.nineworlds.serenity.injection.modules.providers.DataSourceFactoryProvider;
import us.nineworlds.serenity.injection.modules.providers.ServerClientPreferenceProvider;

public class AndroidModule extends Module {

  private final Context applicationContext;
  private static SerenityClient embyAPIClient;
  private static SerenityClient plexClient;

  public AndroidModule(Application application) {
    this.applicationContext = application;
    bind(EventBus.class).toInstance(EventBus.getDefault());
    bind(Context.class).withName(ApplicationContext.class).toInstance(application);
    bind(StringPreference.class).withName(ServerClientPreference.class).toProvider(ServerClientPreferenceProvider.class).providesSingletonInScope();
    bind(StringPreference.class).withName(ServerIPPreference.class).toProvider(ServerIPPreferenceProvider.class).providesSingletonInScope();
    bind(StringPreference.class).withName(ServerPortPreference.class).toProvider(ServerPorPreferenceProvider.class).providesSingletonInScope();
    bind(SerenityClient.class).toProvider(SerentityClientProvider.class).providesSingletonInScope();
    bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(applicationContext));
    bind(AndroidHelper.class).toInstance(new AndroidHelper(applicationContext));
    bind(Resources.class).toInstance(application.getResources());
    bind(JobManager.class).toProvider(JobManagerProvider.class).providesSingletonInScope();
    bind(LocalBroadcastManager.class).toInstance(providesLocalBroadcastManager());
  }


  private LocalBroadcastManager providesLocalBroadcastManager() {
    return LocalBroadcastManager.getInstance(applicationContext);
  }

  public static class JobManagerProvider implements Provider<JobManager> {

    @Inject @ApplicationContext Context context;

    @Override public JobManager get() {
        Configuration configuration = new Configuration.Builder(context).minConsumerCount(1)
            .maxConsumerCount(5)
            .loadFactor(3)
            .consumerKeepAlive(120)
            .build();
        return new JobManager(configuration);
    }
  }

  public static class EventBusProvider implements Provider<EventBus> {

    @Override public EventBus get() {
      return EventBus.getDefault();
    }
  }


  public static class ServerIPPreferenceProvider implements Provider<StringPreference> {
    @Inject SharedPreferences sharedPreferences;

    @Override public StringPreference get() {
      return new StringPreference(sharedPreferences, "server", "");
    }
  }

  public static class ServerPorPreferenceProvider implements Provider<StringPreference> {
    @Inject SharedPreferences sharedPreferences;

    @Override public StringPreference get() {
      return new StringPreference(sharedPreferences, "serverport", "");
    }
  }

  public static class SerentityClientProvider implements Provider<SerenityClient> {

    @Inject @ApplicationContext Context applicationContext;
    @Inject @ServerClientPreference StringPreference serverClientPreference;

    @Override public SerenityClient get() {
      if (plexClient == null) {
        IConfiguration serverConfig = ServerConfig.getInstance(applicationContext);
        plexClient = PlexappFactory.getInstance(serverConfig);
      }
      if (embyAPIClient == null) {
        embyAPIClient = new EmbyAPIClient(applicationContext, "http://localhost:8096/");
      }

      if ("Emby".equals(serverClientPreference.get())) {
        return embyAPIClient;
      }

      return plexClient;
    }
  }


}
