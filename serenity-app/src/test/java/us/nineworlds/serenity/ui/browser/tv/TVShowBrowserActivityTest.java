package us.nineworlds.serenity.ui.browser.tv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.com.tvrecyclerview.TvRecyclerView;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowToast;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.test.ShadowArrayAdapter;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;
import static us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity.SERIES_LAYOUT_GRID;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = ShadowArrayAdapter.class)
public class TVShowBrowserActivityTest extends InjectingTest {

  @Mock TVCategoryState mockTVCategoryState;

  @Mock SharedPreferences mockSharedPreferences;

  @Mock TVShowBrowserPresenter mockTVShowBrowserPresenter;

  TVShowBrowserActivity activity;

  @Override @Before public void setUp() throws Exception {
    super.setUp();
    initMocks(this);
  }

  @After public void tearDown() {
    activity.finish();
    activity = null;
  }

  @Test public void assertThatFanArtHasExpectedDrawable() {
    demandActivityOnCreate();

    assertThat(activity.fanArt).isNotNull();

    Drawable background = activity.fanArt.getBackground();
    ShadowDrawable shadowDrawable = Shadows.shadowOf(background);
    assertThat(shadowDrawable.getCreatedFromResId()).isEqualTo(R.drawable.tvshows);
  }

  @Test public void assertThatTVShowItemCountIsNotNull() {
    demandActivityOnCreate();

    assertThat(activity.tvShowItemCountView).isNotNull().isInstanceOf(TextView.class);
  }

  @Test public void assertThatCategoryFilterIsNotNull() {
    demandActivityOnCreate();

    assertThat(activity.categorySpinner).isNotNull().isInstanceOf(Spinner.class);
  }

  @Test public void assertThatTVShowBannerGalleryIsNotNull() {
    demandActivityOnCreate();

    assertThat(activity.tvShowBannerGalleryView).isNotNull().isInstanceOf(TvRecyclerView.class);
  }

  @Test public void assertThatSecondaryCategoryFilterIsNotNull() {
    demandActivityOnCreate();

    assertThat(activity.secondarySpinner).isNotNull().isInstanceOf(Spinner.class);
  }

  @Test public void assertThatGridViewIsNotNull() {
    doReturn(true).when(mockSharedPreferences).getBoolean(eq(SERIES_LAYOUT_GRID), anyBoolean());

    demandActivityOnCreate();

    assertThat(activity.tvShowGridView).isNotNull().isInstanceOf(TvRecyclerView.class);
    assertThat(activity.tvShowBannerGalleryView).isNull();
  }

  @Test public void displayShowsShowsToastWhenSeriesIsEmpty() {
    demandActivityOnCreate();

    activity.displayShows(Collections.<SeriesContentInfo>emptyList(), null);

    Toast latestToast = ShadowToast.getLatestToast();
    assertThat(latestToast).isNotNull();
  }

  @Test public void displayShowsSetsItemCountToExpectedSize() {
    List<SeriesContentInfo> expectedSeries = new ArrayList<>();
    expectedSeries.add(new TVShowSeriesInfo());

    demandActivityOnCreate();

    activity.displayShows(expectedSeries, null);

    assertThat(activity.tvShowItemCountView).hasText("1 Item(s)");
  }

  @Test public void restartCallsPopulateMenuDrawer() {
    demandActivityOnCreate();

    TVShowBrowserActivity spy = spy(activity);
    doNothing().when(spy).populateMenuDrawer();

    spy.onRestart();

    verify(spy).populateMenuDrawer();
  }

  @Test public void resumeCallsPopulateDrawer() {
    demandActivityOnCreate();

    TVShowBrowserActivity spy = spy(activity);
    doNothing().when(spy).populateMenuDrawer();

    spy.restarted_state = true;
    spy.onResume();

    verify(spy).populateMenuDrawer();
  }

  @Test public void resumeRequestsUpdatedTVCategoriesFromPresenter() {
    String expectedKey = RandomStringUtils.randomAlphanumeric(5);
    demandActivityOnCreate();

    TVShowBrowserActivity spy = spy(activity);
    doNothing().when(spy).populateMenuDrawer();

    spy.restarted_state = false;
    spy.key = expectedKey;
    spy.onResume();

    verify(mockTVShowBrowserPresenter).fetchTVCategories(expectedKey);
  }

  @Test public void requestUpdatedVideosCallsFetchTVCategoriesPreseneter() {
    String expectedKey = RandomStringUtils.randomAlphanumeric(10);
    String expectedCategory = RandomStringUtils.randomAlphanumeric(5);
    doNothing().when(mockTVShowBrowserPresenter).fetchTVShows(anyString(), anyString());
    demandActivityOnCreate();

    activity.requestUpdatedVideos(expectedKey, expectedCategory);

    verify(mockTVShowBrowserPresenter).fetchTVShows(expectedKey, expectedCategory);
  }

