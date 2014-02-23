/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver
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

package us.nineworlds.serenity.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.MenuItem;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Represents the meta data returned for the various libraries in Plex. Used to
 * construct the main menu items for Serenity.
 * 
 * @author davidcarver
 * 
 */
public class MenuMediaContainer extends AbstractMediaContainer {

	private static final String SETTINGS_SECTION_KEY = "0";
	private static final String SETTINGS_TYPE = "settings";
	private static final String SEARCH_TYPE = "search";
	private static final String OPTIONS_TYPE = "options";
	protected ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
	protected Context context;

	/**
	 * @param mc
	 */
	public MenuMediaContainer(MediaContainer mc) {
		super(mc);
	}

	public MenuMediaContainer(Context c) {
		super(null);
		context = c;
	}

	public MenuMediaContainer(MediaContainer mc, Context c) {
		super(mc);
		context = c;
	}

	public List<MenuItem> createMenuItems() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		List<Directory> dirs = mc.getDirectories();

		if (dirs != null) {
			for (Directory item : dirs) {
				if (isImplemented(item)) {
					boolean musicEnabled = preferences.getBoolean(
							"plex_music_library", false);
					if (musicEnabled == false
							&& "artist".equals(item.getType())) {
						continue;
					}

					MenuItem m = new MenuItem();
					m.setTitle(item.getTitle());
					m.setType(item.getType());

					m.setSection(item.getKey());
					menuItems.add(m);
				}
			}
		}

		if (!menuItems.isEmpty()) {
			menuItems.add(createSearchMenu());
		}

		menuItems.add(createSettingsMenu());
		menuItems.add(createOptionsMenu());

		return menuItems;
	}

	protected boolean isImplemented(Directory item) {
		if ("photos".equals(item.getType()) || "photo".equals(item.getType())) {
			return false;
		}
		return true;
	}

	/**
	 * Create the settings MenuItem since there is no option to retrieve this
	 * from Plex itself.
	 * 
	 */
	public MenuItem createSettingsMenu() {
		MenuItem settingsMenuItem = new MenuItem();
		settingsMenuItem.setTitle(context.getString(R.string.settings));
		settingsMenuItem.setType(SETTINGS_TYPE);
		settingsMenuItem.setSection(SETTINGS_SECTION_KEY);
		return settingsMenuItem;
	}

	/**
	 * Create the settings MenuItem since there is no option to retrieve this
	 * from Plex itself.
	 * 
	 */
	public MenuItem createSearchMenu() {
		MenuItem searchMenuItem = new MenuItem();
		searchMenuItem.setTitle(context.getString(R.string.search));
		searchMenuItem.setType(SEARCH_TYPE);
		searchMenuItem.setSection(SETTINGS_SECTION_KEY);
		return searchMenuItem;
	}

	/**
	 * Create the settings MenuItem since there is no option to retrieve this
	 * from Plex itself.
	 * 
	 */
	public MenuItem createOptionsMenu() {
		MenuItem optionsMenuItem = new MenuItem();
		optionsMenuItem.setTitle(context.getString(R.string.options));
		optionsMenuItem.setType(OPTIONS_TYPE);
		optionsMenuItem.setSection(SETTINGS_SECTION_KEY);
		return optionsMenuItem;
	}

}
