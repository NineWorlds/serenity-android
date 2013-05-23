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

import us.nineworlds.serenity.core.imageloader.BitmapDisplayer;
import us.nineworlds.serenity.core.imageloader.MainMenuBackgroundBitmapDisplayer;
import us.nineworlds.serenity.ui.views.MainMenuTextView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class GalleryOnItemSelectedListener implements OnItemSelectedListener {

	private View mainGalleryBackgroundView;
	private MainMenuTextView preSelected;

	public GalleryOnItemSelectedListener(View v) {
		mainGalleryBackgroundView = v;
	}

	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long arg3) {
		if (v instanceof MainMenuTextView) {
			MainMenuTextView tv = (MainMenuTextView) v;
			//mainView.setBackgroundResource(tv.getBackgroundImageId());
						
			Bitmap background = BitmapFactory.decodeResource(v.getContext().getResources(), tv.getBackgroundImageId());
			new ImageLoader((Activity)v.getContext(), mainGalleryBackgroundView, background).doInBackground();
			//mainView.setBackgroundDrawable(new BitmapDrawable(background));
			// mainView.refreshDrawableState();

			tv.setTextSize(tv.getTextSize() + 10);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setTextColor(Color.parseColor("#F0F0F0"));
			tv.setGravity(Gravity.CENTER_VERTICAL);
			if (preSelected != null) {
				preSelected.setTextSize(30);
				preSelected.setTypeface(null, Typeface.NORMAL);
				preSelected.setGravity(Gravity.CENTER_VERTICAL);
			}
			preSelected = tv;
		}

	}

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
