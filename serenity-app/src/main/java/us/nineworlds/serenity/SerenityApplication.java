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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.birbit.android.jobqueue.JobManager;
import com.castillo.dd.PendingDownload;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.SerenityObjectGraph;
import us.nineworlds.serenity.injection.modules.AndroidModule;

/**
 * Global manager for the Serenity application
 *
 * @author dcarver
 *
 */
public class SerenityApplication extends Application {

    @Inject
    AndroidHelper androidHelper;

    @Inject
    SharedPreferences preferences;

    @Inject
    JobManager jobManager;

    private static boolean enableTracking = true;
    private static List<PendingDownload> pendingDownloads;

    public static final int PROGRESS = 0xDEADBEEF;

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

    public static List<PendingDownload> getPendingDownloads() {
        return pendingDownloads;
    }

    public static boolean isTrackingEnabled() {
        return enableTracking;
    }

    public SerenityApplication() {
        pendingDownloads = new ArrayList<PendingDownload>();

    }

    private void init() {
        inject();
        if (enableTracking) {
            installAnalytics();
        }
        sendStartedApplicationEvent();
        jobManager.start();
    }

    protected void inject() {
        SerenityObjectGraph objectGraph = SerenityObjectGraph.getInstance();
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
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread
                .getDefaultUncaughtExceptionHandler();
        if (uncaughtExceptionHandler instanceof ExceptionReporter) {
            ExceptionReporter exceptionReporter = (ExceptionReporter) uncaughtExceptionHandler;
            exceptionReporter
                    .setExceptionParser(new AnalyticsExceptionParser());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();

        setDefaultPreferences();
    }

    protected void setDefaultPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        SharedPreferences.Editor editor = preferences.edit();
        if (androidHelper.isGoogleTV() || androidHelper.isAndroidTV()
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
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Devices")
                        .setAction("Started Application").setLabel(deviceModel)
                        .build());
            }
        }
    }

    @Override
    public void onTerminate() {
        jobManager.stop();
        super.onTerminate();
    }
}
