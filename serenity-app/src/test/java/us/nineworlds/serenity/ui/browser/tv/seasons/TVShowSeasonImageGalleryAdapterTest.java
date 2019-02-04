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
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.test.InjectingTest;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class TVShowSeasonImageGalleryAdapterTest extends InjectingTest {

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
    doReturn(new View(RuntimeEnvironment.application)).when(mockedViewHolder).getItemView();
    doReturn(getApplicationContext()).when(mockedViewHolder).getContext();

    adapter.updateSeasonsList(Collections.singletonList(seriesContentInfo));
    adapter.onBindViewHolder(mockedViewHolder, 0);

    verify(mockedViewHolder).reset();
    verify(mockedViewHolder).createImage(seriesContentInfo, 100, 125);
    verify(mockedViewHolder).toggleWatchedIndicator(seriesContentInfo);
  }

  private SeasonViewHolder createViewHolder() {
    LinearLayout linearLayout = new LinearLayout(application);
    return (SeasonViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }
}