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

package us.nineworlds.serenity.core.imageloader;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.Animation;

import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowView;

import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class BackgroundBitmapDisplayerTest {

	View backgroundView;

	@Mock
	Bitmap mockBitmap;

	BackgroundBitmapDisplayer backgroundBitmapDisplayer;

	@Before
	public void setUp() {
		initMocks(this);
		Robolectric.getBackgroundThreadScheduler().pause();
		Robolectric.getForegroundThreadScheduler().pause();
		backgroundView = new View(application);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void backgroundViewDoesNotHaveAnimationWhenFadeInNotSet()
			throws Exception {
		backgroundBitmapDisplayer = new BackgroundBitmapDisplayer(mockBitmap,
				R.drawable.movies, backgroundView);
		backgroundBitmapDisplayer.run();
		Animation animation = backgroundView.getAnimation();
		assertThat(animation).isNull();
	}

	@Test
	public void backgroundViewHasTransitionDrawableSet() throws Exception {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
		Editor editor = prefs.edit();
		editor.putBoolean("animation_background_fadein", true);
		editor.commit();

		backgroundBitmapDisplayer = new BackgroundBitmapDisplayer(mockBitmap,
				R.drawable.movies, backgroundView);
		backgroundBitmapDisplayer.run();

		assertThat(backgroundView.getBackground()).isInstanceOf(
				TransitionDrawable.class);
	}

	@Test
	public void backgroundViewHasTransitionDrawableWithCrossfade()
			throws Exception {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
		Editor editor = prefs.edit();
		editor.putBoolean("animation_background_fadein", true);
		editor.commit();

		backgroundBitmapDisplayer = new BackgroundBitmapDisplayer(mockBitmap,
				R.drawable.movies, backgroundView);
		backgroundBitmapDisplayer.run();

		TransitionDrawable transitionDrawable = (TransitionDrawable) backgroundView
				.getBackground();
		assertThat(transitionDrawable).isCrossFadeEnabled();
	}

}
