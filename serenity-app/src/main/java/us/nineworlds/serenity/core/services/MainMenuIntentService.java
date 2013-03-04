/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
 * 
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

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.impl.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * A IntentService that fetches the available Libraries used
 * for menu options from the Plex Media Server.
 * 
 * @author dcarver
 *
 */
public class MainMenuIntentService extends AbstractPlexRESTIntentService {
	
	/**
	 * 
	 */
	private static final String SETTINGS_SECTION_KEY = "0";
	/**
	 * 
	 */
	private static final String SETTINGS_TYPE = "settings";
	/**
	 * 
	 */
	private static final String SETTINGS_MENU = "Settings";
	private ArrayList<MenuItem> menuItems;

	/**
	 * @param name
	 */
	public MainMenuIntentService() {
		super("MainMenuIntentService");
		menuItems = new ArrayList<MenuItem>();
	}
		

	@Override
	protected void onHandleIntent(Intent intent) {
		fetchMenuItems();
		createSettingsMenu();
		sendMessageResults(intent);
	}
		
	
	protected void fetchMenuItems() {
		Log.i("MainMenuInterntService", "Retrieving Menu Items");
		
		if (factory != null) {
			loadMenuItems();
		}
	}
	
	protected void loadMenuItems() {
		// Fetch TV Shows and Movies
		try {
			MediaContainer mc = factory.retrieveSections();
			List<Directory> dirs = mc.getDirectories();
			
			if (dirs != null) {
				for (Directory item : dirs) {
					MenuItem m = new MenuItem();
					m.setTitle(item.getTitle());
					m.setType(item.getType());
					m.setSection(item.getKey());
					menuItems.add(m);
				}
			} 
		} catch (Exception e) {
		  Log.e("MainMenuIntentService", "Unable to comminicate to server at " + factory.baseURL(), e);
		} 
	}
	
	/**
	 * Create the settings MenuItem since there is no option
	 * to retrieve this from Plex itself.
	 *  
	 */
	protected void createSettingsMenu() {
		MenuItem settingsMenuItem = new MenuItem();
		settingsMenuItem.setTitle(SETTINGS_MENU);
		settingsMenuItem.setType(SETTINGS_TYPE);
		settingsMenuItem.setSection(SETTINGS_SECTION_KEY);
		menuItems.add(settingsMenuItem);
	}


	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = menuItems;
			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e(getClass().getName(), "Unable to send message", ex);
			}
		}
		
	}
	
}
