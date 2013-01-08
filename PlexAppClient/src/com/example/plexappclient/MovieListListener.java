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

package com.example.plexappclient;

import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.google.tv.leftnavbar.LeftNavBar;

/**
 * @author dcarver
 *
 */
public class MovieListListener implements LeftNavBar.TabListener {
	
	private ImageView moviesImageView = null;
	
	public MovieListListener(ImageView imageView) {
		
		moviesImageView = imageView;
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		onTabSelected(tab, ft);
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Bitmap bitMap = PlexAppMainActivity.getBitmapFromURL("http://192.168.0.108:32400/:/resources/movie-fanart.jpg");
		moviesImageView.setImageBitmap(bitMap);
		
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	

}
