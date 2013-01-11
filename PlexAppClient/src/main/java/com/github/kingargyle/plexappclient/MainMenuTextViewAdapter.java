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

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;

public class MainMenuTextViewAdapter extends BaseAdapter {
	
	/** The parent context */
	private Context myContext;
	private View mainView;
	
	// Put some images to project-folder: /res/drawable/
	// format: jpg, gif, png, bmp, ...
	private String[] menuItems = { "Movies", "TV Shows", "Music", "Settings"};

	/** Simple Constructor saving the 'parent' context. */
	public MainMenuTextViewAdapter(Context c) {
		this.myContext = c;
	}
	
	public MainMenuTextViewAdapter(Context c, View v) {
		this.myContext = c;
		this.mainView = v;
	}

	// inherited abstract methods - must be implemented
	// Returns count of images, and individual IDs
	public int getCount() {
		return this.menuItems.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	// Returns a new GalleryTextView to be displayed,
	public View getView(int position, View convertView, 
			ViewGroup parent) {
		
		// This is hackish but this is for proof of conept purposes
		MainMenuTextView v = null;
		
		if (position == 0) {
			v = new MainMenuTextView(this.myContext, R.drawable.background_movie_art);
		} else if (position == 1) {
			v = new MainMenuTextView(this.myContext, R.drawable.hddvd);
		} else {
			v = new MainMenuTextView(this.myContext, R.drawable.plexapp);
		}
		
		v.setText(menuItems[position]);
		v.setTextSize(30);
		v.setGravity(Gravity.CENTER_VERTICAL);
		v.setLayoutParams(new Gallery.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));

		return v;
	}
}
