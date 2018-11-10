package us.nineworlds.serenity.core;

import com.android.volley.toolbox.HurlStack;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;

public class OkHttpStack extends HurlStack {

  @Inject OkHttpClient client;

  public OkHttpStack() {
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }

  @Override protected HttpURLConnection createConnection(URL url) throws IOException {
    OkUrlFactory factory = new OkUrlFactory(client);
    return factory.open(url);
  }
}