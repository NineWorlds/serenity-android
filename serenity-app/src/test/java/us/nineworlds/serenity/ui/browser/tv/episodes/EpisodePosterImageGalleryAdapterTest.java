package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.test.InjectingTest;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(AndroidJUnit4.class)
public class EpisodePosterImageGalleryAdapterTest extends InjectingTest {

  private EpisodePosterImageGalleryAdapter adapter;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    adapter = new EpisodePosterImageGalleryAdapter();
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }

  @Test
  public void onCreateViewHolderCreatesTVShowViewHolder() {
    EpisodeViewHolder viewHolder = createViewHolder();

    assertThat(viewHolder).isNotNull();
  }

  private EpisodeViewHolder createViewHolder() {
    ContextThemeWrapper context = new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme);
    LinearLayout linearLayout = new LinearLayout(context);
    return (EpisodeViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
  }

  @Test
  public void onBindViewHolder() {
    EpisodeViewHolder mockEpisodeViewHolder = mock(EpisodeViewHolder.class);
    VideoContentInfo mockSeriesContent = mock(VideoContentInfo.class);
    doReturn(new View(getApplicationContext())).when(mockEpisodeViewHolder).getItemView();
    doReturn(getApplicationContext()).when(mockEpisodeViewHolder).getContext();

    adapter.updateEpisodes(Collections.singletonList(mockSeriesContent));
    adapter.onBindViewHolder(mockEpisodeViewHolder, 0);

    verify(mockEpisodeViewHolder).reset();
    verify(mockEpisodeViewHolder).createImage(eq(mockSeriesContent), eq(250), eq(160), any());
    verify(mockEpisodeViewHolder).toggleWatchedIndicator(mockSeriesContent);
  }
}