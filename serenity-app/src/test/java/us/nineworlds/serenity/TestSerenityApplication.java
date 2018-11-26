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

import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import com.birbit.android.jobqueue.JobManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.lang.reflect.Method;
import org.greenrobot.eventbus.EventBus;
import org.robolectric.TestLifecycleApplication;
import toothpick.Scope;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.LoginModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.injection.modules.ExoplayerVideoModule;

import static org.mockito.Mockito.mock;

public class TestSerenityApplication extends SerenityApplication implements TestLifecycleApplication {

	public TestSerenityApplication() {
		super();
	}

	protected void inject() {
		Scope scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE);
		scope.installModules(new AndroidModule(this), new SerenityModule(), new LoginModule(), new ExoplayerVideoModule() );
		jobManager = mock(JobManager.class);
		androidHelper = mock(AndroidHelper.class);
		preferences = scope.getInstance(SharedPreferences.class);
		localBroadcastManager = scope.getInstance(LocalBroadcastManager.class);
		logger = mock(Logger.class);
		eventBus = mock(EventBus.class);
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
