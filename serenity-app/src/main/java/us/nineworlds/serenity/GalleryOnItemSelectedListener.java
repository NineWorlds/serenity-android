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

package us.nineworlds.serenity;

import us.nineworlds.serenity.core.imageloader.MainMenuBackgroundBitmapDisplayer;
import us.nineworlds.serenity.ui.views.MainMenuTextView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class GalleryOnItemSelectedListener implements OnItemSelectedListener {

	private View mainGalleryBackgroundView;
	private MainMenuTextView preSelected;
	private Animation fadeIn;

	public GalleryOnItemSelectedListener(View v) {
		mainGalleryBackgroundView = v;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long arg3) {
		if (v instanceof MainMenuTextView) {
			MainMenuTextView tv = (MainMenuTextView) v;
						
			Bitmap background = BitmapFactory.decodeResource(v.getContext().getResources(), tv.getBackgroundImageId());
			new ImageLoader((Activity)v.getContext(), mainGalleryBackgroundView, background).doInBackground();
			
			tv.setTextColor(v.getContext().getResources().getColor(android.R.color.white));
			tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
			

			if (preSelected != null) {
				preSelected.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				preSelected.setTextColor(Color.parseColor("#414141"));
			}
			
			preSelected = tv;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
	
	private class ImageLoader extends AsyncTask<Void, Void, Void> {
		
		Activity context;
		View view;
		Bitmap bm;
		
		public ImageLoader(Activity context, View view, Bitmap bm) {
			this.view = view;
			this.context = context;
			this.bm = bm;
		}

		@Override
		protected Void doInBackground(Void... params) {
			context.runOnUiThread(new MainMenuBackgroundBitmapDisplayer(bm, R.drawable.serenity_bonsai_logo, view));
			return null;
		}
		
	}
	
	
}
