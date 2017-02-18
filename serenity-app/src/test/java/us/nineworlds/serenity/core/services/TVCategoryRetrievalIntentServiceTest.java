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

package us.nineworlds.serenity.core.services;

import android.content.Intent;
import dagger.Module;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TVCategoryRetrievalIntentServiceTest extends InjectingTest {

	MockTVShowCategoryRetrievalIntentService service;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		service = new MockTVShowCategoryRetrievalIntentService();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void arrayListIsEmptyWhenNullIsPassedForBundleExtras() {
		Intent intent = Mockito.mock(Intent.class);
		service.onHandleIntent(intent);
		assertThat(service.getCatagories()).isEmpty();
	}

	protected class MockTVShowCategoryRetrievalIntentService extends
	TVShowCategoryRetrievalIntentService {

		@Override
		public void onHandleIntent(Intent intent) {
			super.onHandleIntent(intent);
		}

		public List<CategoryInfo> getCatagories() {
			return categories;
		}
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(addsTo = AndroidModule.class, includes = SerenityModule.class, injects = {
			TVCategoryRetrievalIntentServiceTest.class,
			MockTVShowCategoryRetrievalIntentService.class })
	public class TestModule {

	}

}
