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
import android.graphics.drawable.TransitionDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowView;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.junit.Assert.assertEquals;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BackgroundBitmapDisplayerTest {

	View backgroundView;

	@Mock
	Bitmap mockBitmap;

	BackgroundBitmapDisplayer backgroundBitmapDisplayer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
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
				.getDefaultSharedPreferences(application);
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
				.getDefaultSharedPreferences(application);
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

	@Test
	public void defaultResourceImageIdIsSetWhenBitmapIsNull() {
		backgroundBitmapDisplayer = new BackgroundBitmapDisplayer(null,
				R.drawable.movies, backgroundView);
		backgroundBitmapDisplayer.run();

		ShadowView shadowView = shadowOf(backgroundView);
		assertEquals("Resource Ids did not match.",
				shadowView.getBackgroundResourceId(), R.drawable.movies);
	}
}
