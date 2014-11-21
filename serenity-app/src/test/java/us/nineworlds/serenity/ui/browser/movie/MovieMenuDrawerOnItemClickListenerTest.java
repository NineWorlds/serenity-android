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

package us.nineworlds.serenity.ui.browser.movie;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.AdapterView;
import dagger.Module;
import dagger.Provides;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class MovieMenuDrawerOnItemClickListenerTest extends InjectingTest {

	@Mock
	DrawerLayout mockDrawerLayout;

	@Mock
	AdapterView mockAdapterView;

	@Mock
	View mockView;

	@Mock
	SharedPreferences sharedPreferences;

	@Mock
	Editor mockEditor;

	@Mock
	VideoPlayerIntentUtils mockVPUtils;

	MovieBrowserActivity movieBrowserActivity;

	MovieBrowserActivity spyMovieBrowserActivity;

	MovieMenuDrawerOnItemClickedListener onItemClickedListener;

	@Override
	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundScheduler().pause();
		Robolectric.getUiThreadScheduler().pause();
		MockitoAnnotations.initMocks(this);
		super.setUp();

		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();

		spyMovieBrowserActivity = Mockito.spy(movieBrowserActivity);

		onItemClickedListener = new MovieMenuDrawerOnItemClickedListener(
				mockDrawerLayout);
	}

	@Test
	public void setsGridViewEnabled() {
		spyMovieBrowserActivity.setGridViewEnabled(false);

		doNothing().when(spyMovieBrowserActivity).recreate();

		doReturn(spyMovieBrowserActivity).when(mockView).getContext();
		doReturn(mockEditor).when(sharedPreferences).edit();

		onItemClickedListener.onItemClick(mockAdapterView, mockView, 0, 0);

		assertThat(spyMovieBrowserActivity.isGridViewActive()).isTrue();
	}

	@Test
	public void setsGridViewDisabledWhenDetailViewSelected() {
		spyMovieBrowserActivity.setGridViewEnabled(true);

		doNothing().when(spyMovieBrowserActivity).recreate();

		doReturn(spyMovieBrowserActivity).when(mockView).getContext();
		doReturn(mockEditor).when(sharedPreferences).edit();

		onItemClickedListener.onItemClick(mockAdapterView, mockView, 1, 0);

		assertThat(spyMovieBrowserActivity.isGridViewActive()).isFalse();
	}

	@Test
	public void videoStartsPlayingAllVideosFromQueue() {
		spyMovieBrowserActivity.setGridViewEnabled(false);

		doNothing().when(spyMovieBrowserActivity).recreate();

		doReturn(spyMovieBrowserActivity).when(mockView).getContext();
		doReturn(mockEditor).when(sharedPreferences).edit();

		onItemClickedListener.onItemClick(mockAdapterView, mockView, 2, 0);

		verify(mockVPUtils).playAllFromQueue(spyMovieBrowserActivity);
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(Robolectric.application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
		MovieMenuDrawerOnItemClickedListener.class,
		MovieMenuDrawerOnItemClickListenerTest.class })
	public class TestModule {

		@Provides
		@Singleton
		SharedPreferences providesSharedPreferences() {
			return sharedPreferences;
		}

		@Provides
		@Singleton
		VideoPlayerIntentUtils providesVideoPlayerUtils() {
			return mockVPUtils;
		}

	}

}
