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

package com.github.kingargyle.plexappclient;

import java.net.URI;
import java.net.URL;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.Toast;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.core.ServerConfig;
import com.github.kingargyle.plexappclient.ui.activity.SerenityActivity;
import com.github.kingargyle.plexappclient.ui.preferences.SerenityPreferenceActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SerenityActivity {

	private Gallery mainGallery;
	private View mainView;
	private SharedPreferences preferences;
	public static int MAIN_MENU_PREFERENCE_RESULT_CODE = 100;
	public static int BROWSER_RESULT_CODE = 200;
	private boolean restarted_state = false;
	
	private AndroidUpnpService upnpService;
	private BrowseRegistryListener registryListener = new BrowseRegistryListener();
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
	        upnpService = (AndroidUpnpService) service;

	        // Refresh the list with all known devices
	        for (Device device : upnpService.getRegistry().getDevices()) {
	            registryListener.deviceAdded(device);
	        }

	        // Getting ready for future device advertisements
	        upnpService.getRegistry().addListener(registryListener);

	        // Search asynchronously for all devices
	        upnpService.getControlPoint().search();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        upnpService = null;
	    }		
		
	};

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_plex_app_main);
		mainView = findViewById(R.id.mainLayout);
		mainGallery = (Gallery) findViewById(R.id.mainGalleryMenu);	
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(((ServerConfig)ServerConfig.getInstance()).getServerConfigChangeListener());
		
		boolean googletv = SerenityApplication.isGoogleTV(this);
		if (!googletv) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("external_player", true);
			editor.commit();
		}
		
		getApplicationContext().bindService(
	            new Intent(this, AndroidUpnpServiceImpl.class),
	            serviceConnection,
	            Context.BIND_AUTO_CREATE
	        );
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plex_app_main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent(MainActivity.this,
				SerenityPreferenceActivity.class);
		startActivityForResult(i, 0);

		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// Hey! This refreshes the whole Activity!
		//this.onCreate(null);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		if (restarted_state == false) {
			setupGallery();
		}
		restarted_state = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);		
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		restarted_state = true;
		super.onRestart();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Make sure we release the service otherwise it will keep
		// checking for items.
		if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        getApplicationContext().unbindService(serviceConnection);		
	}

	/**
	 * Refresh the screen after coming back from the preferences screen.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MAIN_MENU_PREFERENCE_RESULT_CODE) {
			setupGallery();
		}
		
	}
	

	private void setupGallery() {
		mainGallery.setAdapter(new MainMenuTextViewAdapter(this, mainView));
		mainGallery
				.setOnItemSelectedListener(new GalleryOnItemSelectedListener(
						mainView));
		mainGallery
				.setOnItemClickListener(new GalleryOnItemClickListener(this));
	}
	
	/**
	 * This class is taken from the example code for cling.
	 * 
	 * http://4thline.org/projects/cling/core/manual/cling-core-manual.html#chapter.Android
	 * 
	 * It sole purpose is to listen for remote UPnP devices being discovered.
	 * In our use case, it is looking for Plex Media Servers to announce
	 * that they are available to be used.   It uses the remoteDiscoverStart
	 * to get the necessary information so that it is available to the Preferences
	 * as quickly as possible.
	 * 
	 * This populates a global ConcurrentHashMap in the main application and is then
	 * used to populate the available devices in the preference setting.
	 * 
	 * @author dcarver
	 *
	 */
	public class BrowseRegistryListener extends DefaultRegistryListener {

	    @Override
	    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
	        deviceAdded(device);
	    }

	    @Override
	    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
	        deviceRemoved(device);
	    }

	    @Override
	    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
	    	
	    }

	    @Override
	    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
	        deviceRemoved(device);
	    }

	    @Override
	    public void localDeviceAdded(Registry registry, LocalDevice device) {
	        
	    }

	    @Override
	    public void localDeviceRemoved(Registry registry, LocalDevice device) {
	        
	    }

	    public void deviceAdded(final Device device) {
	    	String friendlyName = device.getDetails().getFriendlyName();
	    	if (friendlyName.contains("Plex Media Server")) {
	    		DeviceDetails details = device.getDetails();
	    		SerenityApplication.getPlexMediaServers().put(friendlyName, device);
	    	}
	    }

	    public void deviceRemoved(final Device device) {
	    	String friendlyName = device.getDetails().getFriendlyName();
	    	SerenityApplication.getPlexMediaServers().remove(friendlyName);
	    }
	}
	
	protected class DeviceDisplay {
	    Device device;

	    public DeviceDisplay(Device device) {
	        this.device = device;
	    }

	    public Device getDevice() {
	        return device;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        DeviceDisplay that = (DeviceDisplay) o;
	        return device.equals(that.device);
	    }

	    @Override
	    public int hashCode() {
	        return device.hashCode();
	    }

	    @Override
	    public String toString() {
	        // Display a little star while the device is being loaded
	        return device.isFullyHydrated() ? device.getDisplayString() : device.getDisplayString() + " *";
	    }
	}	
	
	
}
