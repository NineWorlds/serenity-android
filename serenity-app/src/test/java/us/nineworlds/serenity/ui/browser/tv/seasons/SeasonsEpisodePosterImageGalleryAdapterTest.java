package us.nineworlds.serenity.ui.browser.tv.seasons;

import android.view.View;
import android.widget.LinearLayout;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeViewHolder;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class SeasonsEpisodePosterImageGalleryAdapterTest extends InjectingTest {

  SeasonsEpisodePosterImageGalleryAdapter adapter;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    adapter = new SeasonsEpisodePosterImageGalleryAdapter();
  }

  @Test
  public void onCreateViewHolderCreatesTVShowViewHolder() {
    EpisodeViewHolder viewHolder = createViewHolder();
    assertThat(viewHolder).isNotNull();
  }

  @Test
  public void onBindViewHolder() {
    EpisodeViewHolder mockedViewHolder = mock(EpisodeViewHolder.class);
    VideoContentInfo mockEpisodesContent = mock(VideoContentInfo.class);
    doReturn(new View(RuntimeEnvironment.application)).when(mockedViewHolder).getItemView();
    doReturn(getApplicationContext()).when(mockedViewHolder).getContext();

    adapter.updateEpisodes(Collections.singletonList(mockEpisodesContent));
    adapter.onBindViewHolder(mockedViewHolder, 0);

    verify(mockedViewHolder).reset();
    verify(mockedViewHolder).createImage(mockEpisodesContent, 220, 130);
    verify(mockedViewHolder).toggleWatchedIndicator(mockEpisodesContent);
    verify(mockedViewHolder).updateSeasonsTitle(mockEpisodesContent);
  }

  private EpisodeViewHolder createViewHolder() {
    LinearLayout linearLayout = new LinearLayout(application);
    return (EpisodeViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }
}