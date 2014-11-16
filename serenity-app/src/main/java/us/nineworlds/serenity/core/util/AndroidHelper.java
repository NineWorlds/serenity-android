/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver
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

package us.nineworlds.serenity.core.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import us.nineworlds.serenity.injection.ApplicationContext;
import us.nineworlds.serenity.injection.BaseInjector;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Build;

@Singleton
public class AndroidHelper extends BaseInjector {

	private static final String COM_GOOGLE_ANDROID_TV = "com.google.android.tv";

	@Inject
	@ApplicationContext
	Context context;

	public boolean isAndroidTV() {
		final PackageManager pm = context.getPackageManager();

		if (isAmazonFireTV()) {
			return true;
		}
		return pm.hasSystemFeature("android.hardware.type.television");
	}

	public boolean isAmazonFireTV() {
		return Build.MODEL.startsWith("AFT")
				&& Build.MANUFACTURER.equals("Amazon");
	}

	public boolean isLeanbackSupported() {
		final PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature("android.software.leanback");
	}

	public boolean isGoogleTV() {
		final PackageManager pm = context.getPackageManager();
		FeatureInfo[] features = pm.getSystemAvailableFeatures();
		return pm.hasSystemFeature(COM_GOOGLE_ANDROID_TV);
	}

	public boolean isRunningOnOUYA() {
		if ("OUYA".equals(Build.MANUFACTURER)) {
			return true;
		}
		return false;
	}
}
