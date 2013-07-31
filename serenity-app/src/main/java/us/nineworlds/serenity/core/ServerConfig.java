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

import us.nineworlds.plex.rest.config.IConfiguration;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * A configuration that reads information from the SharedPreferences store. This
 * information contains necessary info for connecting to the plex media server.
 * 
 * @author dcarver
 * 
 */
public class ServerConfig implements IConfiguration {

	private String serveraddress;
	private String serverport;
	private String discoveredServers;
	private SharedPreferences preferences;
	private static ServerConfig config;
	private OnSharedPreferenceChangeListener listener;

	/**
	 * Sets up the configuration based on the context of the activity that
	 * called it.
	 * 
	 */
	private ServerConfig(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

		serveraddress = preferences.getString("server", "");
		discoveredServers = preferences.getString("discoveredServer", "");
		serverport = preferences.getString("serverport", "32400");
	}

	@Override
	public String getHost() {
		if (serveraddress.length() == 0) {
			return discoveredServers;
		}
		return serveraddress;
	}

	@Override
	public String getPort() {
		return serverport;
	}

	@Override
	public void setHost(String hostip) {
		serveraddress = hostip;

	}

	@Override
	public void setPort(String port) {
		serverport = port;
	}

	public static IConfiguration getInstance(Context context) {
		if (config == null) {
			config = new ServerConfig(context);
		}

		return config;
	}

	/**
	 * This should only be called after a context has been set.
	 * 
	 * @return
	 */
	public static IConfiguration getInstance() {
		return config;
	}

	public OnSharedPreferenceChangeListener getServerConfigChangeListener() {
		if (listener == null) {
			listener = new ServerConfigChangeListener();
		}
		return listener;
	}

	/**
	 * Listen for Server Configuration Changes from the SharedPreferences.
	 * 
	 * This handles updating the variables for accesing a plex media server.
	 * 
	 * @author dcarver
	 * 
	 */
	public class ServerConfigChangeListener implements
			OnSharedPreferenceChangeListener {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if ("serverport".equals(key)) {
				serverport = preferences.getString(key, "32400");
				return;
			}

			if ("server".equals(key)) {
				serveraddress = preferences.getString(key, "");
				return;
			}

			if ("discoveredServer".equals(key)) {
				discoveredServers = preferences.getString(key, "");
				serveraddress = discoveredServers;
				storeServerAddress();
				return;
			}
		}

		/**
		 * Store the server address if the discoveredServers has been set. Users
		 * can override this in the preference setting.
		 */
		protected void storeServerAddress() {
			Editor edit = preferences.edit();
			edit.putString("server", discoveredServers);
			edit.commit();
		}

	}

}
