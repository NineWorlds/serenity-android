package us.nineworlds.serenity.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import us.nineworlds.serenity.injection.SerenityObjectGraph;

import com.android.volley.toolbox.HurlStack;

public class OkHttpStack extends HurlStack {

	@Inject
	OkHttpClient client;

	public OkHttpStack() {
		SerenityObjectGraph.getInstance().inject(this);
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException {
		OkUrlFactory factory = new OkUrlFactory(client);
		return factory.open(url);
	}
}