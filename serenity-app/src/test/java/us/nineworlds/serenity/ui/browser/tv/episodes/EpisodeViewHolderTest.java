package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;

import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class EpisodeViewHolderTest {

    EpisodeViewHolder viewHolder;

    @Mock
    VideoContentInfo mockVideoContentInfo;

    @Before
    public void setUp() {
        initMocks(this);
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        LinearLayout recyclerView = new LinearLayout(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.poster_indicator_view, recyclerView, false);
        viewHolder = new EpisodeViewHolder(view);
    }

    @Test
    public void reset() {
        viewHolder.metaData.setVisibility(View.VISIBLE);
        viewHolder.title.setVisibility(View.VISIBLE);
        viewHolder.watchedView.setVisibility(View.VISIBLE);
        viewHolder.posterInprogressIndicator.setVisibility(View.VISIBLE);

        viewHolder.reset();

        assertThat(viewHolder.metaData).isInvisible();
        assertThat(viewHolder.title).isInvisible();
        assertThat(viewHolder.watchedView).isInvisible();
        assertThat(viewHolder.posterInprogressIndicator).isInvisible();
    }

    @Test
    public void createImageLoadsImageForBanners() {
        EpisodeViewHolder spy = spy(viewHolder);

        doReturn("http://www.example.com").when(mockVideoContentInfo).getImageURL();
        doNothing().when(spy).loadImage(anyString());

        spy.createImage(mockVideoContentInfo, 100, 100, null);

        verify(spy).loadImage("http://www.example.com");
        verify(spy, never()).loadImage(null);
    }

    @Test
    public void toggleWatchedIndicatorSetsWatchedViewVisibleWhenWatchedShowsIsGreaterThanZero() {
        viewHolder.watchedView.setVisibility(View.GONE);

        doReturn(false).when(mockVideoContentInfo).isPartiallyWatched();
        doReturn(true).when(mockVideoContentInfo).isWatched();

        viewHolder.toggleWatchedIndicator(mockVideoContentInfo);

        assertThat(viewHolder.watchedView).isVisible();

        verify(mockVideoContentInfo).isPartiallyWatched();
        verify(mockVideoContentInfo).isWatched();
    }

    @Test
    public void toggleWatchedIndicatorShowsPartiallyWatchedView() {
        viewHolder.watchedView.setVisibility(View.GONE);
        viewHolder.posterInprogressIndicator.setVisibility(View.INVISIBLE);

        doReturn(true).when(mockVideoContentInfo).isPartiallyWatched();
        doReturn(false).when(mockVideoContentInfo).isWatched();

        viewHolder.toggleWatchedIndicator(mockVideoContentInfo);

        assertThat(viewHolder.watchedView).isInvisible();

        verify(mockVideoContentInfo).isPartiallyWatched();
        verify(mockVideoContentInfo, never()).isWatched();
    }

    @Test
    public void seasonTitleHasExpectedText() {
        doReturn("Test").when(mockVideoContentInfo).getTitle();

        viewHolder.updateSeasonsTitle(mockVideoContentInfo);

        assertThat(viewHolder.title).hasText("Test").isVisible();

        verify(mockVideoContentInfo).getTitle();
    }

    @Test
    public void seasonMetaDataHasExpectedEpisodeText() {
        doReturn("Episode").when(mockVideoContentInfo).getEpisode();

        viewHolder.updateSeasonsTitle(mockVideoContentInfo);

        assertThat(viewHolder.metaData).endsWithText("Episode").isVisible();

        verify(mockVideoContentInfo, atLeastOnce()).getEpisode();
    }

    @Test
    public void seasonMetaDataHasExpectedSeasonText() {
        doReturn("Season").when(mockVideoContentInfo).getSeason();

        viewHolder.updateSeasonsTitle(mockVideoContentInfo);

        assertThat(viewHolder.metaData).startsWithText("Season").isVisible();

        verify(mockVideoContentInfo, atLeastOnce()).getSeason();
    }


}