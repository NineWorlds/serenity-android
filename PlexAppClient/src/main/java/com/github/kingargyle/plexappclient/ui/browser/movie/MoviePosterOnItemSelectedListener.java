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

package com.github.kingargyle.plexappclient.ui.browser.movie;

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
import android.widget.TextView;

/**
 * @author dcarver
 *
 */
public class MoviePosterOnItemSelectedListener implements
		OnItemSelectedListener {
	
	private View bgLayout;
	private Activity context;
	private PlexAppImageManager imageManager;
	private View previous;
	
	/**
	 * 
	 */
	public MoviePosterOnItemSelectedListener(View bgv, Activity activity) {
		bgLayout = bgv;
		context = activity;
		
		imageManager = SerenityApplication.getImageManager();
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(AdapterView<?> av, View v, int position,
			long id) {
		
		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.refreshDrawableState();
		}
		
		previous = v;
		
		v.setBackgroundColor(Color.BLUE);
		v.setPadding(5, 5, 5, 5);
		v.refreshDrawableState();
		
		createMovieDetail((MoviePosterImageView) v);
		createInfographicDetails(position);
		changeBackgroundImage(v);
		
	}
	
	private void createMovieDetail(MoviePosterImageView v) {
		TextView castinfo = (TextView) context.findViewById(R.id.movieCastInfo);
		castinfo.setText(v.getPosterInfo().getCastInfo());
		
		TextView summary = (TextView) context.findViewById(R.id.movieSummary);
		summary.setText(v.getPosterInfo().getPlotSummary());
		
		TextView title = (TextView) context.findViewById(R.id.movieBrowserPosterTitle);
		title.setText(v.getPosterInfo().getTitle());
	}

	/**
	 * Change the background image of the activity.
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {
		MoviePosterImageView mpiv = (MoviePosterImageView) v;
		MoviePosterInfo mi = mpiv.getPosterInfo();
		
		if (mi.getBackgroundURL() != null) {
			Bitmap bm = imageManager.getImage(mi.getBackgroundURL(), 1280, 720);
			BitmapDrawable bmd = new BitmapDrawable(bm);
			
			bgLayout.setBackgroundDrawable(bmd);
		}
	}

	/**
	 * Create the images representing info such as sound, ratings, etc
	 * based on the currently selected movie poster.
	 * 
	 * @param position
	 */
	private void createInfographicDetails(int position) {
		LinearLayout infographicsView = (LinearLayout) context.findViewById(R.id.movieInfoGraphicLayout);
		infographicsView.removeAllViews();
		
		ImageView soundView = new ImageView(context);
		soundView.setImageResource(R.drawable.dolbydigital);
		soundView.setScaleType(ScaleType.FIT_XY);
		soundView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		infographicsView.addView(soundView);
		infographicsView.refreshDrawableState();
		
		if (position == 1) {
			soundView = new ImageView(context);
			soundView.setImageResource(R.drawable.mpaa_r);
			soundView.setScaleType(ScaleType.FIT_XY);
			soundView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			infographicsView.addView(soundView);
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
