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
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity;

import android.content.Context;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.birbit.android.jobqueue.JobManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import toothpick.config.Module;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.logger.Logger;
import us.nineworlds.serenity.core.util.AndroidHelper;

public class TestingModule extends Module {

  @Mock JobManager mockJobManager;
  @Mock SerenityClient mockPlexAppFactory;
  @Mock LocalBroadcastManager mockLocalBroadcastManager;
  @Mock Logger mockLogger;
  @Mock Context mockContext;
  @Mock AndroidHelper mockAndroidHelper;

  public TestingModule() {
    MockitoAnnotations.initMocks(this);
    bind(JobManager.class).toInstance(mockJobManager);
    bind(SerenityClient.class).toInstance(mockPlexAppFactory);
    bind(LocalBroadcastManager.class).toInstance(mockLocalBroadcastManager);
    bind(Logger.class).toInstance(mockLogger);
    bind(AndroidHelper.class).toInstance(mockAndroidHelper);
    //bind(Context.class).withName(ApplicationContext.class).toInstance(mockContext);
  }
}