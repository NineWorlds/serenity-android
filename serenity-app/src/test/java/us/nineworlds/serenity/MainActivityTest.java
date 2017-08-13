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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.ButterKnife;
import com.birbit.android.jobqueue.JobManager;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import net.ganin.darv.DpadAwareRecyclerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.fragments.MainMenuFragment;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.widgets.DrawerLayout;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, qualifiers = "large")
public class MainActivityTest extends InjectingTest {

  MainActivity activity;

  @Mock JobManager mockJobManager;

  @Mock KeyEvent mockKeyEvent;

  @Mock SharedPreferences mockSharedPreferences;

  @Mock PlexappFactory mockPlexAppFactory;

  MainMenuFragment fragment;

  @Override @Before public void setUp() throws Exception {
    super.setUp();

    initMocks(this);

    Robolectric.getBackgroundThreadScheduler().pause();
    Robolectric.getForegroundThreadScheduler().pause();

    try {
      activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().visible().get();
    } catch (NullPointerException ex) {
      activity = Robolectric.buildActivity(MainActivity.class).create().start().visible().get();
    }

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(new MainMenuFragment(), null);
    fragmentTransaction.commit();
  }

  @After public void tearDown() throws Exception {
    if (activity != null) {
      activity.finish();
    }
  }

  @Test public void testAssertThatMainActivityIsCreated() throws Exception {
    assertThat(activity).isNotNull().isNotFinishing();
  }

  @Test public void testCreatesMenu() throws Exception {
    assertThat(activity.findViewById(R.id.mainGalleryBackground)).isVisible();
  }

  @Test public void onKeyDownOpensMenuDrawerWhenKeycodeMenu() {
    DrawerLayout drawerLayout = ButterKnife.findById(activity, R.id.drawer_layout);
    ListView listView = ButterKnife.findById(activity, R.id.left_drawer_list);
    drawerLayout.closeDrawers();
    doReturn(true).when(mockSharedPreferences).getBoolean("remote_control_menu", true);

    assertThat(activity.onKeyDown(KeyEvent.KEYCODE_MENU, mockKeyEvent)).isTrue();
    assertThat(listView).hasFocus();

    verify(mockSharedPreferences).getBoolean("remote_control_menu", true);
  }

  @Test public void onKeyDownClosesOpenDrawerWhenKeycodeMenu() {
    DrawerLayout drawerLayout = ButterKnife.findById(activity, R.id.drawer_layout);
    LinearLayout leftDrawer = ButterKnife.findById(activity, R.id.left_drawer);
    DpadAwareRecyclerView mainMenu = ButterKnife.findById(activity, (R.id.mainGalleryMenu));

    drawerLayout.openDrawer(leftDrawer);

    doReturn(true).when(mockSharedPreferences).getBoolean("remote_control_menu", true);

    assertThat(activity.onKeyDown(KeyEvent.KEYCODE_MENU, mockKeyEvent)).isTrue();
    assertThat(mainMenu).hasFocus();

    verify(mockSharedPreferences).getBoolean("remote_control_menu", true);
  }

  @Test public void onKeyDownClosesOpenDrawerWhenKeycodeBackIsPressed() {
    DrawerLayout drawerLayout = ButterKnife.findById(activity, R.id.drawer_layout);
    LinearLayout leftDrawer = ButterKnife.findById(activity, R.id.left_drawer);
    DpadAwareRecyclerView mainMenu = ButterKnife.findById(activity, (R.id.mainGalleryMenu));

    drawerLayout.openDrawer(leftDrawer);

    doReturn(false).when(mockSharedPreferences).getBoolean("remote_control_menu", true);

    assertThat(activity.onKeyDown(KeyEvent.KEYCODE_BACK, mockKeyEvent)).isTrue();
    assertThat(mainMenu).hasFocus();

    verify(mockSharedPreferences).getBoolean("remote_control_menu", true);
  }

  @Override public List<Object> getModules() {
    List<Object> modules = new ArrayList<Object>();
    modules.add(new AndroidModule(RuntimeEnvironment.application));
    modules.add(new TestModule());
    return modules;
  }

  @Module(addsTo = AndroidModule.class, includes = SerenityModule.class, overrides = true, injects = {
      MainActivity.class, MainActivityTest.class
  })
  public class TestModule {

    @Provides @Singleton JobManager providesJobManager() {
      return mockJobManager;
    }

    @Provides @Singleton SharedPreferences providesPreferences() {
      return mockSharedPreferences;
    }

    @Provides @Singleton PlexappFactory providesPlexappFactory() {
      return mockPlexAppFactory;
    }
  }
}
