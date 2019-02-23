package us.nineworlds.serenity.ui.browser.tv.seasons;

import android.view.View;
import android.widget.LinearLayout;
import androidx.test.core.app.ApplicationProvider;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeViewHolder;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class SeasonsEpisodePosterImageGalleryAdapterTest extends InjectingTest {

  @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock
  AbstractVideoOnItemClickListener mockonItemClickListner;

  @Mock
  View mockView;

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
    doReturn(new View(ApplicationProvider.getApplicationContext())).when(mockedViewHolder).getItemView();
    doReturn(getApplicationContext()).when(mockedViewHolder).getContext();

    adapter.updateEpisodes(Collections.singletonList(mockEpisodesContent));
    adapter.onBindViewHolder(mockedViewHolder, 0);

    verify(mockedViewHolder).reset();
    verify(mockedViewHolder).createImage(mockEpisodesContent, 220, 130);
    verify(mockedViewHolder).toggleWatchedIndicator(mockEpisodesContent);
    verify(mockedViewHolder).updateSeasonsTitle(mockEpisodesContent);
  }

  @Test
  public void onItemViewClickCallWhenNotNull() {
    adapter.setOnItemClickListener(mockonItemClickListner);
    adapter.onItemViewClick(mockView, 0);

    verify(mockonItemClickListner).onItemClick(mockView, 0);
  }

  @Test
  public void onItemViewFocusChangedClearsExistingAnimations() {
    doReturn(ApplicationProvider.getApplicationContext()).when(mockView).getContext();

    adapter.onItemViewFocusChanged(true, mockView, 0);

    verify(mockView, atLeastOnce()).clearAnimation();
  }

  private EpisodeViewHolder createViewHolder() {
    LinearLayout linearLayout = new LinearLayout(ApplicationProvider.getApplicationContext());
    return (EpisodeViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }
}