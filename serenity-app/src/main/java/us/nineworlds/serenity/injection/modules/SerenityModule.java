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

package us.nineworlds.serenity.injection.modules;

import dagger.Module;
import dagger.Provides;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.core.logger.TimberLogger;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.TimeUtil;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.browser.tv.TVCategoryState;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserPresenter;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.GalleryVideoOnItemLongClickListener;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.util.VideoQueueHelper;

@Module(library = true)
public class SerenityModule {

  @Provides @Singleton TimeUtil providesTimeUtil() {
    return new TimeUtil();
  }

  @Provides @Singleton VideoQueueHelper providesVideoQueueHelper() {
    return new VideoQueueHelper();
  }

  @Provides @Singleton @ForMediaServers Map<String, Server> providesMediaServers() {
    return new ConcurrentHashMap<String, Server>();
  }

  @Provides @Singleton @ForVideoQueue LinkedList<VideoContentInfo> providesVideoQueue() {
    return new LinkedList<VideoContentInfo>();
  }

  @Provides @Singleton VideoPlayerIntentUtils providesVideoPlayerUtils() {
    return new VideoPlayerIntentUtils();
  }

  @Provides @Singleton OkHttpClient providesOkHttpClient() {
    return new OkHttpClient.Builder().build();
  }

  @Provides GalleryVideoOnItemClickListener providesGalleryVideoOnItemClickListener() {
    return new GalleryVideoOnItemClickListener();
  }

  @Provides GalleryVideoOnItemLongClickListener providesGalleryVideoOnItemLongClickListener() {
    return new GalleryVideoOnItemLongClickListener();
  }

  @Provides @Singleton MovieSelectedCategoryState providesMovieSelectedCategoryState() {
    return new MovieSelectedCategoryState();
  }

  @Provides @Singleton TVCategoryState providesTVCategoryState() {
    return new TVCategoryState();
  }

  @Provides TVShowBrowserPresenter providesTVShowBrowserPresenter() {
    return new TVShowBrowserPresenter();
  }

  @Provides @Singleton Logger providesLogger() {
    return new TimberLogger();
  }
}
