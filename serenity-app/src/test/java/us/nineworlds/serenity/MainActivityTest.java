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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
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
import android.content.res.Resources;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.material.appbar.MaterialToolbar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.LinkedList;

import javax.inject.Inject;

import toothpick.config.Module;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.fragments.MainMenuFragment;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(qualifiers = "large")
@LooperMode(LooperMode.Mode.LEGACY)
public class MainActivityTest extends InjectingTest {

    @Mock
    KeyEvent mockKeyEvent;
    @Mock
    SharedPreferences mockSharedPreferences;
    @Mock
    AndroidHelper mockAndroidHelper;
    @Mock
    LinkedList<VideoContentInfo> mockVideoQueue;

    @Inject
    LocalBroadcastManager mockLocalBroadcastManager;

    MainActivity activity;

    @Override
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        super.setUp();

        Robolectric.getBackgroundThreadScheduler().pause();
        Robolectric.getForegroundThreadScheduler().pause();

        activity =
                Robolectric.buildActivity(MainActivity.class).create().start().resume().visible().pause().restart().get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(new MainMenuFragment(), null);
        fragmentTransaction.commit();
    }

    @After
    public void tearDown() throws Exception {
        if (activity != null) {
            activity.finish();
        }
    }

    @Test
    public void testToolBarIsAvailable() {
        MaterialToolbar toolbar = activity.findViewById(R.id.action_toolbar);

        assertThat(toolbar).isNotNull();
    }

    @Test
    public void testAssertThatMainActivityIsCreated() throws Exception {
        assertThat(activity).isNotNull().isNotFinishing();
    }

    @Test
    public void testCreatesMenu() throws Exception {
        assertThat((ImageView) activity.findViewById(R.id.mainGalleryBackground)).isVisible();
    }

    @Test
    public void onActivityResultCallsRecreateWhenResultCodeIsMainMenuPreferenceResultCode() {
        MainActivity spy = spy(activity);
        doNothing().when(spy).recreate();

        spy.onActivityResult(0, 100, null);

        verify(spy).recreate();
    }

    @Test
    public void onActivityResultNeverCallsRecreateWhenResultCodeIsNotMainMenuPreferenceResultCode() {
        MainActivity spy = spy(activity);
        doNothing().when(spy).recreate();

        spy.onActivityResult(0, 101, null);

        verify(spy, never()).recreate();
    }

    @Override
    public void installTestModules() {
        scope.installTestModules(new TestingModule(), new TestModule());
    }

    public class TestModule extends Module {

        public TestModule() {
            bind(SharedPreferences.class).toInstance(mockSharedPreferences);
            bind(AndroidHelper.class).toInstance(mockAndroidHelper);
            bind(LinkedList.class).withName(ForVideoQueue.class).toInstance(mockVideoQueue);
            bind(Resources.class).toInstance(ApplicationProvider.getApplicationContext().getResources());
        }
    }
}
