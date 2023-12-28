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

package us.nineworlds.serenity.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import toothpick.config.Module;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
public class MenuMediaContainerTest extends InjectingTest {

  @Mock
  IMediaContainer mockMediaContainer;

  MenuMediaContainer menuMediaContainer;

  List<IDirectory> mockDirectories;

  @Mock
  IDirectory mockDirectory;

  @Override
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    super.setUp();
    mockDirectories = new ArrayList<IDirectory>();

    doReturn(mockDirectories).when(mockMediaContainer).getDirectories();

    menuMediaContainer = new MenuMediaContainer(mockMediaContainer);
  }

  @Test
  public void createMenuItemsDoesNotReturnNull() {
    assertThat(menuMediaContainer.createMenuItems()).isNotNull();
  }

  @Test
  public void createMenuItemsReturnsOneMovieMenuItem() {
    demandMovieMenuItem();

    mockDirectories.add(mockDirectory);

    List<MenuItem> menuItems = menuMediaContainer.createMenuItems();
    assertThat(menuItems).isNotEmpty().hasSize(2);
  }

  @Test
  public void createMenuItemsReturnsSettingsMenuItem() {
    List<MenuItem> menuItems = menuMediaContainer.createMenuItems();
    for (MenuItem item : menuItems) {
      if ("settings".equals(item.getType())) {
        return;
      }
    }
    fail("Did not find Settings menu item");
  }

  private void demandMovieMenuItem() {
    doReturn("movie").when(mockDirectory).getType();
    doReturn("title").when(mockDirectory).getTitle();
    doReturn("1").when(mockDirectory).getKey();
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {
    public TestModule() {
      bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()));
      bind(Resources.class).toInstance(ApplicationProvider.getApplicationContext().getResources());
    }
  }
}
