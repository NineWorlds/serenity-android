package us.nineworlds.serenity.injection.modules.providers;

import android.content.SharedPreferences;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Provider;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.TimeUtil;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

public class VideoPlayerIntentUtilsProvider implements Provider<VideoPlayerIntentUtils> {

  @Inject @ForVideoQueue protected LinkedList<VideoContentInfo> videoQueue;
  @Inject protected SharedPreferences prefs;
  @Inject protected TimeUtil timeUtil;

  @Override public VideoPlayerIntentUtils get() {
    return new VideoPlayerIntentUtils(videoQueue, prefs, timeUtil);
  }
}
