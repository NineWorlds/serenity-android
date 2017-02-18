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
import android.view.View;
import android.widget.AdapterView;
import dagger.Module;
import dagger.Provides;
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
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.widgets.SerenityGallery;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SecondaryCategorySpinnerOnItemSelectedListenerTest extends
		InjectingTest {

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
	protected SerenityGallery mockGallery;

	MovieBrowserActivity movieBrowserActivity = Robolectric
			.buildActivity(MovieBrowserActivity.class).create().get();

	protected SecondaryCategorySpinnerOnItemSelectedListener onItemSelectedListener;
	protected SecondaryCategorySpinnerOnItemSelectedListener spyOnItemSelectedListener;

	@Override
	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundThreadScheduler().pause();
		Robolectric.getForegroundThreadScheduler().pause();
		MockitoAnnotations.initMocks(this);
		super.setUp();

		ShadowApplication shadowApplication = Shadows
				.shadowOf(application);
		shadowApplication
				.declareActionUnbindable("com.google.android.gms.analytics.service.START");

		onItemSelectedListener = new SecondaryCategorySpinnerOnItemSelectedListener(
				"Action", "59", movieBrowserActivity);

		spyOnItemSelectedListener = spy(onItemSelectedListener);
	}

	@Test
	public void verifyViewAdapterSetSelectionIsNotCalledWhenFirstTimeSwitchIsFalse() {
		spyOnItemSelectedListener.setFirstSelection(false);
		doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
				anyInt());
		doReturn("All").when(mockCategoryInfo).getCategory();

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));

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

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(mockCategoryState).setGenreCategory("All");
	}

	@Test
	public void verifyRefreshGridViewAdapterIsCalledWhenGridViewIsActive() {
		spyOnItemSelectedListener.setFirstSelection(false);
		doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
				anyInt());
		doReturn("All").when(mockCategoryInfo).getCategory();

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(true).when(mockMultiViewVideoActivity).isGridViewActive();
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
				.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(spyOnItemSelectedListener).refreshGallery(
				mockPosterImageAdapter);
	}

	@Test
	public void verifyGetPosterAdapterIsNotCalledWhenSelectedAndItemCategoryAreTheSame() {
		spyOnItemSelectedListener.setFirstSelection(false);
		doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
				anyInt());
		doReturn("Action").when(mockCategoryInfo).getCategory();

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(spyOnItemSelectedListener, times(0)).getPosterImageAdapter(
				any(SecondaryCategoryInfo.class));
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
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

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

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));

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
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
			SecondaryCategorySpinnerOnItemSelectedListener.class,
			SecondaryCategorySpinnerOnItemSelectedListenerTest.class,
			MovieBrowserActivity.class })
	public class TestModule {

		@Provides
		@Singleton
		SharedPreferences providesSharedPreferences() {
			return mockSharedPreferences;
		}

		@Provides
		@Singleton
		MovieSelectedCategoryState providesMovieSelectedCategoryState() {
			return mockCategoryState;
		}

	}

}
