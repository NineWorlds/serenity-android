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
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.birbit.android.jobqueue.JobManager;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;
import us.nineworlds.serenity.core.ServerConfig;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.emby.server.EmbyServer;
import us.nineworlds.serenity.emby.server.EmbyServerJob;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.injection.modules.AndroidModule;
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
  @Inject EventBus eventBus;

  private static boolean enableTracking = true;

  public enum TrackerName {
    APP_TRACKER, // Tracker used only in this app.
    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
    // roll-up tracking.
    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
    // company.
  }

  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

  public static void disableTracking() {
    enableTracking = false;
  }

  synchronized Tracker getTracker() {
    if (!mTrackers.containsKey(TrackerName.GLOBAL_TRACKER)) {

      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      Tracker t = analytics.newTracker(R.xml.global_tracker);
      mTrackers.put(TrackerName.GLOBAL_TRACKER, t);
    }
    return mTrackers.get(TrackerName.GLOBAL_TRACKER);
  }

  private void init() {
    inject();
    if (enableTracking) {
      installAnalytics();
    }
    sendStartedApplicationEvent();
    eventBus.register(this);
    jobManager.start();
    logger.initialize();
  }

  protected void inject() {
    SerenityObjectGraph objectGraph = SerenityObjectGraph.Companion.getInstance();
    objectGraph.createObjectGraph(createModules());
    objectGraph.inject(this);
  }

  protected List<Object> createModules() {
    List<Object> modules = new ArrayList<Object>();
    modules.add(new AndroidModule(this));
    return modules;
  }

  protected void installAnalytics() {
    Tracker tracker = getTracker();
    Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    if (uncaughtExceptionHandler instanceof ExceptionReporter) {
      ExceptionReporter exceptionReporter = (ExceptionReporter) uncaughtExceptionHandler;
      exceptionReporter.setExceptionParser(new AnalyticsExceptionParser());
    }
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
    if (androidHelper.isGoogleTV()
        || androidHelper.isAndroidTV()
        || androidHelper.isAmazonFireTV()
        || androidHelper.isLeanbackSupported()) {
      editor.putBoolean("serenity_tv_mode", true);
      editor.apply();
    }
  }

  protected void sendStartedApplicationEvent() {
    String deviceModel = android.os.Build.MODEL;
    if (enableTracking) {
      Tracker tracker = getTracker();
      if (tracker != null) {
        tracker.send(new HitBuilders.EventBuilder().setCategory("Devices")
            .setAction("Started Application")
            .setLabel(deviceModel)
            .build());
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

  private void discoverServers() {
    jobManager.addJobInBackground(new GDMServerJob());
    jobManager.addJobInBackground(new EmbyServerJob());
  }

  @Subscribe(threadMode = ThreadMode.BACKGROUND) public void onEmbyServerDiscovery(EmbyServer serverEvent) {
    servers.put(serverEvent.getServerName(), serverEvent);
  }

}
