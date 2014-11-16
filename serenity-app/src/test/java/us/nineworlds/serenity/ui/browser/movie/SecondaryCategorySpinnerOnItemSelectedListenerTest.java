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

package us.nineworlds.serenity.ui.browser.movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import dagger.Module;
import dagger.Provides;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class SecondaryCategorySpinnerOnItemSelectedListenerTest extends
		InjectingTest {

	@Mock
	SharedPreferences mockSharedPreferences;

	@Mock
	View mockView;

	@Mock
	AdapterView mockAdapterView;

	@Mock
	SecondaryCategoryInfo mockCategoryInfo;

	SecondaryCategorySpinnerOnItemSelectedListener onItemSelectedListener;

	@Override
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		super.setUp();

		onItemSelectedListener = new SecondaryCategorySpinnerOnItemSelectedListener(
				"Action", "59");

	}

	@Test
	public void doStuff() {

	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(Robolectric.application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
			SecondaryCategorySpinnerOnItemSelectedListener.class,
			SecondaryCategorySpinnerOnItemSelectedListenerTest.class })
	public class TestModule {

		@Provides
		@Singleton
		SharedPreferences providesSharedPreferences() {
			return mockSharedPreferences;
		}
	}

}
