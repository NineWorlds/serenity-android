package us.nineworlds.serenity.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import us.nineworlds.serenity.injection.SerenityObjectGraph;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;

public class OkHttpStack extends HurlStack {

	@Inject
	OkHttpClient client;

	public OkHttpStack() {
		SerenityObjectGraph.getInstance().inject(this);
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException {
		return client.open(url);
	}
}