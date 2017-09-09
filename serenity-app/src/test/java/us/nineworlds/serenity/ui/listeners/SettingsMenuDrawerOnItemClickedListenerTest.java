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

package us.nineworlds.serenity.ui.listeners;

import android.content.Intent;
import android.content.MutableContextWrapper;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.ui.activity.SerenityDrawerLayoutActivity;
import us.nineworlds.serenity.widgets.DrawerLayout;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SettingsMenuDrawerOnItemClickedListenerTest {

  @Mock DrawerLayout mockDrawerLayout;
  @Mock View mockView;
  @Mock SerenityDrawerLayoutActivity mockSerenityDrawerLayoutActivity;

  SettingsMenuDrawerOnItemClickedListener onItemClickedListener;

  @Before public void setUp() {
    initMocks(this);
    onItemClickedListener = new SettingsMenuDrawerOnItemClickedListener(mockDrawerLayout);
  }

  @Test public void onClickStartsSettingActivity() {
    doNothing().when(mockSerenityDrawerLayoutActivity).startActivity(any(Intent.class));
    doReturn(mockSerenityDrawerLayoutActivity).when(mockView).getContext();

    onItemClickedListener.onClick(mockView);

    verify(mockSerenityDrawerLayoutActivity).startActivity(any(Intent.class));
    verify(mockView).getContext();
  }

  @Test public void onClickClosesMenuDrawer() {
    doNothing().when(mockSerenityDrawerLayoutActivity).startActivity(any(Intent.class));
    doReturn(mockSerenityDrawerLayoutActivity).when(mockView).getContext();
    doNothing().when(mockDrawerLayout).closeDrawers();

    onItemClickedListener.onClick(mockView);

    verify(mockDrawerLayout).closeDrawers();
    verify(mockView).getContext();
  }

  @Test public void onClickDiscoversActivityFromContextWrapper() {
    MutableContextWrapper context = new MutableContextWrapper(mockSerenityDrawerLayoutActivity);

    doNothing().when(mockSerenityDrawerLayoutActivity).startActivity(any(Intent.class));
    doReturn(context).when(mockView).getContext();

    onItemClickedListener.onClick(mockView);

    verify(mockSerenityDrawerLayoutActivity).startActivity(any(Intent.class));
    verify(mockView).getContext();
  }
}
