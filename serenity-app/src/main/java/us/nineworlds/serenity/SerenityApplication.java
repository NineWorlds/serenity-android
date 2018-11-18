/**
 * The MIT License (MIT)
 * Copyright (c) 2012-2014 David Carver
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

package us.nineworlds.serenity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.birbit.android.jobqueue.JobManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.danlew.android.joda.JodaTimeAndroid;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.registries.FactoryRegistryLocator;
import toothpick.registries.MemberInjectorRegistryLocator;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.emby.server.EmbyServer;
import us.nineworlds.serenity.emby.server.EmbyServerJob;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.LoginModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.injection.modules.VideoModule;
import us.nineworlds.serenity.jobs.GDMServerJob;
import us.nineworlds.serenity.server.GDMReceiver;

/**
 * Global manager for the Serenity application
 *
 * @author dcarver
 */
public class SerenityApplication extends Application {

  @Inject @ForMediaServers Map<String, Server> servers;
  @Inject AndroidHelper androidHelper;
  @Inject SharedPreferences preferences;
  @Inject JobManager jobManager;
  @Inject Logger logger;
  @Inject LocalBroadcastManager localBroadcastManager;
  private BroadcastReceiver gdmReceiver;
  EventBus eventBus;

  private static boolean enableTracking = true;

  public static void disableTracking() {
    enableTracking = false;
  }

  private void init() {
    inject();

    JodaTimeAndroid.init(this);
    sendStartedApplicationEvent();
    eventBus = EventBus.getDefault();
    eventBus.register(this);
    jobManager.start();
    logger.initialize();
  }

  protected void inject() {

    Scope scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE);
    scope.installModules(new AndroidModule(this), new SerenityModule(), new LoginModule(), new VideoModule());
    Toothpick.inject(this, scope);
  }

  protected List<Object> createModules() {
    List<Object> modules = new ArrayList<Object>();
    modules.add(new AndroidModule(this));
    return modules;
  }

  @Override public void onCreate() {
    super.onCreate();
    init();
    setDefaultPreferences();
    gdmReceiver = new GDMReceiver();
    registerGDMReceiver();
    discoverServers();
  }

  protected void setDefaultPreferences() {
    PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
    SharedPreferences.Editor editor = preferences.edit();
    if (androidHelper.isAndroidTV()
        || androidHelper.isAmazonFireTV()
        || androidHelper.isLeanbackSupported()) {
      editor.putBoolean("serenity_tv_mode", true);
      editor.apply();
    }
  }

  protected void sendStartedApplicationEvent() {
    String deviceModel = android.os.Build.MODEL;
    if (enableTracking) {
      FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
      if (analytics != null) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Devices");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, deviceModel);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, deviceModel);

        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
      }
    }
  }

  @Override public void onTerminate() {
    eventBus.unregister(this);
    jobManager.stop();
    localBroadcastManager.unregisterReceiver(gdmReceiver);
    super.onTerminate();
  }

  protected void registerGDMReceiver() {
    IntentFilter filters = new IntentFilter();
    filters.addAction(GDMReceiver.GDM_MSG_RECEIVED);
    filters.addAction(GDMReceiver.GDM_SOCKET_CLOSED);
    localBroadcastManager.registerReceiver(gdmReceiver, filters);
  }

  protected void discoverServers() {
    jobManager.addJobInBackground(new GDMServerJob());
    jobManager.addJobInBackground(new EmbyServerJob());
  }

  @Subscribe(threadMode = ThreadMode.BACKGROUND) public void onEmbyServerDiscovery(EmbyServer serverEvent) {
    servers.put(serverEvent.getServerName(), serverEvent);
  }

}
