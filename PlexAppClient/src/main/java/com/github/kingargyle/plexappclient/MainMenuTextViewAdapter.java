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

import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.config.IConfiguration;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.core.ServerConfig;
import com.github.kingargyle.plexappclient.ui.views.MainMenuTextView;
import com.github.kingargyle.plexapp.model.impl.Directory;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Toast;

public class MainMenuTextViewAdapter extends BaseAdapter {

	/** The parent context */
	private Context myContext;
	private View mainView;
	private PlexappFactory factory;
	private ArrayList<MenuItem> menuItems;

	/** Simple Constructor saving the 'parent' context. */
	public MainMenuTextViewAdapter(Context c) {
		this.myContext = c;
	}

	public MainMenuTextViewAdapter(Context c, View v) {
		this.myContext = c;
		this.mainView = v;
		menuItems = new ArrayList<MenuItem>();

		initializePlexFactory();
		fetchMenuItems();
	}
	
	protected void fetchMenuItems() {
		
		Toast.makeText(myContext, "Retrieving Menu Items", Toast.LENGTH_SHORT).show();
		
		if (factory != null) {
			loadMenuItems();
		}
		createSettingsMenu();
	}
	
	void loadMenuItems() {
		// Fetch TV Shows and Movies
		try {
			MediaContainer mc = factory.retrieveSections();
			List<Directory> dirs = mc.getDirectories();
			for (Directory item : dirs) {
				MenuItem m = new MenuItem();
				m.setTitle(item.getTitle());
				m.setType(item.getType());
				m.setSection(item.getKey());
				menuItems.add(m);
			}
		} catch (Exception e) {
		  Toast.makeText(myContext, "Unable to comminicate with server at " + factory.baseURL() + ". Reason: " + e.getMessage(), Toast.LENGTH_LONG).show();
		  Log.e("MainMenuTextAdapter", "Unable to comminicate to server at " + factory.baseURL(), e);
		} 
	}
	

	/**
	 * Create the settings MenuItem since there is no option
	 * to retrieve this from Plex itself.
	 *  
	 */
	void createSettingsMenu() {
		MenuItem settingsMenuItem = new MenuItem();
		settingsMenuItem.setTitle("Settings");
		settingsMenuItem.setType("settings");
		settingsMenuItem.setSection("0");
		menuItems.add(settingsMenuItem);
	}

	/**
	 * Initialize the REST interface factor to retrieve data from the server.
	 */
	private void initializePlexFactory() {
		try {
			IConfiguration config = ServerConfig.getInstance(myContext);
			factory = PlexappFactory.getInstance(config);
		} catch (Exception ex) {
			Log.w("Unable to initialize server config or non existing server address", ex);
		}
	}

	public int getCount() {
		return this.menuItems.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MenuItem menuItem = menuItems.get(position);
		
		MainMenuTextView v = createView(menuItem);
		setDefaults(menuItem.getTitle(), v);

		return v;
	}

	/**
	 * Create a Main Menu item view for the corresponding MenuItem. If
	 * an appropriate type can not be found a default MainMenuTextView
	 * will be created.
	 * 
	 * @param v
	 * @param menuItem
	 * @return
	 */
	MainMenuTextView createView(MenuItem menuItem) {
		MainMenuTextView v = null;
		if ("movie".equals(menuItem.getType())) {
			v = new MainMenuTextView(this.myContext,
					R.drawable.movies);
			v.setLibraryKey(menuItem.getSection());
			v.setActivityType(menuItem.getType());
			return v;
		}
		
		if ("show".equals(menuItem.getType())) {
			v = new MainMenuTextView(this.myContext, R.drawable.tvshows);
			v.setLibraryKey(menuItem.getSection());
			v.setActivityType(menuItem.getType());
			return v;
		}
		
		if ("settings".equals(menuItem.getType())) {
			v = new MainMenuTextView(this.myContext, R.drawable.settings);
			v.setLibraryKey("0");
			v.setActivityType(menuItem.getType());
			return v;
		}
		
		return new MainMenuTextView(myContext, R.drawable.plexapp);
	}

	/**
	 * Sets the default values for the view passed to it.
	 * @param position
	 * @param v
	 */
	void setDefaults(String title, MainMenuTextView v) {
		v.setText(title);
		v.setTextSize(30);
		v.setGravity(Gravity.CENTER_VERTICAL);
		v.setLayoutParams(new Gallery.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	}

	private class MenuItem {
		private String type;
		private String title;
		private String section;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSection() {
			return section;
		}

		public void setSection(String section) {
			this.section = section;
		}

	}
}
