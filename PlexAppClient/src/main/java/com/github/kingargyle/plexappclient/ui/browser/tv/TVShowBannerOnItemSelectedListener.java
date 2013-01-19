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

package com.github.kingargyle.plexappclient.ui.browser.tv;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.imagecache.PlexAppImageManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class TVShowBannerOnItemSelectedListener implements
		OnItemSelectedListener {

	private View bgLayout;
	private Activity context;
	private PlexAppImageManager imageManager;
	private View previous;

	/**
	 * 
	 */
	public TVShowBannerOnItemSelectedListener(View bgv, Activity activity) {
		bgLayout = bgv;
		context = activity;

		imageManager = SerenityApplication.getImageManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(AdapterView<?> av, View v, int position, long id) {

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.refreshDrawableState();
		}

		previous = v;

		v.setBackgroundColor(Color.BLUE);
		v.setPadding(5, 5, 5, 5);
		v.refreshDrawableState();

		createTVShowDetail((TVShowBannerImageView) v);
		changeBackgroundImage(v);

	}

	private void createTVShowDetail(TVShowBannerImageView v) {

		TextView summary = (TextView) context.findViewById(R.id.tvShowSeriesSummary);
		summary.setText(v.getPosterInfo().getPlotSummary());

		TextView title = (TextView) context
				.findViewById(R.id.tvBrowserTitle);
		title.setText(v.getPosterInfo().getTitle());
	}

	/**
	 * Change the background image of the activity.
	 * 
	 * Should be a background activity
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {
		
        //FIXME: Run background image changes in a non-ui thread.
		TVShowBannerImageView mpiv = (TVShowBannerImageView) v;
		TVShowBannerInfo mi = mpiv.getPosterInfo();

		if (mi.getBackgroundURL() != null) {
			Bitmap bm = imageManager.getImage(mi.getBackgroundURL(), 1280, 720);
			BitmapDrawable bmd = new BitmapDrawable(bm);
			bgLayout.setBackgroundDrawable(bmd);
			
			Bitmap tbm = imageManager.getImage(mi.getThumbNailURL(), 400, 200);
			
			ImageView showImage = (ImageView) context.findViewById(R.id.tvShowImage);			
			showImage.setScaleType(ScaleType.FIT_XY);
			showImage.setLayoutParams(new LinearLayout.LayoutParams(400, 500));
			showImage.setImageDrawable(new BitmapDrawable(tbm));
			
		}
		
	}

	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
