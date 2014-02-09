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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.preference.Preference;
import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.SerenityApplication;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.Server;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import us.nineworlds.serenity.ui.activity.OverscanSetupActivity;
import us.nineworlds.serenity.ui.util.DisplayUtils;

/**
 * This is the main activity for managing user preferences in the app.
 * 
 * @author dcarver
 * 
 */
public class SerenityPreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		findPreference("overscan_setup").setOnPreferenceClickListener(this);
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
		populateAvailableLocales();
		populateSupportedPlayers();
	}

	/**
	 * Populates the Discovered Devices preference list with available Plex
	 * Media Servers. The name of the Device and the device's IP address are
	 * used as values in the entry. The user friendly name is used from the
	 * device itself.
	 * 
	 */
	protected void populateAvailablePlexMediaServers() {
		ListPreference discoveredServers = (ListPreference) findPreference("discoveredServer");
		ConcurrentHashMap<String, Server> plexMediaServers = SerenityApplication
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
		Iterator<Map.Entry<String, Server>> entIt = plexMediaServers.entrySet()
				.iterator();
		while (entIt.hasNext()) {
			Map.Entry<String, Server> servers = entIt
					.next();
			Server device = servers.getValue();
			ipAddresses.add(device.getIPAddress());
		}
		if (!ipAddresses.isEmpty()) {
			ipAddresses.toArray(values);
			discoveredServers.setEntryValues(values);
		}

	}
	
	protected void populateAvailableLocales() {
		Locale[] locales = Locale.getAvailableLocales();
		ListPreference preferenceLocales = (ListPreference) findPreference("preferred_subtitle_language");
		ArrayList<String> localNames = new ArrayList<String>();
		ArrayList<String> localCodes = new ArrayList<String>();
		for (Locale local : locales) {
			if (!localNames.contains(local.getDisplayLanguage())) {
				localNames.add(local.getDisplayLanguage());
				localCodes.add(local.getISO3Language());
			}
		}
		String entries[] = new String[localNames.size()];
		String values[] = new String[localCodes.size()];
		localNames.toArray(entries);
		localCodes.toArray(values);
		preferenceLocales.setEntries(entries);
		preferenceLocales.setEntryValues(values);
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
	
	protected void populateSupportedPlayers() {
		ListPreference supportedPlayers = (ListPreference) findPreference("serenity_external_player_filter");
		Map<String, String> availablePlayers = new HashMap<String, String>();
		if (hasPlayerByName(this, "com.mxtech.videoplayer.ad")) {
			availablePlayers.put("mxplayer", "MX Player");
		}
		
		if (hasPlayerByName(this, "com.mxtech.videoplayer.pro")) {
			availablePlayers.put("mxplayerpro", "MX Player Pro");
		}
		
		if (hasPlayerByName(this, "net.gtvbox.videoplayer")) {
			availablePlayers.put("vimu", "ViMu Player" );
		}
		
		availablePlayers.put("default", "System Default");
		
		String[] entries = new String[availablePlayers.size()];
		String[] values = new String[availablePlayers.size()];
		ArrayList playerNames = new ArrayList();
		ArrayList playerValues = new ArrayList();
		
		
		for (Entry entry : availablePlayers.entrySet()) {
			playerNames.add(entry.getValue());
			playerValues.add(entry.getKey());
		}
		
		playerNames.toArray(entries);
		playerValues.toArray(values);
		
		supportedPlayers.setEntries(entries);
		supportedPlayers.setEntryValues(values);
	}
	
	protected boolean hasPlayerByName(Context context, String playerPackageName) {

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> pkgAppsList = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);

		for (ResolveInfo resolveInfo : pkgAppsList) {
			String packageName = resolveInfo.activityInfo.packageName;
			if (packageName.contains(playerPackageName)) {
				return true;
			}
		}

		return false;		
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		final String key = preference.getKey();
		if ("overscan_setup".equals(key)) {
			startActivity(new Intent(this, OverscanSetupActivity.class));
			return true;
		}
		return false;
	}
}
