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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;

import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.SerenityShadowResources;
import org.robolectric.shadows.ShadowActivity;

import android.view.KeyEvent;
import android.widget.Gallery;

/**
 * @author dcarver
 * 
 */
@RunWith(SerenityRobolectricTestRunner.class)
@Config(shadows = SerenityShadowResources.class)
public class MainActivityTest {

	MainActivity activity;

	@Before
	public void setUp() throws Exception {
		try {
			activity = Robolectric.buildActivity(MainActivity.class).attach()
					.create().start().get();
		} catch (NullPointerException ex) {
			activity = Robolectric.buildActivity(MainActivity.class).create()
					.start().get();
		}
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testAssertThatMainActivityIsCreated() throws Exception {
		assertThat(activity).isNotNull().isNotFinishing();
	}

	@Test
	public void testCreatesMenu() throws Exception {
		assertThat(activity.getMenuDrawer()).isNotNull();
		assertThat(activity.findViewById(R.id.mainGalleryBackground))
				.isVisible();
	}

	@Test
	public void testGalleryAdapterIsNotNull() throws Exception {
		Gallery gallery = (Gallery) activity.findViewById(R.id.mainGalleryMenu);
		assertThat(gallery.getAdapter()).isNotNull().isInstanceOf(
				MainMenuTextViewAdapter.class);
	}

	@Test
	public void testGalleryOnItemClickListenerIsSet() throws Exception {
		Gallery gallery = (Gallery) activity.findViewById(R.id.mainGalleryMenu);
		Assertions.assertThat(gallery.getOnItemClickListener()).isNotNull()
				.isInstanceOf(GalleryOnItemClickListener.class);
	}

	@Test
	public void testGalleryOnItemSelectedListenerIsSet() throws Exception {
		Gallery gallery = (Gallery) activity.findViewById(R.id.mainGalleryMenu);
		Assertions.assertThat(gallery.getOnItemSelectedListener()).isNotNull()
				.isInstanceOf(GalleryOnItemSelectedListener.class);
	}

	@Test
	public void testThatActivityIsDestroyed() throws Exception {
		ShadowActivity a = Robolectric.shadowOf(activity);
		a.callOnDestroy();
		Assertions.assertThat(a.isDestroyed()).isTrue();
	}

	@Test
	public void testThatActivityStops() throws Exception {
		ShadowActivity a = Robolectric.shadowOf(activity);
		a.callOnStop();
		Assertions.assertThat(a.isFinishing()).isFalse();
	}

	@Test
	public void testThatActivityCallsOnResume() throws Exception {
		ShadowActivity a = Robolectric.shadowOf(activity);
		a.callOnResume();
		Gallery g = (Gallery) activity.findViewById(R.id.mainGalleryMenu);
		assertThat(g).isVisible().isEnabled();
	}

	@Test
	public void testThatActivityCallsOnRestart() throws Exception {
		ShadowActivity a = Robolectric.shadowOf(activity);
		a.callOnRestart();
		Gallery g = (Gallery) activity.findViewById(R.id.mainGalleryMenu);
		assertThat(g).isVisible().isEnabled();
	}

	boolean visible = false;

	@Test
	public void testMenuDrawerOpens() throws Exception {
		visible = false;
		KeyEvent keyEvent = mock(KeyEvent.class);
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.KEYCODE_MENU);
		MenuDrawer drawer = activity.getMenuDrawer();
		drawer.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {

			@Override
			public void onDrawerStateChange(int oldState, int newState) {
				if (oldState == MenuDrawer.STATE_CLOSED
						&& (newState == MenuDrawer.STATE_OPEN || newState == MenuDrawer.STATE_OPENING)) {
					visible = true;
				}
			}

			@Override
			public void onDrawerSlide(float arg0, int arg1) {

			}
		});

		activity.onKeyDown(KeyEvent.KEYCODE_MENU, keyEvent);
		Thread.sleep(1000);
		Assertions.assertThat(visible).isTrue();
	}

	@Test
	public void testMenuDrawerCloses() throws Exception {
		visible = true;
		KeyEvent keyEvent = mock(KeyEvent.class);
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.KEYCODE_MENU);
		MenuDrawer drawer = activity.getMenuDrawer();
		drawer.toggleMenu();

		drawer.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {

			@Override
			public void onDrawerStateChange(int oldState, int newState) {
				if (oldState == MenuDrawer.STATE_OPENING
						&& (newState == MenuDrawer.STATE_CLOSED || newState == MenuDrawer.STATE_CLOSING)) {
					visible = false;
				}
			}

			@Override
			public void onDrawerSlide(float arg0, int arg1) {

			}
		});

		Thread.sleep(1000);
		drawer.toggleMenu();
		Assertions.assertThat(visible).isFalse();
	}

}
