package us.nineworlds.serenity.ui.browser.tv.episodes;

import android.view.View;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.Module;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class EpisodePosterImageGalleryAdapterTest extends InjectingTest {

    EpisodePosterImageGalleryAdapter adapter;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        adapter = new EpisodePosterImageGalleryAdapter();
    }

    @Override
    public List<Object> getModules() {
        List<Object> modules = new ArrayList<>();
        modules.add(new AndroidModule(application));
        modules.add(new TestingModule());
        modules.add(new TestModule());
        return modules;
    }

    @Test
    public void onCreateViewHolderCreatesTVShowViewHolder() {
        EpisodeViewHolder viewHolder = createViewHolder();

        assertThat(viewHolder).isNotNull();
    }

    private EpisodeViewHolder createViewHolder() {
        LinearLayout linearLayout = new LinearLayout(application);
        return (EpisodeViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
    }

    @Test
    public void onBindViewHolder()  {
        EpisodeViewHolder mockEpisodeViewHolder = mock(EpisodeViewHolder.class);
        VideoContentInfo mockSeriesContent = mock(VideoContentInfo.class);
        doReturn(new View(RuntimeEnvironment.application)).when(mockEpisodeViewHolder).getItemView();

        adapter.updateEpisodes(Collections.singletonList(mockSeriesContent));
        adapter.onBindViewHolder(mockEpisodeViewHolder, 0);

        verify(mockEpisodeViewHolder).reset();
        verify(mockEpisodeViewHolder).createImage(mockSeriesContent, 300, 187);
        verify(mockEpisodeViewHolder).toggleWatchedIndicator(mockSeriesContent);
    }

    @Module(addsTo = AndroidModule.class,
            includes = SerenityModule.class,
            library = true,
            overrides = true,
            injects = EpisodePosterImageGalleryAdapterTest.class)
    public class TestModule {

    }

}