package us.nineworlds.serenity.core.services;

import android.os.AsyncTask;
import java.io.IOException;
import javax.inject.Inject;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;
import us.nineworlds.serenity.common.rest.SerenityClient;

public class CompletedVideoRequest extends AsyncTask<Void, Void, Void> {

  @Inject SerenityClient factory;

  private final String uvideoId;

  public CompletedVideoRequest(String videoId) {
    uvideoId = videoId;
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }

  @Override protected Void doInBackground(Void... params) {
    try {
      factory.progress(uvideoId, "0");
      factory.watched(uvideoId);
    } catch (IOException e) {
      // DO NOTHING
    }
    return null;
  }
}
