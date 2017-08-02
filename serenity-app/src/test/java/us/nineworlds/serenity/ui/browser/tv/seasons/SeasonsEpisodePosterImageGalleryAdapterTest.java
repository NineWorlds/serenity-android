package us.nineworlds.serenity.ui.browser.tv.seasons;

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
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodeViewHolder;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
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
    public void onBindViewHolder()  {
        EpisodeViewHolder mockedViewHolder = mock(EpisodeViewHolder.class);
        VideoContentInfo mockEpisodesContent = mock(VideoContentInfo.class);

        adapter.updateEpisodes(Collections.singletonList(mockEpisodesContent));
        adapter.onBindViewHolder(mockedViewHolder, 0);

        verify(mockedViewHolder).reset();
        verify(mockedViewHolder).createImage(mockEpisodesContent, 270, 147);
        verify(mockedViewHolder).toggleWatchedIndicator(mockEpisodesContent);
        verify(mockedViewHolder).updateSeasonsTitle(mockEpisodesContent);
    }


    private EpisodeViewHolder createViewHolder() {
        LinearLayout linearLayout = new LinearLayout(application);
        return (EpisodeViewHolder) adapter.onCreateViewHolder(linearLayout, 0);
    }


    @Override
    public List<Object> getModules() {
        List<Object> modules = new ArrayList<>();
        modules.add(new AndroidModule(RuntimeEnvironment.application));
        modules.add(new TestingModule());
        modules.add(new TestModule());
        return modules;
    }

    @Module(addsTo = AndroidModule.class,
            includes = SerenityModule.class,
            library = true,
            overrides = true,
            injects = SeasonsEpisodePosterImageGalleryAdapterTest.class)
    public class TestModule {

    }
}