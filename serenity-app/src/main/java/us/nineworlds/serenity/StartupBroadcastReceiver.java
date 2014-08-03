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
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity;

import us.nineworlds.serenity.core.OnDeckRecommendations;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Used to automatically launch Serenity for Android after boot is completed on
 * a device. This is only enabled if the startup preference option has been set
 * to true.
 * 
 * @author dcarver
 * 
 */
public class StartupBroadcastReceiver extends BroadcastReceiver {

	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;

		if (intent.getAction() == null) {
			return;
		}

		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			createRecomendations();
			launchSerenityOnStartup();
		}
	}

	/**
	 * @param context
	 */
	protected void launchSerenityOnStartup() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean startupAfterBoot = prefs.getBoolean("serenity_boot_startup",
				false);
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

		OnDeckRecommendations onDeckRecommendations = new OnDeckRecommendations(
				context);
		onDeckRecommendations.recommend();
	}

}