  @Test public void updateCategoriesSetsDropDownViewResourceOnSpinner() throws Exception {
    demandActivityOnCreate();

    List<CategoryInfo> expectedCategories = Collections.singletonList(new CategoryInfo());
    activity.updateCategories(expectedCategories);

    assertThat(activity.categorySpinner.getAdapter()).isNotNull();
    ArrayAdapter<CategoryInfo> actualAdapter = (ArrayAdapter<CategoryInfo>) activity.categorySpinner.getAdapter();
    ShadowArrayAdapter shadowArrayAdapter = Shadow.extract(actualAdapter);
    assertThat(shadowArrayAdapter.getResourceId()).isEqualTo(R.layout.serenity_spinner_textview);
    assertThat(shadowArrayAdapter.getDropDownViewResourceId()).isEqualTo(R.layout.serenity_spinner_textview_dropdown);
  }

  @Test public void updateCategoriesMakesCategorySpinnerVisible() {
    demandActivityOnCreate();

    activity.categorySpinner.setVisibility(View.GONE);

    List<CategoryInfo> expectedCategories = Collections.singletonList(new CategoryInfo());
    activity.updateCategories(expectedCategories);

    assertThat(activity.categorySpinner).isVisible();
  }

  @Test public void onCreateSetsOnKeyDownDelegate() {
    demandActivityOnCreate();

    assertThat(activity.onKeyDownDelegate).isNotNull().isInstanceOf(OnKeyDownDelegate.class);
  }

  @Test public void populateSecondaryCategoriesSetsSecondarySpinnerVisible() {
    demandActivityOnCreate();
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    List<SecondaryCategoryInfo> expectedCategories = Collections.singletonList(new SecondaryCategoryInfo());
    activity.populateSecondaryCategories(expectedCategories, selectedCategory);

    assertThat(activity.secondarySpinner).isVisible();
    assertThat(activity.categorySpinner).isVisible();
  }

  @Test public void populateSecondaryCategoriesSetsSecondarySetsDropDownViewResourceId() {
    demandActivityOnCreate();
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    List<SecondaryCategoryInfo> expectedCategories = Collections.singletonList(new SecondaryCategoryInfo());
    activity.populateSecondaryCategories(expectedCategories, selectedCategory);

    ArrayAdapter<SecondaryCategoryInfo> adapter =
        (ArrayAdapter<SecondaryCategoryInfo>) activity.secondarySpinner.getAdapter();
    ShadowArrayAdapter shadowArrayAdapter = Shadow.extract(adapter);

    assertThat(shadowArrayAdapter.getDropDownViewResourceId()).isEqualTo(R.layout.serenity_spinner_textview_dropdown);
  }

  @Test public void populateSecondaryCategoriesSetsSecondaryExpectedOnItemSelectedListener() {
    demandActivityOnCreate();
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    List<SecondaryCategoryInfo> expectedCategories = Collections.singletonList(new SecondaryCategoryInfo());
    activity.populateSecondaryCategories(expectedCategories, selectedCategory);

    assertThat(activity.secondarySpinner.getOnItemSelectedListener()).isInstanceOf(
        TVSecondaryCategorySpinnerOnItemSelectedListener.class);
  }

  @Test public void populateSecondaryCategoriesDisplaysToastWhenCategoriesIsNull() {
    demandActivityOnCreate();
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    activity.populateSecondaryCategories(null, selectedCategory);

    assertThat(ShadowToast.getLatestToast()).isNotNull();
    assertThat(ShadowToast.getTextOfLatestToast()).contains("No Entries available for category");
  }

  @Test public void populateSecondaryCategoriesDisplaysToastWhenCategoriesIsEmpty() {
    demandActivityOnCreate();
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    activity.populateSecondaryCategories(Collections.<SecondaryCategoryInfo>emptyList(), selectedCategory);

    assertThat(ShadowToast.getLatestToast()).isNotNull();
    assertThat(ShadowToast.getTextOfLatestToast()).contains("No Entries available for category");
  }

  @Test public void onKeyDownCallsDelegate() {
    demandActivityOnCreate();
    int expectedKeyCode = 10;
    KeyEvent expectedKeyEvent = Mockito.mock(KeyEvent.class);

    OnKeyDownDelegate mockOnKeyDownDelegate = Mockito.mock(OnKeyDownDelegate.class);
    activity.onKeyDownDelegate = mockOnKeyDownDelegate;

    activity.onKeyDown(expectedKeyCode, expectedKeyEvent);

    verify(mockOnKeyDownDelegate).onKeyDown(expectedKeyCode, expectedKeyEvent);
  }

  private void demandActivityOnCreate() {
    Intent intent = new Intent();
    intent.putExtra("key", "12345");

    activity = Robolectric.buildActivity(TVShowBrowserActivity.class, intent).create().get();
  }

  @Override public List<Object> getModules() {
    List<Object> modules = new ArrayList<>();

    modules.add(new AndroidModule(application));
    modules.add(new TestingModule());
    modules.add(new TestModule());
    return modules;
  }

  @Module(addsTo = AndroidModule.class, includes = SerenityModule.class, library = true, overrides = true, injects = TVShowBrowserActivityTest.class)
  public class TestModule {

    @Provides @Singleton TVCategoryState providesTVCategoryState() {
      return mockTVCategoryState;
    }

    @Provides @Singleton SharedPreferences provideSharedPreferences() {
      return mockSharedPreferences;
    }

    @Provides TVShowBrowserPresenter providesTVShowBrowserPresenter() {
      return mockTVShowBrowserPresenter;
    }
  }
}