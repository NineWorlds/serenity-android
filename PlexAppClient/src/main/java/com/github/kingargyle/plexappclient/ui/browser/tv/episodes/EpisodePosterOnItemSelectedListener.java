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

package com.github.kingargyle.plexappclient.ui.browser.tv.episodes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.imageloader.BackgroundImageLoader;
import com.github.kingargyle.plexappclient.core.model.VideoContentInfo;
import com.github.kingargyle.plexappclient.ui.util.ImageInfographicUtils;
import com.github.kingargyle.plexappclient.ui.views.SerenityPosterImageView;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.cache.CacheManager;

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
public class EpisodePosterOnItemSelectedListener implements
		OnItemSelectedListener {

	/**
	 * 
	 */
	private static final int MAX_IMAGE_THREADS = 5;
	private View bgLayout;
	private Activity context;
	private ImageManager imageManager;
	private View previous;
	

	// Sets up a Executor service for handling image loading
	private ExecutorService imageExecutorService;

	/**
	 * 
	 */
	public EpisodePosterOnItemSelectedListener(View bgv, Activity activity) {
		bgLayout = bgv;
		context = activity;
		imageManager = SerenityApplication.getImageManager();
		imageExecutorService = Executors.newFixedThreadPool(MAX_IMAGE_THREADS);
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

		createMovieDetail((SerenityPosterImageView) v);
		createMovieMetaData((SerenityPosterImageView) v);
		createInfographicDetails((SerenityPosterImageView) v);
		changeBackgroundImage(v);

	}

	private void createMovieDetail(SerenityPosterImageView v) {
		TextView summary = (TextView) context.findViewById(R.id.movieSummary);
		summary.setText(v.getPosterInfo().getPlotSummary());

		TextView title = (TextView) context
				.findViewById(R.id.movieBrowserPosterTitle);
		title.setText(v.getPosterInfo().getTitle());
	}
	
	private void createMovieMetaData(SerenityPosterImageView v) {
		SerenityPosterImageView mpiv = (SerenityPosterImageView) v;
		VideoContentInfo mi = mpiv.getPosterInfo();
		TextView ty  = (TextView) context.findViewById(R.id.video_year);
		TextView tg  = (TextView) context.findViewById(R.id.video_genre);
		TextView tw  = (TextView) context.findViewById(R.id.video_writers);
		TextView td  = (TextView) context.findViewById(R.id.video_directors);

		ty.setText("Unknown");
		tg.setText("Unknown");
		tw.setText("Unknown");
		td.setText("Unknown");
		
		if (mi.getYear() != null) {
			ty.setText(mi.getYear());
		}
		
		if (mi.getGenres() != null) {
			String details = "";
			for (String genre : mi.getGenres()) {
				details = details + genre + "\r\n";
			}
			tg.setText(details);
		}
		
		if (mi.getWriters() != null) {
			String details = "";
			for (String writers : mi.getWriters()) {
				details = details + writers + "\r\n";
			}
			tw.setText(details);			
		}
		
		if (mi.getDirectors() != null) {
			String details = "";
			for (String directors : mi.getDirectors()) {
				details = details + directors + "\r\n";
			}
			td.setText(details);			
		}
		
	}
	

	/**
	 * Change the background image of the activity.
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {
		SerenityPosterImageView epiv = (SerenityPosterImageView) v;
		VideoContentInfo ei = epiv.getPosterInfo();

		if (ei.getBackgroundURL() == null) {
			return;
		}
		
		CacheManager cm = imageManager.getCacheManager();

		Bitmap bm = cm.get(ei.getBackgroundURL(), 1280, 720);
		if (bm == null) {
			imageExecutorService.submit(new BackgroundImageLoader(ei.getBackgroundURL(), bgLayout, R.drawable.tvshows ));
			return;
		}

		BitmapDrawable bmd = new BitmapDrawable(bm);
		bgLayout.setBackgroundDrawable(bmd);
	}


	/**
	 * Create the images representing info such as sound, ratings, etc based on
	 * the currently selected movie poster.
	 * 
	 * @param position
	 */
	private void createInfographicDetails(SerenityPosterImageView v) {
		LinearLayout infographicsView = (LinearLayout) context
				.findViewById(R.id.movieInfoGraphicLayout);
		infographicsView.removeAllViews();
		VideoContentInfo epi = v.getPosterInfo();
		
		ImageView viewed = new ImageView(context);
		viewed.setScaleType(ScaleType.FIT_XY);
		viewed.setLayoutParams(new LayoutParams(80, 58));
		
		if (epi.getViewCount() > 0) {
			viewed.setImageResource(R.drawable.watched_small);
		} else {
			viewed.setImageResource(R.drawable.unwatched_small);
		}
		infographicsView.addView(viewed);
		
		ImageInfographicUtils imageUtils = new ImageInfographicUtils(158, 58);
		

		ImageView acv = imageUtils.createAudioCodecImage(epi.getAudioCodec(), v.getContext());		
		if (acv != null) {
			infographicsView.addView(acv);
		}
		
		ImageView achannelsv = imageUtils.createAudioChannlesImage(epi.getAudioChannels(), v.getContext());		
		if (achannelsv != null) {
			infographicsView.addView(achannelsv);
		}
		

		ImageView resv = imageUtils.createVideoResolutionImage(epi.getVideoResolution(), v.getContext());
		if (resv != null) {
			infographicsView.addView(resv);
		}
		
		ImageInfographicUtils imageUtilsAR = new ImageInfographicUtils(100, 58);
		ImageView aspectv = imageUtilsAR.createAspectRatioImage(epi.getAspectRatio(), v.getContext());
		if (aspectv != null) {
			infographicsView.addView(aspectv);
		}
		

		// Currently can't find appropriate logo for this.
		//ImageView crv = setContentRating(epi.getContentRating());
		//infographicsView.addView(crv);

		infographicsView.refreshDrawableState();
	}


	public void onNothingSelected(AdapterView<?> av) {

	}

}
