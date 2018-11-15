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

import com.google.firebase.analytics.FirebaseAnalytics;
import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.injection.modules.AndroidModule;

public class TestSerenityApplication extends SerenityApplication implements TestLifecycleApplication {

	public TestSerenityApplication() {
		super();
	}

	@Override
	protected List<Object> createModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(this));
		modules.add(new TestingModule());
		return modules;
	}

	@Override public void onCreate() {
		super.onCreate();
		FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false);
	}

	@Override protected void discoverServers() {

	}

	@Override protected void setDefaultPreferences() {

	}

	@Override
	public void afterTest(Method arg0) {

	}

	@Override
	public void beforeTest(Method arg0) {
	}

	@Override
	public void prepareTest(Object arg0) {

	}
}
