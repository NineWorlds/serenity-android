package us.nineworlds.serenity.injection.modules.providers;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Provider;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.VideoQueueHelper;

public class VideoQueueHelperProvider implements Provider<VideoQueueHelper> {

  @Inject @ApplicationContext Context context;
  @Inject protected SharedPreferences sharedPreferences;
  @Inject protected LinkedList<VideoContentInfo> videoQueue;

  @Override public VideoQueueHelper get() {
    return new VideoQueueHelper(context, sharedPreferences, videoQueue);
  }
}
