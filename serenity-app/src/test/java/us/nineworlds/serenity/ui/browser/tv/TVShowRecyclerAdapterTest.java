package us.nineworlds.serenity.ui.browser.tv;

import android.widget.ImageView;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import edu.emory.mathcs.backport.java.util.Collections;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TVShowRecyclerAdapterTest extends InjectingTest {

    TVShowRecyclerAdapter adapter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        adapter = new TVShowRecyclerAdapter();
    }

    @Override
    public List<Object> getModules() {
        List<Object> modules = new ArrayList<>();
        modules.add(new AndroidModule(RuntimeEnvironment.application));
        modules.add(new TestingModule());
        modules.add(new TestModule());
        return modules;
    }

    @Test
    public void onCreateViewHolderCreatesTVShowViewHolder() {
        TVShowViewHolder viewHolder = createViewHolder();

        assertThat(viewHolder).isNotNull();
    }

    private TVShowViewHolder createViewHolder() {
        LinearLayout linearLayout = new LinearLayout(application);
        return (TVShowViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
    }

    @Test
    public void onBindViewHolder()  {
        TVShowViewHolder mockTVShowViewHolder = mock(TVShowViewHolder.class);
        SeriesContentInfo mockSeriesContent = mock(SeriesContentInfo.class);
        doReturn(new ImageView(RuntimeEnvironment.application)).when(mockTVShowViewHolder).getItemView();

        adapter.updateSeries(Collections.singletonList(mockSeriesContent));
        adapter.onBindViewHolder(mockTVShowViewHolder, 0);

        verify(mockTVShowViewHolder).reset();
        verify(mockTVShowViewHolder).createImage(mockSeriesContent, 758, 140);
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



    @Module(addsTo = AndroidModule.class,
            includes = SerenityModule.class,
            library = true,
            overrides = true,
            injects = TVShowRecyclerAdapterTest.class)
    public class TestModule {

    }
}