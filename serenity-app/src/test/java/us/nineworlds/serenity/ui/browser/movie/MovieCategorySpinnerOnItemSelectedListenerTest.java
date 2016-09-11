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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import net.ganin.darv.DpadAwareRecyclerView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GridVideoOnItemLongClickListener;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.jess.ui.TwoWayGridView;

import dagger.Module;
import dagger.Provides;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class MovieCategorySpinnerOnItemSelectedListenerTest extends
InjectingTest {

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

	@Mock
	protected SerenityGallery mockGallery;

	MovieBrowserActivity movieBrowserActivity;
	protected MovieCategorySpinnerOnItemSelectedListener onItemSelectedListener;
	protected MovieCategorySpinnerOnItemSelectedListener spyOnItemSelectedListener;

	@Override
	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundScheduler().pause();
		Robolectric.getUiThreadScheduler().pause();
		MockitoAnnotations.initMocks(this);
		super.setUp();

		ShadowApplication shadowApplication = Robolectric
				.shadowOf(Robolectric.application);
		shadowApplication
		.declareActionUnbindable("com.google.android.gms.analytics.service.START");

		onItemSelectedListener = new MovieCategorySpinnerOnItemSelectedListener(
				"All", "59", mockMultiViewVideoActivity);

		spyOnItemSelectedListener = spy(onItemSelectedListener);
	}

	@Test
	public void verifyThatGalleryRefreshIsCalledWhenGridViewIsNotActiveAndFirstTimeIsTrue() {
		doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
				anyInt());
		doReturn("All").when(mockCategoryInfo).getCategory();

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		demandAdapters();

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(mockView).getContext();
		verify(spyOnItemSelectedListener)
		.refreshGallery(mockPosterImageAdapter);

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
		doNothing().when(spyOnItemSelectedListener).refreshGridView(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(mockAdapterView, times(0)).setSelection(anyInt());
		verify(mockCategoryState, times(0)).getGenreCategory();
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

		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGridView(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(mockCategoryState).setCategory("action");
	}

	@Test
	public void verifyRefreshGridViewAdapterIsCalledWhenGridViewIsActive() {
		spyOnItemSelectedListener.setFirstSelection(true);
		doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
				anyInt());
		doReturn("All").when(mockCategoryInfo).getCategory();

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(true).when(mockMultiViewVideoActivity).isGridViewActive();
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGridView(
				any(AbstractPosterImageGalleryAdapter.class));

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(spyOnItemSelectedListener).refreshGridView(
				mockPosterImageAdapter);
	}

	@Test
	public void verifyGetPosterAdapterIsNotCalledWhenSelectedAndItemCategoryAreTheSame() {
		spyOnItemSelectedListener.setFirstSelection(false);
		doReturn(mockCategoryInfo).when(mockAdapterView).getItemAtPosition(
				anyInt());
		doReturn("Action").when(mockCategoryInfo).getCategory();

		doReturn(mockMultiViewVideoActivity).when(mockView).getContext();
		doReturn(mockSecondarySpinner).when(mockMultiViewVideoActivity)
		.findViewById(R.id.categoryFilter2);
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(SecondaryCategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).createGallery(
				any(CategoryInfo.class));

		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGridView(
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
		doNothing().when(spyOnItemSelectedListener).refreshGridView(
				any(AbstractPosterImageGalleryAdapter.class));

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

		demandAdapters();

		spyOnItemSelectedListener.onItemSelected(mockAdapterView, mockView, 0,
				0);

		verify(mockCategoryInfo, times(2)).getCategory();
		verify(mockAdapterView).setSelection(anyInt());
		verify(spyOnItemSelectedListener)
				.createGallery(any(CategoryInfo.class));
	}

	@Test
	public void refreshGallerySetsExpectedAdapter() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();

		spyOnItemSelectedListener
		.setMultiViewVideoActivity(movieBrowserActivity);

		spyOnItemSelectedListener.findViews();
		spyOnItemSelectedListener.refreshGallery(mockPosterImageAdapter);

		DpadAwareRecyclerView gallery = (DpadAwareRecyclerView) movieBrowserActivity
				.findViewById(R.id.moviePosterGallery);
		assertThat(gallery.getAdapter()).isEqualTo(mockPosterImageAdapter);
	}

	@Test
	public void refreshGallerySetsOnItemClickListener() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().start()
				.get();

		spyOnItemSelectedListener
		.setMultiViewVideoActivity(movieBrowserActivity);

		spyOnItemSelectedListener.findViews();
		spyOnItemSelectedListener.refreshGallery(mockPosterImageAdapter);

		SerenityGallery gallery = (SerenityGallery) movieBrowserActivity
				.findViewById(R.id.moviePosterGallery);
		assertThat(gallery.getOnItemClickListener()).isInstanceOf(
				GalleryVideoOnItemClickListener.class);
	}

	@Test
	public void refreshGallerySetsOnItemLongClickListener() {
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().start()
				.get();

		spyOnItemSelectedListener
		.setMultiViewVideoActivity(movieBrowserActivity);

		spyOnItemSelectedListener.findViews();
		spyOnItemSelectedListener.refreshGallery(mockPosterImageAdapter);

		SerenityGallery gallery = (SerenityGallery) movieBrowserActivity
				.findViewById(R.id.moviePosterGallery);
		assertThat(gallery.getOnItemLongClickListener()).isInstanceOf(
				GalleryVideoOnItemLongClickListener.class);
	}

	@Test
	public void refreshGridSetsOnItemLongClickListener() {
		doReturn(true).when(mockSharedPreferences).getBoolean(
				"movie_layout_grid", false);
		doReturn(1).when(mockPosterImageAdapter).getItemCount();

		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().start()
				.get();
		spyOnItemSelectedListener
		.setMultiViewVideoActivity(movieBrowserActivity);

		spyOnItemSelectedListener.findViews();

		spyOnItemSelectedListener.refreshGridView(mockPosterImageAdapter);

		TwoWayGridView gridView = (TwoWayGridView) movieBrowserActivity
				.findViewById(R.id.movieGridView);
		assertThat(gridView.getOnItemLongClickListener()).isInstanceOf(
				GridVideoOnItemLongClickListener.class);
	}

	@Test
	public void refreshGridSetsOnItemClickListener() {
		doReturn(true).when(mockSharedPreferences).getBoolean(
				"movie_layout_grid", false);
		doReturn(1).when(mockPosterImageAdapter).getItemCount();

		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().start()
				.get();
		spyOnItemSelectedListener
		.setMultiViewVideoActivity(movieBrowserActivity);

		spyOnItemSelectedListener.findViews();

		spyOnItemSelectedListener.refreshGridView(mockPosterImageAdapter);

		TwoWayGridView gridView = (TwoWayGridView) movieBrowserActivity
				.findViewById(R.id.movieGridView);
		assertThat(gridView.getOnItemClickListener()).isInstanceOf(
				GridVideoOnItemClickListener.class);
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

	protected void demandAdapters() {
		doReturn(mockPosterImageAdapter).when(spyOnItemSelectedListener)
		.getPosterImageAdapter(any(CategoryInfo.class));
		doNothing().when(spyOnItemSelectedListener).refreshGallery(
				any(AbstractPosterImageGalleryAdapter.class));
		doNothing().when(spyOnItemSelectedListener).refreshGridView(
				any(AbstractPosterImageGalleryAdapter.class));
	}

	private void demandMockCategory(String value, CategoryInfo mockCategoryInfo) {
		doReturn(value).when(mockCategoryInfo).getCategory();
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(Robolectric.application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
		MovieCategorySpinnerOnItemSelectedListener.class,
		MovieCategorySpinnerOnItemSelectedListenerTest.class,
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
