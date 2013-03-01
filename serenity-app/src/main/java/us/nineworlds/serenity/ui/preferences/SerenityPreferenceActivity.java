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

package us.nineworlds.serenity.ui.preferences;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.RemoteDevice;

import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.SerenityApplication;

import us.nineworlds.serenity.R;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

/**
 * This is the main activity for managing user preferences in the app.
 * 
 * @author dcarver
 * 
 */
public class SerenityPreferenceActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		setResult(MainActivity.MAIN_MENU_PREFERENCE_RESULT_CODE);
		super.finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		populateAvailablePlexMediaServers();
	}

	/**
	 * Populates the Discovered Devices preference list with available
	 * Plex Media Servers.   The name of the Device and the device's IP
	 * address are used as values in the entry.   The user friendly name
	 * is used from the device itself.
	 *  
	 */
	protected void populateAvailablePlexMediaServers() {
		ListPreference discoveredServers = (ListPreference) findPreference("discoveredServer");
		ConcurrentHashMap<String, Device> plexMediaServers = SerenityApplication
				.getPlexMediaServers();
		if (plexMediaServers.isEmpty()) {
			discoveredServers.setEnabled(false);
			return;
		}

		discoveredServers.setEnabled(true);
		String entries[] = new String[plexMediaServers.size()];
		String values[] = new String[plexMediaServers.size()];

		plexMediaServers.keySet().toArray(entries);
		discoveredServers.setEntries(entries);

		ArrayList<String> ipAddresses = new ArrayList<String>();
		Iterator<Map.Entry<String, Device>> entIt = plexMediaServers.entrySet()
				.iterator();
		while (entIt.hasNext()) {
			Map.Entry<String, Device> servers = (Map.Entry<String, Device>) entIt
					.next();
			Device device = servers.getValue();
			if (device instanceof RemoteDevice) {
				RemoteDevice plexServer = (RemoteDevice) device;
				URL serverURL = plexServer.getIdentity().getDescriptorURL();
				String serverIPAddress = serverURL.getHost();
				ipAddresses.add(serverIPAddress);
			}
		}
		if (!ipAddresses.isEmpty()) {
			ipAddresses.toArray(values);
			discoveredServers.setEntryValues(values);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

}
