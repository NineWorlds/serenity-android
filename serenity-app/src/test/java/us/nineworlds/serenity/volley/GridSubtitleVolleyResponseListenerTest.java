/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.volley;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.LinkedList;

import toothpick.config.Module;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.test.InjectingTest;
import us.nineworlds.serenity.ui.activity.SerenityActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author dcarver
 *
 */
@RunWith(RobolectricTestRunner.class)
public class GridSubtitleVolleyResponseListenerTest extends InjectingTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock
    VideoPlayerIntentUtils mockVideoPlayerIntentUtils;

    Serializer serializer;
    SerenityActivity movieBrowserActivity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        serializer = new Persister();
        Robolectric.getBackgroundThreadScheduler().pause();
        Robolectric.getForegroundThreadScheduler().pause();
        movieBrowserActivity = Robolectric
                .buildActivity(MovieBrowserActivity.class).create().get();
    }

    @After
    public void tearDown() throws Exception {
        if (movieBrowserActivity != null) {
            movieBrowserActivity.finish();
        }
    }

    @Test
    public void testVideoHasSubtitles() throws Exception {
        MoviePosterInfo video = new MoviePosterInfo();
        String data = IOUtils.toString(getClass().getResourceAsStream(
                "/resources/library/metadata/526/patriotSubtitle.xml"));
        MediaContainer mediaContainer = serializer.read(MediaContainer.class,
                data, false);

        LayoutInflater layoutInflator = movieBrowserActivity
                .getLayoutInflater();
        View view = layoutInflator
                .inflate(R.layout.poster_indicator_view, null);

        GridSubtitleVolleyResponseListener gridSubtitleResponseListener = new GridSubtitleVolleyResponseListener(
                video, movieBrowserActivity, view);
        gridSubtitleResponseListener.onResponse(mediaContainer);

        assertThat(video.getAvailableSubtitles()).isNotEmpty();
    }

    @Test
    public void testVideoHasNoSubtitles() throws Exception {
        MoviePosterInfo video = new MoviePosterInfo();
        String data = IOUtils.toString(getClass().getResourceAsStream(
                "/resources/library/metadata/523/nosubtitles.xml"));
        MediaContainer mediaContainer = serializer.read(MediaContainer.class,
                data, false);

        LayoutInflater layoutInflator = movieBrowserActivity
                .getLayoutInflater();
        View view = layoutInflator
                .inflate(R.layout.poster_indicator_view, null);

        GridSubtitleVolleyResponseListener gridSubtitleResponseListener = new GridSubtitleVolleyResponseListener(
                video, movieBrowserActivity, view);
        gridSubtitleResponseListener.onResponse(mediaContainer);

        assertThat(video.getAvailableSubtitles()).isNullOrEmpty();
    }

    @Override
    public void installTestModules() {
        scope.installTestModules(new TestingModule(), new TestModule());
    }

    public class TestModule extends Module {
        public TestModule() {
            bind(LinkedList.class).withName(ForVideoQueue.class).toInstance(new LinkedList());
            bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()));
            bind(Resources.class).toInstance(ApplicationProvider.getApplicationContext().getResources());
            bind(VideoPlayerIntentUtils.class).toInstance(mockVideoPlayerIntentUtils);
        }
    }
}
