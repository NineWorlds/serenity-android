package us.nineworlds.serenity.ui.browser.tv;

import android.view.ContextThemeWrapper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import us.nineworlds.serenity.R;
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
public class TVShowRecyclerAdapterTest extends InjectingTest {

  TVShowRecyclerAdapter adapter;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    adapter = new TVShowRecyclerAdapter();
  }

  @Override public void installTestModules() {
    scope.installTestModules(new TestingModule());
  }

  @Test
  public void onCreateViewHolderCreatesTVShowViewHolder() {
    TVShowViewHolder viewHolder = createViewHolder();

    assertThat(viewHolder).isNotNull();
  }

  private TVShowViewHolder createViewHolder() {
    ContextThemeWrapper context = new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme);
    LinearLayout linearLayout = new LinearLayout(context);
    return (TVShowViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
  }

  @Test
  public void onBindViewHolder() {
    TVShowViewHolder mockTVShowViewHolder = mock(TVShowViewHolder.class);
    SeriesContentInfo mockSeriesContent = mock(SeriesContentInfo.class);
    doReturn(new ImageView(getApplicationContext())).when(mockTVShowViewHolder).getItemView();
    doReturn(getApplicationContext()).when(mockTVShowViewHolder).getContext();

    adapter.updateSeries(Collections.singletonList(mockSeriesContent));
    adapter.onBindViewHolder(mockTVShowViewHolder, 0);

    verify(mockTVShowViewHolder).reset();
    verify(mockTVShowViewHolder).createImage(mockSeriesContent, 750, 120);
    verify(mockTVShowViewHolder).toggleWatchedIndicator(mockSeriesContent);
  }

  @Test
  public void getItemCountReturnsExpectedSize() {
    SeriesContentInfo mockSeriesContent = mock(SeriesContentInfo.class);
    adapter.updateSeries(Collections.singletonList(mockSeriesContent));
    assertThat(adapter.getItemCount()).isEqualTo(1);
  }

  @Test
  public void getItemIdReturnsExpectedId() {
    SeriesContentInfo mockSeriesContent = mock(SeriesContentInfo.class);
    adapter.updateSeries(Collections.singletonList(mockSeriesContent));
    assertThat(adapter.getItemId(0)).isEqualTo(0);
  }

  @Test
  public void getItemReturnsExpectedItem() {
    SeriesContentInfo mockSeriesContent = mock(SeriesContentInfo.class);
    adapter.updateSeries(Collections.singletonList(mockSeriesContent));
    assertThat(adapter.getItem(0)).isEqualTo(mockSeriesContent);
  }
}