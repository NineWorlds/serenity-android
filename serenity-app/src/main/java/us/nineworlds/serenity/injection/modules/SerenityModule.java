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

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.OkHttpClient;
import toothpick.config.Module;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.core.logger.TimberLogger;
import us.nineworlds.serenity.core.util.TimeUtil;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.injection.modules.providers.VideoPlayerIntentUtilsProvider;
import us.nineworlds.serenity.injection.modules.providers.VideoQueueHelperProvider;
import us.nineworlds.serenity.ui.browser.movie.MovieSelectedCategoryState;
import us.nineworlds.serenity.ui.browser.tv.TVCategoryState;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserPresenter;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.util.VideoQueueHelper;

public class SerenityModule extends Module {

  public SerenityModule() {
    super();
    bind(LinkedList.class).withName(ForVideoQueue.class).toInstance(new LinkedList());
    bind(TimeUtil.class).toInstance(new TimeUtil());
    bind(VideoQueueHelper.class).toProvider(VideoQueueHelperProvider.class).providesSingleton();
    bind(Map.class).withName(ForMediaServers.class).toInstance(new ConcurrentHashMap<String, Server>());
    bind(VideoPlayerIntentUtils.class).toProvider(VideoPlayerIntentUtilsProvider.class).providesSingleton();
    bind(OkHttpClient.class).toInstance(new OkHttpClient.Builder().build());
    bind(MovieSelectedCategoryState.class).toInstance(new MovieSelectedCategoryState());
    bind(TVCategoryState.class).toInstance(new TVCategoryState());
    bind(TVShowBrowserPresenter.class).toInstance(new TVShowBrowserPresenter());
    bind(Logger.class).toInstance(new TimberLogger());
  }

}
