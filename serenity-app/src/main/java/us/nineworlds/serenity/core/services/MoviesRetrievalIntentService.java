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

package us.nineworlds.serenity.core.services;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.Types;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;

/**
 * A service that retrieves movies information from the Plex Media Server.
 *
 * @author dcarver
 */
public class MoviesRetrievalIntentService extends AbstractPlexRESTIntentService {

  private static final String MOVIES_RETRIEVAL_INTENT_SERVICE = "MoviesRetrievalIntentService";

  private static final String DEFAULT_CATEGORY = "all";

  protected List<VideoContentInfo> videoContentList = null;
  protected String key;
  protected String category;

  @Deprecated public MoviesRetrievalIntentService() {
    super(MOVIES_RETRIEVAL_INTENT_SERVICE);
    videoContentList = new ArrayList<VideoContentInfo>();
  }

  @Override public void sendMessageResults(Intent intent) {
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Messenger messenger = (Messenger) extras.get("MESSENGER");
      Message msg = Message.obtain();
      msg.obj = videoContentList;
      try {
        messenger.send(msg);
      } catch (RemoteException ex) {
        Log.e(getClass().getName(), "Unable to send message", ex);
      }
    }
  }

  @Override protected void onHandleIntent(Intent intent) {
    Bundle bundle = intent.getExtras();
    if (bundle == null) {
      Log.e(getClass().getName(), "Missing bundle extras.");
      return;
    }
    key = bundle.getString("key", "");
    category = intent.getExtras().getString("category", DEFAULT_CATEGORY);
    createPosters();
    sendMessageResults(intent);
  }

  protected void createPosters() {
    IMediaContainer mc = null;
    try {
      mc = retrieveVideos();
    } catch (IOException ex) {
      Log.e("AbstractPosterImageGalleryAdapter", "Unable to talk to server: ", ex);
    } catch (Exception e) {
      Log.e("AbstractPosterImageGalleryAdapter", "Oops.", e);
    }

    if (mc != null && mc.getSize() > 0) {
      videoContentList = new MovieMediaContainer(mc).createVideos();
    }
  }

  protected IMediaContainer retrieveVideos() throws Exception {
    if (category == null) {
      category = DEFAULT_CATEGORY;
    }

    return factory.retrieveItemByIdCategory(key, category, Types.MOVIES);
  }
}
