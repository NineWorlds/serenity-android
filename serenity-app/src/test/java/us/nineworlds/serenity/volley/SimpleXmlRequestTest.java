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

package us.nineworlds.serenity.volley;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.InputStream;
import java.net.URL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import us.nineworlds.plex.rest.model.impl.MediaContainer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

@RunWith(RobolectricTestRunner.class)
public class SimpleXmlRequestTest {

	MockWebServer webserver;
	RequestQueue queue;

	@Before
	public void setUp() throws Exception {
		queue = VolleyUtils.getRequestQueueInstance(Robolectric.application);
		webserver = new MockWebServer();

		MockResponse response = new MockResponse();
		InputStream xmlStream = this.getClass().getResourceAsStream(
				"/resources/index.xml");
		response.setBody(xmlStream, 1198l);
		webserver.enqueue(response);

		webserver.play();
	}

	@After
	public void tearDown() throws Exception {
		webserver.shutdown();
	}

	@Test
	public void assertThatResponseDoesNotReturnResponseError() throws Exception {
		URL url = webserver.getUrl("/");
		SimpleXmlRequest request = new SimpleXmlRequest(Request.Method.GET,
				url.toString(), MediaContainer.class,
				new MockSuccessListener(), new MockErrorListener());
		queue.add(request);
	}

	protected class MockSuccessListener implements Listener {

		@Override
		public void onResponse(Object response) {
			System.out.println("Success Called");
			assertThat(response).isNotNull().isInstanceOf(MediaContainer.class);
		}
	}

	protected class MockErrorListener implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Assert.fail(error.getMessage());

		}
	}
}
