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

package us.nineworlds.serenity.ui.listeners;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

// UnitTestCodeMash2015

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SubtitleSpinnerOnItemSelectedListenerTest extends InjectingTest {

	@Mock
	SharedPreferences mockSharedPreferences;

	@Mock
	MoviePosterInfo mockVideoContentInfo;

	SubtitleSpinnerOnItemSelectedListener subtitleSpinnerOnItemSelectedListener;

	@Mock
	AdapterView mockAdapterView;

	@Mock
	View mockView;

	@Mock
	Subtitle mockSubtitle;

	@Mock
	ArrayAdapter mockAdapter;

	@Override
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		super.setUp();
		subtitleSpinnerOnItemSelectedListener = new SubtitleSpinnerOnItemSelectedListener(
				mockVideoContentInfo);
	}

	@Test
	public void onItemSelectedReturnsExistingSubtitleIfNotFirstSelection() {
		subtitleSpinnerOnItemSelectedListener.setFirstSelection(false);
		doReturn(mockSubtitle).when(mockAdapterView)
				.getItemAtPosition(anyInt());

		subtitleSpinnerOnItemSelectedListener.onItemSelected(mockAdapterView,
				mockView, 0, 0);

		verify(mockVideoContentInfo).setSubtitle(mockSubtitle);
		verify(mockAdapterView).getItemAtPosition(0);
	}

	@Test
	public void onItemSelectedFirstTimeChecksForAutomaticSubtitleSelection() {
		subtitleSpinnerOnItemSelectedListener.setFirstSelection(true);
		doReturn(false).when(mockSharedPreferences).getBoolean(anyString(),
				anyBoolean());

		subtitleSpinnerOnItemSelectedListener.onItemSelected(mockAdapterView,
				mockView, 0, 0);

		verify(mockSharedPreferences).getBoolean(
				"automatic_subtitle_selection", false);
	}

	@Test
	public void onItemSelectedFirstTimeGetsDefaultLanuageWhenAutomaticSubtitleSelectionIsTrue() {
		subtitleSpinnerOnItemSelectedListener.setFirstSelection(true);
		doReturn(true).when(mockSharedPreferences).getBoolean(
				eq("automatic_subtitle_selection"), anyBoolean());
		doReturn("en-us").when(mockSharedPreferences).getString(
				eq("preferred_subtitle_language"), anyString());
		doReturn(mockAdapter).when(mockAdapterView).getAdapter();
		doReturn(0).when(mockAdapter).getCount();

		subtitleSpinnerOnItemSelectedListener.onItemSelected(mockAdapterView,
				mockView, 0, 0);

		verify(mockSharedPreferences).getBoolean(
				"automatic_subtitle_selection", false);
		verify(mockSharedPreferences).getString("preferred_subtitle_language",
				"");
	}

	@Test
	public void onItemSelectedFirstTimeSetsDefaultLanguageSubtitle() {
		subtitleSpinnerOnItemSelectedListener.setFirstSelection(true);
		doReturn(true).when(mockSharedPreferences).getBoolean(
				eq("automatic_subtitle_selection"), anyBoolean());
		doReturn("en-us").when(mockSharedPreferences).getString(
				eq("preferred_subtitle_language"), anyString());

		doReturn(mockAdapter).when(mockAdapterView).getAdapter();
		doReturn(mockSubtitle).when(mockAdapterView)
				.getItemAtPosition(anyInt());

		doReturn(1).when(mockAdapter).getCount();
		doReturn("en-us").when(mockSubtitle).getLanguageCode();

		subtitleSpinnerOnItemSelectedListener.onItemSelected(mockAdapterView,
				mockView, 0, 0);

		verify(mockSharedPreferences).getBoolean(
				"automatic_subtitle_selection", false);
		verify(mockSharedPreferences).getString("preferred_subtitle_language",
				"");
		verify(mockAdapterView).setSelection(0);
		verify(mockSubtitle).getLanguageCode();
		verify(mockVideoContentInfo).setSubtitle(mockSubtitle);
	}

	@Override
	public List<Object> getModules() {
		List<Object> modules = new ArrayList<Object>();
		modules.add(new AndroidModule(application));
		modules.add(new TestModule());
		return modules;
	}

	@Module(includes = SerenityModule.class, addsTo = AndroidModule.class, injects = {
			SubtitleSpinnerOnItemSelectedListenerTest.class,
			SubtitleSpinnerOnItemSelectedListener.class }, overrides = true)
	public class TestModule {

		@Provides
		@Singleton
		SharedPreferences providesSharedPreferences() {
			return mockSharedPreferences;
		}
	}

}
