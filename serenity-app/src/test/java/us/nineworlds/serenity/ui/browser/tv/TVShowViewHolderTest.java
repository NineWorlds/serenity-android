package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class TVShowViewHolderTest {

    @Mock
    SeriesContentInfo mockSeriesInfo;

    @Mock
    RecyclerView mockRecyclerView;

    @Mock
    RecyclerView.LayoutManager mockLayoutManager;

    Activity activity;

    TVShowViewHolder tvShowViewHolder;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        activity = Robolectric.buildActivity(Activity.class).create().get();
        LinearLayout recyclerView = new LinearLayout(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.poster_tvshow_indicator_view, recyclerView, false);
        tvShowViewHolder = new TVShowViewHolder(view);
    }

    @After
    public void tearDown() {
        activity.finish();
    }

    @Test
    public void resetSetsViewsToDefaultValues() {
        tvShowViewHolder.watchedView.setVisibility(View.VISIBLE);
        tvShowViewHolder.posterInprogressIndicator.setVisibility(View.VISIBLE);
        tvShowViewHolder.badgeCount.setVisibility(View.VISIBLE);

        tvShowViewHolder.reset();

        assertThat(tvShowViewHolder.watchedView).isInvisible();
        assertThat(tvShowViewHolder.posterInprogressIndicator).isInvisible();
        assertThat(tvShowViewHolder.badgeCount).isGone();
    }

    @Test
    public void createImageLoadsImageForBanners() {
        TVShowViewHolder spy = spy(tvShowViewHolder);

        doReturn("http://www.example.com").when(mockSeriesInfo).getImageURL();
        doNothing().when(spy).loadImage(anyString());
        doReturn(mockLayoutManager).when(mockRecyclerView).getLayoutManager();
        doReturn(new RecyclerView.LayoutParams(100, 100)).when(mockLayoutManager).generateLayoutParams(any());

        spy.createImage(mockSeriesInfo, 100, 100, mockRecyclerView);

        verify(spy).loadImage("http://www.example.com");
        verify(spy, never()).loadImage(null);
    }

    @Test
    public void toggleWatchedIndicatorSetsWatchedViewVisibleWhenWatchedShowsIsGreaterThanZero() {
        tvShowViewHolder.watchedView.setVisibility(View.GONE);

        doReturn("1").when(mockSeriesInfo).getShowsWatched();
        doReturn(false).when(mockSeriesInfo).isPartiallyWatched();
        doReturn(true).when(mockSeriesInfo).isWatched();

        tvShowViewHolder.toggleWatchedIndicator(mockSeriesInfo);

        assertThat(tvShowViewHolder.watchedView).isVisible();

        verify(mockSeriesInfo, atLeastOnce()).getShowsWatched();
        verify(mockSeriesInfo).isPartiallyWatched();
        verify(mockSeriesInfo).isWatched();
    }

    @Test
    public void toggleWatchedIndicatorSetsBadgeCountViewToVisibleWhenNotWatched() {
        tvShowViewHolder.watchedView.setVisibility(View.GONE);

        doReturn("1").when(mockSeriesInfo).getShowsWatched();
        doReturn(false).when(mockSeriesInfo).isPartiallyWatched();
        doReturn(false).when(mockSeriesInfo).isWatched();

        tvShowViewHolder.toggleWatchedIndicator(mockSeriesInfo);

        assertThat(tvShowViewHolder.watchedView).isInvisible();
        assertThat(tvShowViewHolder.badgeCount).isVisible();

        verify(mockSeriesInfo, atLeastOnce()).getShowsWatched();
        verify(mockSeriesInfo).isPartiallyWatched();
        verify(mockSeriesInfo).isWatched();
    }

    @Test
    public void toggleWatchedIndicatorShowsPartiallyWatchedView() {
        tvShowViewHolder.watchedView.setVisibility(View.GONE);
        tvShowViewHolder.posterInprogressIndicator.setVisibility(View.INVISIBLE);

        doReturn("1").when(mockSeriesInfo).getShowsWatched();
        doReturn(10).when(mockSeriesInfo).totalShows();
        doReturn(true).when(mockSeriesInfo).isPartiallyWatched();
        doReturn(false).when(mockSeriesInfo).isWatched();

        tvShowViewHolder.toggleWatchedIndicator(mockSeriesInfo);

        assertThat(tvShowViewHolder.watchedView).isInvisible();
        assertThat(tvShowViewHolder.posterInprogressIndicator).isVisible();

        verify(mockSeriesInfo, atLeastOnce()).getShowsWatched();
        verify(mockSeriesInfo).totalShows();
        verify(mockSeriesInfo).isPartiallyWatched();
        verify(mockSeriesInfo).isWatched();
    }

}