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

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.model.meta.Device;

import us.nineworlds.serenity.ui.listeners.BrowseRegistryListener;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author dcarver
 * 
 */
public class DLNAServiceConnection implements ServiceConnection {

	private AndroidUpnpService upnpService;
	private BrowseRegistryListener registryListener;

	public DLNAServiceConnection(AndroidUpnpService service,
			BrowseRegistryListener listener) {
		upnpService = service;
		registryListener = listener;
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		upnpService = (AndroidUpnpService) service;
		if (upnpService == null) {
			return;
		}

		// Refresh the list with all known devices
		for (Device device : upnpService.getRegistry().getDevices()) {
			registryListener.deviceAdded(device);
		}

		// Getting ready for future device advertisements
		upnpService.getRegistry().addListener(registryListener);

		// Search asynchronously for all devices
		upnpService.getControlPoint().search();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		upnpService = null;
	}

}
