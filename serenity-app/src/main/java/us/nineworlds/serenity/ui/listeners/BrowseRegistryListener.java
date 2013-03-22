/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import us.nineworlds.serenity.SerenityApplication;

/**
 * This class is taken from the example code for cling.
 * 
 * http://4thline.org/projects/cling/core/manual/cling-core-manual.html#
 * chapter.Android
 * 
 * It sole purpose is to listen for remote UPnP devices being discovered. In our
 * use case, it is looking for Plex Media Servers to announce that they are
 * available to be used. It uses the remoteDiscoverStart to get the necessary
 * information so that it is available to the Preferences as quickly as
 * possible.
 * 
 * This populates a global ConcurrentHashMap in the main application and is then
 * used to populate the available devices in the preference setting.
 * 
 * @author dcarver
 * 
 */
public class BrowseRegistryListener extends DefaultRegistryListener {

	/**
	 * 
	 */
	private static final String PLEX_MEDIA_SERVER = "Plex Media Server";

	@Override
	public void remoteDeviceDiscoveryStarted(Registry registry,
			RemoteDevice device) {
		deviceAdded(device);
	}

	@Override
	public void remoteDeviceDiscoveryFailed(Registry registry,
			final RemoteDevice device, final Exception ex) {
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
		if (friendlyName.contains(PLEX_MEDIA_SERVER)) {
			DeviceDetails details = device.getDetails();
			SerenityApplication.getPlexMediaServers().put(friendlyName, device);
		}
	}

	public void deviceRemoved(final Device device) {
		String friendlyName = device.getDetails().getFriendlyName();
		SerenityApplication.getPlexMediaServers().remove(friendlyName);
	}
}
