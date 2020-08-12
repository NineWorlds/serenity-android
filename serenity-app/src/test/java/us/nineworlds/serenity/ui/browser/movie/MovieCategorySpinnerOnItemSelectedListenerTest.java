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

import android.app.Application;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowApplication;
import toothpick.config.Module;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.LEGACY)
public class MovieCategorySpinnerOnItemSelectedListenerTest extends InjectingTest {

  @Mock
  protected SharedPreferences mockSharedPreferences;

  @Mock
  protected View mockView;

  @Mock
  protected Spinner mockSecondarySpinner;

  @Mock
  protected AdapterView mockAdapterView;

  @Mock
  protected CategoryInfo mockCategoryInfo;

  @Mock
  protected SerenityMultiViewVideoActivity mockMultiViewVideoActivity;

  @Mock
  protected MovieSelectedCategoryState mockCategoryState;

  @Mock
  protected MoviePosterImageAdapter mockPosterImageAdapter;

  @Mock protected RecyclerView mockPosterGallery;

  protected MovieCategorySpinnerOnItemSelectedListener onItemSelectedListener;
  protected MovieCategorySpinnerOnItemSelectedListener spyOnItemSelectedListener;

  @Override
  @Before
  public void setUp() throws Exception {
    Robolectric.getBackgroundThreadScheduler().pause();
    Robolectric.getForegroundThreadScheduler().pause();
    initMocks(this);
    super.setUp();

//    ShadowApplication shadowApplication = shadowOf((Application) ApplicationProvider.getApplicationContext());
//    shadowApplication
//        .declareActionUnbindable("com.google.android.gms.analytics.service.START");

    onItemSelectedListener = new MovieCategorySpinnerOnItemSelectedListener(
        "All", "59", mockMultiViewVideoActivity);

    spyOnItemSelectedListener = spy(onItemSelectedListener);
  }

  @Test
  public void verifyCategoryStateSetsGCategoryWhenGenresDoNotMatch() {
    spyOnItemSelectedListener.setFirstSelection(false);
    doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
        anyInt());
    doReturn("action").when(mockCategoryInfo).getCategory();

    doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
    doReturn(mockSecondarySpinner).when(mockMultiViewVideoActivity)
        .findViewById(R.id.categoryFilter2);

    spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
        0);

    verify(mockCategoryState).setCategory("action");
  }

  @Test
  public void verifyThatFirstTimeSwitchIsSetToFalseAfterRetrievingSavedPosition() {
    doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
        anyInt());
    doReturn("All").when(mockCategoryInfo).getCategory();
    doReturn("Action").when(mockCategoryState).getGenreCategory();
    doReturn(0).when(spyOnItemSelectedListener).getSavedInstancePosition(
        any(AdapterView.class));

    doReturn(mockMultiViewVideoActivity).when(mockView).getContext();

    spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
        0);

    assertThat(spyOnItemSelectedListener.isFirstSelection()).isFalse();
  }

  @Test
  public void verifyCategorySetFromFilterTheFirstTime() {
    doReturn("all").when(mockSharedPreferences).getString(
        "serenity_category_filter", "all");
    doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
        anyInt());
    doReturn(1).when(mockAdapterView).getCount();

    doReturn("all").when(mockCategoryInfo).getCategory();
    doReturn("action").when(mockCategoryState).getGenreCategory();
    doReturn(0).when(spyOnItemSelectedListener).getSavedInstancePosition(
        any(AdapterView.class));

    doReturn(mockMultiViewVideoActivity).when(mockView).getContext();

    spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
        0);

    verify(mockCategoryInfo, times(2)).getCategory();
    verify(mockAdapterView).setSelection(anyInt());
    verify(spyOnItemSelectedListener)
        .createGallery(any(CategoryInfo.class));
  }

  @Test
  public void returnThePositionForTheSavedCategoryState() {

    doReturn(2).when(mockAdapterView).getCount();

    CategoryInfo mockCategoryInfo1 = Mockito.mock(CategoryInfo.class);
    CategoryInfo mockCategoryInfo2 = Mockito.mock(CategoryInfo.class);

    doReturn(mockCategoryInfo1).when(mockAdapterView).getItemAtPosition(0);
    doReturn(mockCategoryInfo2).when(mockAdapterView).getItemAtPosition(1);

    demandMockCategory("Action", mockCategoryInfo1);
    demandMockCategory("All", mockCategoryInfo2);

    doReturn("All").when(mockCategoryState).getCategory();

    int result = onItemSelectedListener
        .getSavedInstancePosition(mockAdapterView);

    assertThat(result).isEqualTo(1);
  }

  @Test
  public void returnTheFirstPositionWhenCategoryStateNotFound() {

    doReturn(2).when(mockAdapterView).getCount();

    CategoryInfo mockCategoryInfo1 = Mockito.mock(CategoryInfo.class);
    CategoryInfo mockCategoryInfo2 = Mockito.mock(CategoryInfo.class);

    doReturn(mockCategoryInfo1).when(mockAdapterView).getItemAtPosition(0);
    doReturn(mockCategoryInfo2).when(mockAdapterView).getItemAtPosition(1);

    demandMockCategory("Action", mockCategoryInfo1);
    demandMockCategory("Alpha", mockCategoryInfo2);

    doReturn("All").when(mockCategoryState).getCategory();

    int result = onItemSelectedListener
        .getSavedInstancePosition(mockAdapterView);

    assertThat(result).isEqualTo(0);
  }

  private void demandMockCategory(String value, CategoryInfo mockCategoryInfo) {
    doReturn(value).when(mockCategoryInfo).getCategory();
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {

    public TestModule() {
      bind(SharedPreferences.class).toInstance(mockSharedPreferences);
      bind(MovieSelectedCategoryState.class).toInstance(mockCategoryState);
    }
  }
}
