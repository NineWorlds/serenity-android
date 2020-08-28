package us.nineworlds.serenity.ui.browser.tv.seasons;

import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import androidx.test.core.app.ApplicationProvider;
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

import java.util.Collections;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class TVShowSeasonImageGalleryAdapterTest extends InjectingTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock
  TVShowSeasonOnItemClickListener mockOnItemClickListener;

  @Mock
  View mockView;

  TVShowSeasonImageGalleryAdapter adapter;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    adapter = new TVShowSeasonImageGalleryAdapter();
  }

  @Test
  public void onCreateViewHolderCreatesExpectedViewHolder() {
    SeasonViewHolder viewHolder = createViewHolder();
    assertThat(viewHolder).isNotNull();
  }

  @Test
  public void onBindViewHolder() {
    SeasonViewHolder mockedViewHolder = mock(SeasonViewHolder.class);
    SeriesContentInfo seriesContentInfo = mock(SeriesContentInfo.class);
    doReturn(new View(getApplicationContext())).when(mockedViewHolder).getItemView();
    doReturn(getApplicationContext()).when(mockedViewHolder).getContext();

    adapter.updateSeasonsList(Collections.singletonList(seriesContentInfo));
    adapter.onBindViewHolder(mockedViewHolder, 0);

    verify(mockedViewHolder).reset();
    verify(mockedViewHolder).createImage(eq(seriesContentInfo), eq(100), eq(125), any());
    verify(mockedViewHolder).toggleWatchedIndicator(seriesContentInfo);
  }

  @Test
  public void itemCountReturnsExpectedSize() {
    assertThat(adapter.getItemCount()).isEqualTo(0);
  }

  @Test
  public void onItemClickListenerIsCalled() {
    TVShowSeasonImageGalleryAdapter spy = spy(adapter);
    doReturn(mockOnItemClickListener).when(spy).createOnItemClickListener(any(View.class));

    spy.onItemViewClick(mockView, 0);

    verify(mockOnItemClickListener).onItemClick(mockView, 0);
  }

  @Test
  public void onItemViewFocusChangedClearsAnimationOnViewWhenViewDoesNotHaveFocus() {
    adapter.onItemViewFocusChanged(false, mockView, 0);

    verify(mockView).clearAnimation();
  }

  private SeasonViewHolder createViewHolder() {
    ContextThemeWrapper context = new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme);
    LinearLayout linearLayout = new LinearLayout(context);
    return (SeasonViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }
}