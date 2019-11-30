package us.nineworlds.serenity.ui.browser.tv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowToast;
import toothpick.config.Module;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.test.ShadowArrayAdapter;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = ShadowArrayAdapter.class)
public class TVShowBrowserActivityTest extends InjectingTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock TVCategoryState mockTVCategoryState;
  @Mock SharedPreferences mockSharedPreferences;
  @Mock TVShowBrowserPresenter mockTVShowBrowserPresenter;
  @Mock VideoPlayerIntentUtils mockVideoPlayerIntentUtils;

  TVShowBrowserActivity activity;

  @Override @Before public void setUp() throws Exception {
    super.setUp();
    demandActivityOnCreate();
  }

  @After public void tearDown() {
    activity.finish();
  }

  @Test public void assertThatFanArtHasExpectedDrawable() {

    assertThat(activity.fanArt).isNotNull();

    Drawable background = activity.fanArt.getBackground();
    ShadowDrawable shadowDrawable = Shadows.shadowOf(background);
    assertThat(shadowDrawable.getCreatedFromResId()).isEqualTo(R.drawable.tvshows);
  }

  @Test public void assertThatTVShowItemCountIsNotNull() {
    assertThat(activity.tvShowItemCountView).isNotNull().isInstanceOf(TextView.class);
  }

  @Test public void assertThatCategoryFilterIsNotNull() {
    assertThat(activity.categorySpinner).isNotNull().isInstanceOf(Spinner.class);
  }

  @Test public void assertThatTVShowBannerGalleryIsNotNull() {
    assertThat(activity.tvShowRecyclerView).isNotNull().isInstanceOf(RecyclerView.class);
  }

  @Test public void assertThatSecondaryCategoryFilterIsNotNull() {
    assertThat(activity.secondarySpinner).isNotNull().isInstanceOf(Spinner.class);
  }

  @Test public void displayShowsShowsToastWhenSeriesIsEmpty() {
    activity.displayShows(Collections.<SeriesContentInfo>emptyList(), null);

    Toast latestToast = ShadowToast.getLatestToast();
    assertThat(latestToast).isNotNull();
  }

  @Test public void displayShowsSetsItemCountToExpectedSize() {
    List<SeriesContentInfo> expectedSeries = new ArrayList<>();
    expectedSeries.add(new TVShowSeriesInfo());

    activity.displayShows(expectedSeries, null);

    assertThat(activity.tvShowItemCountView).hasText("1 Item(s)");
  }

  @Test public void restartCallsPopulateMenuDrawer() {
    TVShowBrowserActivity spy = spy(activity);
    doNothing().when(spy).populateMenuDrawer();

    spy.onRestart();

    verify(spy).populateMenuDrawer();
  }

  @Test public void resumeCallsPopulateDrawer() {
    TVShowBrowserActivity spy = spy(activity);
    doNothing().when(spy).populateMenuDrawer();

    spy.restarted_state = true;
    spy.onResume();

    verify(spy).populateMenuDrawer();
  }

  @Test public void resumeRequestsUpdatedTVCategoriesFromPresenter() {
    String expectedKey = RandomStringUtils.randomAlphanumeric(5);

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
    List<CategoryInfo> expectedCategories = Collections.singletonList(new CategoryInfo());
    activity.updateCategories(expectedCategories);

    assertThat(activity.categorySpinner.getAdapter()).isNotNull();
    ArrayAdapter<CategoryInfo> actualAdapter = (ArrayAdapter<CategoryInfo>) activity.categorySpinner.getAdapter();
    ShadowArrayAdapter shadowArrayAdapter = Shadow.extract(actualAdapter);
    assertThat(shadowArrayAdapter.getResourceId()).isEqualTo(R.layout.serenity_spinner_textview);
    assertThat(shadowArrayAdapter.getDropDownViewResourceId()).isEqualTo(R.layout.serenity_spinner_textview_dropdown);
  }

  @Test public void updateCategoriesMakesCategorySpinnerVisible() {
    activity.categorySpinner.setVisibility(View.GONE);

    List<CategoryInfo> expectedCategories = Collections.singletonList(new CategoryInfo());
    activity.updateCategories(expectedCategories);

    assertThat(activity.categorySpinner).isVisible();
  }

  @Test public void onCreateSetsOnKeyDownDelegate() {
    assertThat(activity.onKeyDownDelegate).isNotNull().isInstanceOf(OnKeyDownDelegate.class);
  }

  @Test public void populateSecondaryCategoriesSetsSecondarySpinnerVisible() {
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    List<SecondaryCategoryInfo> expectedCategories = Collections.singletonList(new SecondaryCategoryInfo());
    activity.populateSecondaryCategories(expectedCategories, selectedCategory);

    assertThat(activity.secondarySpinner).isVisible();
    assertThat(activity.categorySpinner).isVisible();
  }

  @Test public void populateSecondaryCategoriesSetsSecondarySetsDropDownViewResourceId() {
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
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    List<SecondaryCategoryInfo> expectedCategories = Collections.singletonList(new SecondaryCategoryInfo());
    activity.populateSecondaryCategories(expectedCategories, selectedCategory);

    assertThat(activity.secondarySpinner.getOnItemSelectedListener()).isInstanceOf(
        TVSecondaryCategorySpinnerOnItemSelectedListener.class);
  }

  @Test public void populateSecondaryCategoriesDisplaysToastWhenCategoriesIsNull() {
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    activity.populateSecondaryCategories(null, selectedCategory);

    assertThat(ShadowToast.getLatestToast()).isNotNull();
    assertThat(ShadowToast.getTextOfLatestToast()).contains("No Entries available for category");
  }

  @Test public void populateSecondaryCategoriesDisplaysToastWhenCategoriesIsEmpty() {
    activity.categorySpinner.setVisibility(View.GONE);
    activity.secondarySpinner.setVisibility(View.GONE);

    String selectedCategory = RandomStringUtils.randomAlphabetic(10);

    activity.populateSecondaryCategories(Collections.<SecondaryCategoryInfo>emptyList(), selectedCategory);

    assertThat(ShadowToast.getLatestToast()).isNotNull();
    assertThat(ShadowToast.getTextOfLatestToast()).contains("No Entries available for category");
  }

  @Test public void onKeyDownCallsDelegate() {
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

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule(), new TestModule());
  }

  public class TestModule extends Module {

    public TestModule() {
      bind(TVCategoryState.class).toInstance(mockTVCategoryState);
      bind(SharedPreferences.class).toInstance(mockSharedPreferences);
      bind(TVShowBrowserPresenter.class).toInstance(mockTVShowBrowserPresenter);
      bind(LinkedList.class).withName(ForVideoQueue.class).toInstance(new LinkedList());
      bind(Resources.class).toInstance(ApplicationProvider.getApplicationContext().getResources());
      bind(VideoPlayerIntentUtils.class).toInstance(mockVideoPlayerIntentUtils);
    }
  }

}