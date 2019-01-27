package us.nineworlds.serenity.core;

import android.support.annotation.NonNull;
import com.android.volley.toolbox.HurlStack;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;

public class OkHttpStack extends HurlStack {

  @Inject OkHttpClient client;

  public OkHttpStack() {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  @Override protected HttpURLConnection createConnection(@NonNull URL url) throws IOException {
    OkUrlFactory factory = new OkUrlFactory(client);
    return factory.open(url);
  }
}