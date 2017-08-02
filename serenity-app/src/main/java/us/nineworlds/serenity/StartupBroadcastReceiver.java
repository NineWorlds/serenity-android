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

package us.nineworlds.serenity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import javax.inject.Inject;

import us.nineworlds.serenity.core.services.OnDeckRecommendationIntentService;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.InjectingBroadcastReceiver;
import us.nineworlds.serenity.injection.SerenityObjectGraph;

/**
 * Used to automatically launch Serenity for Android after boot is completed on
 * a device. This is only enabled if the startup preference option has been set
 * to true.
 * <p>
 * Recommendations are always run if on an Android TV device or a device that
 * supports the leanback feature.
 *
 */
public class StartupBroadcastReceiver extends InjectingBroadcastReceiver {

    @Inject
    AndroidHelper androidHelper;

    @Inject
    SharedPreferences preferences;

    private static final int INITIAL_DELAY = 5000;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        SerenityObjectGraph.getInstance().inject(this);
        this.context = context;

        if (intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            createRecomendations();
            launchSerenityOnStartup();
        }
    }

    protected void launchSerenityOnStartup() {
        boolean startupAfterBoot = preferences.getBoolean("serenity_boot_startup", false);
        if (startupAfterBoot) {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    protected void createRecomendations() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return;
        }

        if (!androidHelper.isLeanbackSupported()) {
            return;
        }

        boolean onDeckRecommendations = preferences.getBoolean("androidtv_recommendation_ondeck", false);

        if (!onDeckRecommendations) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent recommendationIntent = new Intent(context, OnDeckRecommendationIntentService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, recommendationIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                INITIAL_DELAY, AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }
}
