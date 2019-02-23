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

package us.nineworlds.serenity.core;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import toothpick.config.Module;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.test.InjectingTest;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class ServerConfigTest extends InjectingTest {

	SharedPreferences.OnSharedPreferenceChangeListener serverConfigChangeListener;
	ServerConfig serverConfig;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		Robolectric.getForegroundThreadScheduler().pause();
		Robolectric.getBackgroundThreadScheduler().pause();
		serverConfig = (ServerConfig) ServerConfig
				.getInstance(getApplicationContext());
		serverConfigChangeListener = serverConfig
				.getServerConfigChangeListener();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void serverPortNewValueIsRetrieved() {
		SharedPreferences prefs = mock(SharedPreferences.class);
		when(prefs.getString("serverport", "32400")).thenReturn("9999");

		serverConfigChangeListener.onSharedPreferenceChanged(prefs,
				"serverport");
		verify(prefs).getString("serverport", "32400");
	}

	@Test
	public void serverHostNewValueIsRetrieved() {
		SharedPreferences prefs = mock(SharedPreferences.class);
		when(prefs.getString("server", "")).thenReturn("10.0.0.3");

		serverConfigChangeListener.onSharedPreferenceChanged(prefs, "server");
		verify(prefs).getString("server", "");
	}

	@Test
	public void serverSetsNewServerAddressBasedOnDiscoveredServers() {
		Editor reditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

		SharedPreferences prefs = mock(SharedPreferences.class);
		when(prefs.getString("discoveredServer", "")).thenReturn("10.0.0.3");
		when(prefs.edit()).thenReturn(reditor);

		serverConfigChangeListener.onSharedPreferenceChanged(prefs,
				"discoveredServer");
		verify(prefs).getString("discoveredServer", "");
	}

	@Test
	public void setHostSetsCorrectValue() {
		serverConfig.setHost("10.0.0.4");
		assertThat(serverConfig.getHost()).isEqualTo("10.0.0.4");
	}

	@Test
	public void setPortSetsExpectedValue() {
		serverConfig.setPort("6666");
		assertThat(serverConfig.getPort()).isEqualTo("6666");
	}

	@Override
	public void installTestModules() {
		scope.installTestModules(new TestingModule(), new TestModule());
	}

	public class TestModule extends Module {
		public TestModule() {
			bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()));
		}
	}
}
