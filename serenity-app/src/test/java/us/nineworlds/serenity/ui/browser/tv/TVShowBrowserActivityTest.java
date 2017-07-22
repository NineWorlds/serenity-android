package us.nineworlds.serenity.ui.browser.tv;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.ganin.darv.DpadAwareRecyclerView;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Java6Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.*;
import static us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity.SERIES_LAYOUT_GRID;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class)
public class TVShowBrowserActivityTest  extends InjectingTest {

    @Mock
    TVCategoryState mockTVCategoryState;

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    TVShowBrowserPresenter mockTVShowBrowserPresenter;

    TVShowBrowserActivity activity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initMocks(this);
    }

    @After
    public void tearDown() {
        activity.finish();
        activity = null;
    }

    @Test
    public void assertThatFanArtHasExpectedDrawable() {
        demandActivityOnCreate();

        assertThat(activity.fanArt).isNotNull();

        Drawable background = activity.fanArt.getBackground();
        ShadowDrawable shadowDrawable = Shadows.shadowOf(background);
        assertThat(shadowDrawable.getCreatedFromResId()).isEqualTo(R.drawable.tvshows);
    }

    @Test
    public void assertThatTVShowItemCountIsNotNull() {
        demandActivityOnCreate();

        assertThat(activity.tvShowItemCountView).isNotNull().isInstanceOf(TextView.class);
    }

    @Test
    public void assertThatCategoryFilterIsNotNull() {
        demandActivityOnCreate();

        assertThat(activity.categorySpinner).isNotNull().isInstanceOf(Spinner.class);
    }

    @Test
    public void assertThatTVShowBannerGalleryIsNotNull() {
        demandActivityOnCreate();

        assertThat(activity.tvShowBannerGalleryView).isNotNull().isInstanceOf(DpadAwareRecyclerView.class);
    }

    @Test
    public void assertThatSecondaryCategoryFilterIsNotNull() {
        demandActivityOnCreate();

        assertThat(activity.secondarySpinner).isNotNull().isInstanceOf(Spinner.class);
    }

    @Test
    public void assertThatGridViewIsNotNull() {
        doReturn(true).when(mockSharedPreferences).getBoolean(eq(SERIES_LAYOUT_GRID), anyBoolean());

        demandActivityOnCreate();

        assertThat(activity.tvShowGridView).isNotNull().isInstanceOf(DpadAwareRecyclerView.class);
        assertThat(activity.tvShowBannerGalleryView).isNull();
    }

    @Test
    public void displayShowsShowsToastWhenSeriesIsEmpty() {
        demandActivityOnCreate();

        activity.displayShows(Collections.<SeriesContentInfo>emptyList(), null);

        Toast latestToast = ShadowToast.getLatestToast();
        assertThat(latestToast).isNotNull();
    }

    @Test
    public void displayShowsSetsItemCountToExpectedSize() {
        List<SeriesContentInfo> expectedSeries = new ArrayList<>();
        expectedSeries.add(new TVShowSeriesInfo());

        demandActivityOnCreate();

        activity.displayShows(expectedSeries, null);

        assertThat(activity.tvShowItemCountView).hasText("1 Item(s)");
    }

    @Test
    public void restartCallsPopulateMenuDrawer() {
        demandActivityOnCreate();

        TVShowBrowserActivity spy = spy(activity);
        doNothing().when(spy).populateMenuDrawer();

        spy.onRestart();

        verify(spy).populateMenuDrawer();
    }

    @Test
    public void resumeCallsPopulateDrawer() {
        demandActivityOnCreate();

        TVShowBrowserActivity spy = spy(activity);
        doNothing().when(spy).populateMenuDrawer();

        spy.restarted_state = true;
        spy.onResume();

        verify(spy).populateMenuDrawer();
    }

    @Test
    public void resumeRequestsUpdatedTVCategoriesFromPresenter() {
        String expectedKey = RandomStringUtils.randomAlphanumeric(5);
        demandActivityOnCreate();

        TVShowBrowserActivity spy = spy(activity);
        doNothing().when(spy).populateMenuDrawer();

        spy.restarted_state = false;
        spy.key = expectedKey;
        spy.onResume();

        verify(mockTVShowBrowserPresenter).fetchTVCategories(expectedKey);
    }

    private void demandActivityOnCreate() {
        Intent intent = new Intent();
        intent.putExtra("key", "12345");

        activity = Robolectric.buildActivity(TVShowBrowserActivity.class).withIntent(intent).create().get();
    }

    @Override
    public List<Object> getModules() {
        List<Object> modules = new ArrayList<>();

        modules.add(new AndroidModule(application));
        modules.add(new TestingModule());
        modules.add(new TestModule());
        return modules;
    }

    @Module(addsTo = AndroidModule.class,
            includes = SerenityModule.class,
            library = true,
            overrides = true,
            injects = TVShowBrowserActivityTest.class)
    public class TestModule {

        @Provides
        @Singleton
        TVCategoryState providesTVCategoryState() {
            return mockTVCategoryState;
        }

        @Provides
        @Singleton
        SharedPreferences provideSharedPreferences() {
            return mockSharedPreferences;
        }

        @Provides
        TVShowBrowserPresenter providesTVShowBrowserPresenter() {
            return mockTVShowBrowserPresenter;
        }

    }

}