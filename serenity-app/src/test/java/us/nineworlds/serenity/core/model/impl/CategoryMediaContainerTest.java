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
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.emby.model.Directory;
import us.nineworlds.serenity.emby.model.MediaContainer;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.testrunner.PlainAndroidRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(PlainAndroidRunner.class)
public class CategoryMediaContainerTest extends InjectingTest {

  @Mock
  MediaContainer mockMediaContainer;

  List<Directory> plexDirectories;

  CategoryMediaContainer categoryMediaContainer;

  @Override
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    super.setUp();

    categoryMediaContainer = new CategoryMediaContainer(mockMediaContainer);

    plexDirectories = new ArrayList<Directory>();
    for (int i = 0; i < 3; i++) {
      Directory directory = new Directory();
      directory.setKey(UUID.randomUUID().toString());
      directory.setTitle("Title No: " + i);
      if ((i & 1) == 0) {
        directory.setSecondary(1);
      } else {
        directory.setSecondary(0);
      }
      plexDirectories.add(directory);
    }
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }

  @Test
  public void createCatagoriesDoesNotReturnAnEmptyCategoriesList() {
    doReturn(plexDirectories).when(mockMediaContainer).getDirectories();
    List<CategoryInfo> result = categoryMediaContainer.createCategories();
    assertThat(result).isNotEmpty();
  }

  @Test
  public void createCatagoriesCreatesAValidCategory() {
    doReturn(plexDirectories).when(mockMediaContainer).getDirectories();
    List<CategoryInfo> result = categoryMediaContainer.createCategories();
    CategoryInfo category = result.get(0);
    assertThat(category.getCategory()).isNotEmpty().isEqualTo(
        plexDirectories.get(0).getKey());
  }

  @Test
  public void createCatagoriesCreatesExpectedDetail() {
    doReturn(plexDirectories).when(mockMediaContainer).getDirectories();
    List<CategoryInfo> result = categoryMediaContainer.createCategories();
    CategoryInfo category = result.get(0);
    assertThat(category.getCategoryDetail()).isNotEmpty().isEqualTo(
        plexDirectories.get(0).getTitle());
  }

  @Test
  public void createCatagoriesCreatesExpectedLevelForASingleLevelEntry() {
    doReturn(plexDirectories).when(mockMediaContainer).getDirectories();
    List<CategoryInfo> result = categoryMediaContainer.createCategories();
    CategoryInfo category = result.get(0);
    assertThat(category.getLevel()).isEqualTo(
        plexDirectories.get(0).getSecondary());
  }
}
