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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import dagger.Module;

@RunWith(RobolectricTestRunner.class)
@Config(reportSdk = 14, emulateSdk = 18)
public class VolleyUtilsTest extends InjectingTest {

	RequestQueue queue;

	@Inject
	VolleyUtils volley;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		queue = volley.getRequestQueue();
	}

	@Test
	public void assertThatVolleyRequestQueueIsNotNull() {
		queue = volley.getRequestQueue();
		assertThat(queue).isNotNull();
	}

	@Test
	public void assertThatVolleyRequestQueueIsSameInstance() {
		RequestQueue queue2 = volley.getRequestQueue();
		assertThat(queue).isEqualTo(queue2);

	}

	@Test
	public void assertThatXmlRequestIsNotNull() {
		Response.Listener rlistener = Mockito.mock(Response.Listener.class);
		Request request = volley.volleyXmlGetRequest("http://www.example.com",
				rlistener, new DefaultLoggingVolleyErrorListener());
		assertThat(request).isNotNull();
	}

	@Test
	public void assertThatJsonRequestIsNotNull() {
		Response.Listener rlistener = Mockito.mock(Response.Listener.class);
		Request request = volley.volleyJSonGetRequest("http://www.example.com",
				rlistener, new DefaultLoggingVolleyErrorListener());
		assertThat(request).isNotNull();
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(Robolectric.application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
		VolleyUtils.class, VolleyUtilsTest.class })
	public class TestModule {

	}

}
