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

import static org.fest.assertions.api.ANDROID.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.SerenityShadowResources;


/**
 * @author dcarver
 * 
 */
@RunWith(RobolectricTestRunner.class)
@Config(shadows = SerenityShadowResources.class)
public class MainActivityTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		Robolectric.getBackgroundScheduler().reset();
	}

	@Test
	public void assertThatMainActivityIsCreated() throws Exception {
		MainActivity activity = Robolectric.buildActivity(MainActivity.class)
				.get();
		assertThat(activity).isNotNull().isNotFinishing();
	}

	@Test
	public void onCreateCreatesMenu() throws Exception {
		MainActivity activity = Robolectric.buildActivity(MainActivity.class)
				.create().get();
		assertThat(activity.getMenuDrawer()).isNotNull();
		assertThat(activity.findViewById(R.id.mainGalleryBackground))
				.isVisible();
	}
}
