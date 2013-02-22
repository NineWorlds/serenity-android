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

import com.github.kingargyle.plexappclient.core.model.impl.MenuItem;
import com.github.kingargyle.plexappclient.core.services.MainMenuIntentService;
import com.github.kingargyle.plexappclient.ui.views.MainMenuTextView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Toast;

public class MainMenuTextViewAdapter extends BaseAdapter {

	/** The parent context */
	private static Context myContext;
	
	private static Handler menuItemhandler;
	
	private static MainMenuTextViewAdapter notifyAdapter;
	
	private static ArrayList<MenuItem> menuItems;
	
	private static ProgressDialog pd;

	/** Simple Constructor saving the 'parent' context. */
	public MainMenuTextViewAdapter(Context c) {
		this.myContext = c;
	}

	public MainMenuTextViewAdapter(Context c, View v) {
		this.myContext = c;
		menuItems = new ArrayList<MenuItem>();
		fetchData();
		pd = ProgressDialog.show(myContext, "Build Menus", "Loading Menus");
	}
	
	protected void fetchData() {
		menuItemhandler = new MenuItemHandler();
		Messenger messenger = new Messenger(menuItemhandler);
		Intent intent = new Intent(myContext, MainMenuIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		myContext.startService(intent);
		notifyAdapter = this;
	}
	
	public int getCount() {
		return menuItems.size();
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
		
		return new MainMenuTextView(myContext, R.drawable.serenity_logo2);
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
	
	
	private static class MenuItemHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				menuItems = (ArrayList<MenuItem>) msg.obj;
			}
			if (menuItems.size() == 1) {
				Toast.makeText(myContext, "No Movies or TV Show Libraries found. Use the Plex Web App to add some.", Toast.LENGTH_LONG).show();
			}
			notifyAdapter.notifyDataSetChanged();
			pd.dismiss();
		}
		
	}

}
