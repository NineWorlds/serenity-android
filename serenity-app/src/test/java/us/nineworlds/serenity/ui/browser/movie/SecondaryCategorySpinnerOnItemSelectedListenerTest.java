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

package us.nineworlds.serenity.ui.browser.movie;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowApplication;
import toothpick.config.Module;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

import java.util.LinkedList;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.robolectric.Robolectric.*;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.LEGACY)
public class SecondaryCategorySpinnerOnItemSelectedListenerTest extends InjectingTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock
    protected SharedPreferences mockSharedPreferences;

    @Mock
    protected View mockView;

    @Mock
    protected AdapterView mockAdapterView;

    @Mock
    protected SecondaryCategoryInfo mockCategoryInfo;

    @Mock
    protected SerenityMultiViewVideoActivity mockMultiViewVideoActivity;

    @Mock
    protected MovieSelectedCategoryState mockCategoryState;

    @Mock
    protected MoviePosterImageAdapter mockPosterImageAdapter;

    @Mock
    VideoPlayerIntentUtils mockVideoPlayerIntentUtils;

    MovieBrowserActivity movieBrowserActivity;
    protected SecondaryCategorySpinnerOnItemSelectedListener onItemSelectedListener;
    protected SecondaryCategorySpinnerOnItemSelectedListener spyOnItemSelectedListener;

    @Override
    @Before
    public void setUp() throws Exception {
        getBackgroundThreadScheduler().pause();
        getForegroundThreadScheduler().pause();
        super.setUp();
        movieBrowserActivity = buildActivity(MovieBrowserActivity.class).create().get();

//        ShadowApplication shadowApplication = Shadows
//                .shadowOf(application);
//        shadowApplication
//                .declareActionUnbindable("com.google.android.gms.analytics.service.START");

        onItemSelectedListener = new SecondaryCategorySpinnerOnItemSelectedListener(
                "Action", "59", movieBrowserActivity);

        spyOnItemSelectedListener = spy(onItemSelectedListener);
    }

    @After
    public void tearDown() {
        if (movieBrowserActivity != null) {
            movieBrowserActivity.finish();
        }
    }

    @Test
    public void verifyViewAdapterSetSelectionIsNotCalledWhenFirstTimeSwitchIsFalse() {
        spyOnItemSelectedListener.setFirstSelection(false);
        doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
                anyInt());
        doReturn("All").when(mockCategoryInfo).getCategory();

        spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
                0);

        verify(mockAdapterView, times(0)).setSelection(anyInt());
        verify(mockCategoryState, times(0)).getGenreCategory();
    }

    @Test
    public void verifyCategoryStateSetsGenreCategoryWhenGenresDoNotMatch() {
        spyOnItemSelectedListener.setFirstSelection(false);
        doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
                anyInt());
        doReturn("All").when(mockCategoryInfo).getCategory();

        spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
                0);

        verify(mockCategoryState).setGenreCategory("All");
    }

    @Test
    public void verifyThatFirstTimeSwitchIsSetToFalseAfterRetrievingSavedPosition() {
        doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
                anyInt());
        doReturn("All").when(mockCategoryInfo).getCategory();
        doReturn("Action").when(mockCategoryState).getGenreCategory();
        doReturn(0).when(spyOnItemSelectedListener).getSavedInstancePosition(
                any(AdapterView.class));

        spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0, 0);

        assertThat(spyOnItemSelectedListener.isFirstSelection()).isFalse();
    }

    @Test
    public void verifyThatCategoryStateForGenreIsCalledWhenFirstTimeSwitchIsTrue() {
        doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
                anyInt());
        doReturn("All").when(mockCategoryInfo).getCategory();
        doReturn("Action").when(mockCategoryState).getGenreCategory();
        doReturn(0).when(spyOnItemSelectedListener).getSavedInstancePosition(
                any(AdapterView.class));

        spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
                0);

        verify(mockCategoryState).getGenreCategory();
        verify(mockAdapterView).setSelection(anyInt());
        verify(mockAdapterView, times(2)).getItemAtPosition(0);
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

        doReturn("All").when(mockCategoryState).getGenreCategory();

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

        doReturn("All").when(mockCategoryState).getGenreCategory();

        int result = onItemSelectedListener
                .getSavedInstancePosition(mockAdapterView);

        assertThat(result).isEqualTo(0);
    }

    private void demandMockCategory(String value, CategoryInfo mockCategoryInfo) {
        doReturn(value).when(mockCategoryInfo).getCategory();
    }

    @Override
    public void installTestModules() {
        scope.installTestModules(new TestingModule(), new TestModule());
    }

    public class TestModule extends Module {
        public TestModule() {
            bind(SharedPreferences.class).toInstance(mockSharedPreferences);
            bind(MovieSelectedCategoryState.class).toInstance(mockCategoryState);
            bind(LinkedList.class).withName(ForVideoQueue.class).toInstance(new LinkedList());
            bind(Resources.class).toInstance(ApplicationProvider.getApplicationContext().getResources());
            bind(VideoPlayerIntentUtils.class).toInstance(mockVideoPlayerIntentUtils);
        }
    }
}
