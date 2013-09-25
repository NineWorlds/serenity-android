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

package us.nineworlds.serenity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.ui.activity.SerenityActivity;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author dcarver
 *
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	SerenityActivity activity;
	
	@Before
	public void setUp() throws Exception {
		activity = new MainActivity();
	}
	
	@After
	public void tearDown() throws Exception {
		activity = null;
	}
	
	@Test
	public void createMainActivity() throws Exception {
		assertNotNull(activity);
	}
	
	@Test
	public void getApplicationName() throws Exception {
		String appname = Robolectric.application.getResources().getString(R.string.app_name);
		assertNotNull(appname);
		assertEquals("Serenity for Android", appname);
	}
	
	@Test
	public void testPreferencesManager() throws Exception {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		assertNotNull(preferences);
	}

}
