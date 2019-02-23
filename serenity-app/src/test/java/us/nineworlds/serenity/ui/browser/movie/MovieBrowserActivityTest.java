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

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import toothpick.config.Module;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;

import java.util.LinkedList;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Robolectric.getBackgroundThreadScheduler;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;

@RunWith(RobolectricTestRunner.class)
public class MovieBrowserActivityTest extends InjectingTest {

  @Mock SharedPreferences mockSharedPreferences;
  @Mock VideoPlayerIntentUtils mockVideoPlayerIntentUtils;

  MovieBrowserActivity movieBrowserActivity;

  @Override @Before public void setUp() throws Exception {
    getBackgroundThreadScheduler().pause();
    getForegroundThreadScheduler().pause();

    initMocks(this);
    super.setUp();
  }

  @After public void tearDown() {
    if (movieBrowserActivity != null) {
      movieBrowserActivity.finish();
    }
  }

  @Test public void menuOptionsHasGridView() {
    movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();
    ListView drawerList = (ListView) movieBrowserActivity.findViewById(R.id.left_drawer_list);

    assertThat(drawerList).isNotNull();
    ListAdapter adapter = drawerList.getAdapter();
    MenuDrawerItem item = (MenuDrawerItem) adapter.getItem(0);
    assertThat(item.getText()).isEqualTo("Grid View");
  }

  @Test public void menuOptionsHasDetailView() {
    movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();
    ListView drawerList = (ListView) movieBrowserActivity.findViewById(R.id.left_drawer_list);

    assertThat(drawerList).isNotNull();
    ListAdapter adapter = drawerList.getAdapter();
    MenuDrawerItem item = (MenuDrawerItem) adapter.getItem(1);
    assertThat(item.getText()).isEqualTo("Detail View");
  }

  @Test public void menuOptionsHasPlayAllFromQueue() {
    movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();
    ListView drawerList = (ListView) movieBrowserActivity.findViewById(R.id.left_drawer_list);

    assertThat(drawerList).isNotNull();
    ListAdapter adapter = drawerList.getAdapter();
    MenuDrawerItem item = (MenuDrawerItem) adapter.getItem(2);
    assertThat(item.getText()).isEqualTo("Play All from Queue");
  }

  @Test public void onKeyDownClosesMenuDrawerWhenMenuKeyIsPressed() {
    doReturn(true).when(mockSharedPreferences).getBoolean("remote_control_menu", true);

    movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();
    DrawerLayout drawerLayout = (DrawerLayout) movieBrowserActivity.findViewById(R.id.drawer_layout);
    LinearLayout linearLayout = (LinearLayout) movieBrowserActivity.findViewById(R.id.left_drawer);

    drawerLayout.openDrawer(linearLayout);
    movieBrowserActivity.onKeyDown(KeyEvent.KEYCODE_MENU, null);

    assumeFalse(drawerLayout.isDrawerOpen(linearLayout));
  }

  @Test public void onKeyDownOpensMenuDrawerWhenMenuKeyIsPressed() {
    doReturn(true).when(mockSharedPreferences).getBoolean("remote_control_menu", true);

    movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();

    DrawerLayout drawerLayout = (DrawerLayout) movieBrowserActivity.findViewById(R.id.drawer_layout);
    LinearLayout linearLayout = (LinearLayout) movieBrowserActivity.findViewById(R.id.left_drawer);
    drawerLayout.openDrawer(linearLayout);
    movieBrowserActivity.onKeyDown(KeyEvent.KEYCODE_BACK, null);

    assertThat(drawerLayout.isDrawerOpen(linearLayout)).isTrue();
  }

  @Test public void onKeyDownBackClosesDrawerWhenDrawerIsOpen() {
    doReturn(false).when(mockSharedPreferences).getBoolean("remote_control_menu", true);

    movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();

    movieBrowserActivity.onKeyDown(KeyEvent.KEYCODE_MENU, null);

    DrawerLayout drawerLayout = (DrawerLayout) movieBrowserActivity.findViewById(R.id.drawer_layout);
    assumeTrue(drawerLayout.isDrawerOpen(Gravity.LEFT));
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {

    public TestModule() {
      bind(SharedPreferences.class).toInstance(mockSharedPreferences);
      bind(LinkedList.class).withName(ForVideoQueue.class).toInstance(new LinkedList());
      bind(Resources.class).toInstance(ApplicationProvider.getApplicationContext().getResources());
      bind(VideoPlayerIntentUtils.class).toInstance(mockVideoPlayerIntentUtils);
    }
  }

}
