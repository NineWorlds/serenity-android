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

package us.nineworlds.serenity.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPackageManager;

import toothpick.config.Module;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.testrunner.PlainAndroidRunner;

import javax.inject.Inject;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class OnDeckRecommendationsTest extends InjectingTest {

	private static final String ANDROID_SOFTWARE_LEANBACK = "android.software.leanback";
	private static final String ANDROID_HARDWARE_TYPE_TELEVISION = "android.hardware.type.television";
	private OnDeckRecommendations onDeckRecommendations;

	@Inject
	AndroidHelper mockAndroidHelper;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		onDeckRecommendations = new OnDeckRecommendations(getApplicationContext());
	}

	@Override public void installTestModules() {
		scope.installTestModules(new TestingModule(), new TestModule());
	}

	@After
	public void tearDown() {

	}

	@Test
	public void recommendationsOccurForJellyBeanOrHigher() {
		doReturn(true).when(mockAndroidHelper).isLeanbackSupported();

		ShadowPackageManager shadowPackageManager = shadowOf(getApplicationContext().getPackageManager());
		shadowPackageManager.setSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION, true);
		shadowPackageManager.setSystemFeature(ANDROID_SOFTWARE_LEANBACK, true);

		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor edit = prefrences.edit();
		edit.putBoolean(SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS,
				true);
		edit.putBoolean(SerenityConstants.PREFERENCE_TV_MODE, true);
		edit.apply();

		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isTrue();
		verify(mockAndroidHelper).isLeanbackSupported();
	}

	@Test
	public void recommendationsOccurForJellyBeanOrHigherFailOnGoogleTV4DevicesWithOutLeanback() {
		ShadowPackageManager shadowPackageManager = shadowOf(getApplicationContext().getPackageManager());
		shadowPackageManager.setSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION, true);
		shadowPackageManager.setSystemFeature(ANDROID_SOFTWARE_LEANBACK, false);

		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor edit = prefrences.edit();
		edit.putBoolean(SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS,
				true);
		edit.putBoolean(SerenityConstants.PREFERENCE_TV_MODE, true);
		edit.apply();

		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isFalse();
	}

	@Test
	public void recommendationsOccurForJellyBeanOrHigherFailWhenAndroidTVModeIsFalse() {
		doReturn(true).when(mockAndroidHelper).isLeanbackSupported();
		ShadowPackageManager shadowPackageManager = shadowOf(getApplicationContext().getPackageManager());
		shadowPackageManager.setSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION, true);
		shadowPackageManager.setSystemFeature(ANDROID_SOFTWARE_LEANBACK, true);

		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor edit = prefrences.edit();
		edit.putBoolean(SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS,
				true);
		edit.putBoolean(SerenityConstants.PREFERENCE_TV_MODE, false);
		edit.apply();

		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isFalse();
		verify(mockAndroidHelper).isLeanbackSupported();
	}

	public class TestModule extends Module {

		public TestModule() {
			bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()));
			bind(Context.class).withName(ApplicationContext.class).toInstance(ApplicationProvider.getApplicationContext());
		}
	}
}
