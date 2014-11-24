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
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
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
import org.robolectric.shadows.ShadowActivity;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.widgets.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import dagger.Module;
import dagger.Provides;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class MovieBrowserActivityTest extends InjectingTest {

	@Mock
	SharedPreferences mockSharedPreferences;

	MovieBrowserActivity movieBrowserActivity;

	@Override
	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundScheduler().pause();
		Robolectric.getUiThreadScheduler().pause();

		MockitoAnnotations.initMocks(this);
		super.setUp();
	}

	@Test
	public void menuOptionsHasGridView() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();
		ListView drawerList = (ListView) movieBrowserActivity
				.findViewById(R.id.left_drawer_list);

		assertThat(drawerList).isNotNull();
		ListAdapter adapter = drawerList.getAdapter();
		MenuDrawerItem item = (MenuDrawerItem) adapter.getItem(0);
		assertThat(item.getText()).isEqualTo("Grid View");
	}

	@Test
	public void menuOptionsHasDetailView() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();
		ListView drawerList = (ListView) movieBrowserActivity
				.findViewById(R.id.left_drawer_list);

		assertThat(drawerList).isNotNull();
		ListAdapter adapter = drawerList.getAdapter();
		MenuDrawerItem item = (MenuDrawerItem) adapter.getItem(1);
		assertThat(item.getText()).isEqualTo("Detail View");
	}

	@Test
	public void menuOptionsHasPlayAllFromQueue() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();
		ListView drawerList = (ListView) movieBrowserActivity
				.findViewById(R.id.left_drawer_list);

		assertThat(drawerList).isNotNull();
		ListAdapter adapter = drawerList.getAdapter();
		MenuDrawerItem item = (MenuDrawerItem) adapter.getItem(2);
		assertThat(item.getText()).isEqualTo("Play All from Queue");
	}

	@Test
	public void resumeStartsCategoriesService() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().resume()
				.get();
		ShadowActivity shadowActivity = Robolectric
				.shadowOf(movieBrowserActivity);
		Intent serviceIntent = shadowActivity.getNextStartedService();
		assertThat(serviceIntent).isNotNull();
		assertThat(serviceIntent.getComponent().getClassName()).contains(
				"CategoryRetrievalIntentService");
	}

	@Test
	public void restartCallsPopulateMenuDrawer() {
		doReturn(true).when(mockSharedPreferences).getBoolean(anyString(),
				anyBoolean());
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();
		MovieBrowserActivity spyActivity = Mockito.spy(movieBrowserActivity);
		doNothing().when(spyActivity).populateMenuDrawer();

		spyActivity.onRestart();

		verify(spyActivity).populateMenuDrawer();
	}

	@Test
	public void onKeyDownClosesMenuDrawerWhenMenuKeyIsPressed() {
		doReturn(true).when(mockSharedPreferences).getBoolean(
				"remote_control_menu", true);

		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();
		DrawerLayout drawerLayout = (DrawerLayout) movieBrowserActivity
				.findViewById(R.id.drawer_layout);
		LinearLayout linearLayout = (LinearLayout) movieBrowserActivity
				.findViewById(R.id.left_drawer);

		drawerLayout.openDrawer(linearLayout);
		movieBrowserActivity.onKeyDown(KeyEvent.KEYCODE_MENU, null);

		assumeFalse(drawerLayout.isDrawerOpen(linearLayout));
	}

	@Test
	public void onKeyDownOpensMenuDrawerWhenMenuKeyIsPressed() {
		doReturn(true).when(mockSharedPreferences).getBoolean(
				"remote_control_menu", true);

		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();

		DrawerLayout drawerLayout = (DrawerLayout) movieBrowserActivity
				.findViewById(R.id.drawer_layout);
		LinearLayout linearLayout = (LinearLayout) movieBrowserActivity
				.findViewById(R.id.left_drawer);
		drawerLayout.openDrawer(linearLayout);
		movieBrowserActivity.onKeyDown(KeyEvent.KEYCODE_BACK, null);

		assertThat(drawerLayout.isDrawerOpen(linearLayout)).isTrue();
	}

	@Test
	public void onKeyDownBackClosesDrawerWhenDrawerIsOpen() {
		doReturn(false).when(mockSharedPreferences).getBoolean(
				"remote_control_menu", true);

		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();

		movieBrowserActivity.onKeyDown(KeyEvent.KEYCODE_MENU, null);

		DrawerLayout drawerLayout = (DrawerLayout) movieBrowserActivity
				.findViewById(R.id.drawer_layout);
		assumeTrue(drawerLayout.isDrawerOpen(Gravity.LEFT));
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(Robolectric.application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
			MovieBrowserActivity.class, MovieBrowserActivityTest.class })
	public class TestModule {

		@Provides
		@Singleton
		SharedPreferences providesSharedPreferences() {
			return mockSharedPreferences;
		}
	}

}
