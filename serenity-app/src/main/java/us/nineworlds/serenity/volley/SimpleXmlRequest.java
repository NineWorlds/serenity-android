package us.nineworlds.serenity.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Simple Volley Class for doing XML HTTP Requests which are parsed into Java
 * objects by Simple @see {{@link http://simple.sourceforge.net/}
 */
public class SimpleXmlRequest<T> extends Request<T> {

	private static final Serializer serializer = new Persister();
	private final Class<T> clazz;
	private final Map<String, String> headers;
	private final Listener<T> listener;

	/**
	 * Make HTTP request and return a parsed object from Response Invokes the
	 * other constructor.
	 * 
	 * @see SimpleXmlRequest#SimpleXmlRequest(int, String, Class, Map, Listener,
	 *      ErrorListener)
	 */
	public SimpleXmlRequest(int method, String url, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		this(method, url, clazz, null, listener, errorListener);
	}

	/**
	 * Make HTTP request and return a parsed object from XML Response
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object
	 * @param headers
	 *            Map of request headers
	 * @param listener
	 * @param errorListener
	 * 
	 */
	public SimpleXmlRequest(int method, String url, Class<T> clazz,
			Map<String, String> headers, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if (headers != null) {
			return headers;
		}
		return super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {

			String data = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(serializer.read(clazz, data, false),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			return Response.error(new VolleyError(e.getMessage()));
		}
	}
}