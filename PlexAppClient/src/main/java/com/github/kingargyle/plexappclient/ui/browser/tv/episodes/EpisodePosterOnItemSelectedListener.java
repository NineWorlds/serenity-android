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
		createInfographicDetails((SerenityPosterImageView) v);
		changeBackgroundImage(v);

	}

	private void createMovieDetail(SerenityPosterImageView v) {
		TextView castinfo = (TextView) context.findViewById(R.id.movieCastInfo);
		castinfo.setText(v.getPosterInfo().getCastInfo());

		TextView summary = (TextView) context.findViewById(R.id.movieSummary);
		summary.setText(v.getPosterInfo().getPlotSummary());

		TextView title = (TextView) context
				.findViewById(R.id.movieBrowserPosterTitle);
		title.setText(v.getPosterInfo().getTitle());
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
		

		ImageView acv = setAudioCodec(epi.getAudioCodec());		
		if (acv != null) {
			infographicsView.addView(acv);
		}

		ImageView resv = setVideoResolution(epi.getVideoResolution());
		if (resv != null) {
			infographicsView.addView(resv);
		}
		
		ImageView aspectv = setAspectRatio(epi.getAspectRatio());
		if (aspectv != null) {
			infographicsView.addView(aspectv);
		}
		

		// Currently can't find appropriate logo for this.
		//ImageView crv = setContentRating(epi.getContentRating());
		//infographicsView.addView(crv);

		infographicsView.refreshDrawableState();
	}

	protected ImageView setContentRating(String contentRating) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		v.setLayoutParams(new LayoutParams(154, 58));
		

		if ("G".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_general);
			return v;
		}

		if ("PG".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_pg);
			return v;
		}

		if ("PG-13".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_pg13);
			return v;
		}

		if ("R".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_restricted);
			return v;
		}

		if ("NC-17".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_nc17);
			return v;
		}

		v.setImageResource(R.drawable.mpaa_notrated);
		return v;

	}
	
	protected ImageView setAspectRatio(String ratio) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		v.setLayoutParams(new LayoutParams(100, 58));

		if ("1.33".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_33);
			return v;
		}

		if ("1.66".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_66);
			return v;
		}
		
		if ("1.78".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_78);
			return v;
		}
		
		if ("1.85".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_85);
			return v;
		}
		
		if ("2.20".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_2_20);
			return v;
		}
		
		if ("2.35".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_2_35);
			return v;
		}

		return null;

	}	
	

	protected ImageView setAudioCodec(String codec) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		v.setLayoutParams(new LayoutParams(154, 58));

		if ("aac".equals(codec)) {
			v.setImageResource(R.drawable.aac);
			return v;
		}

		if ("ac3".equals(codec)) {
			v.setImageResource(R.drawable.dolbydigital);
			return v;
		}

		if ("aif".equals(codec)) {
			v.setImageResource(R.drawable.aif);
			return v;
		}

		if ("aifc".equals(codec)) {
			v.setImageResource(R.drawable.aifc);
			return v;
		}

		if ("aiff".equals(codec)) {
			v.setImageResource(R.drawable.aiff);
			return v;
		}

		if ("ape".equals(codec)) {
			v.setImageResource(R.drawable.ape);
			return v;
		}

		if ("avc".equals(codec)) {
			v.setImageResource(R.drawable.avc);
			return v;
		}

		if ("cdda".equals(codec)) {
			v.setImageResource(R.drawable.cdda);
			return v;
		}

		if ("dca".equals(codec)) {
			v.setImageResource(R.drawable.dca);
			return v;
		}

		if ("dts".equals(codec)) {
			v.setImageResource(R.drawable.dts);
			return v;
		}

		if ("eac3".equals(codec)) {
			v.setImageResource(R.drawable.eac3);
			return v;
		}

		if ("flac".equals(codec)) {
			v.setImageResource(R.drawable.flac);
			return v;
		}

		if ("mp1".equals(codec)) {
			v.setImageResource(R.drawable.mp1);
			return v;
		}

		if ("mp2".equals(codec)) {
			v.setImageResource(R.drawable.mp2);
			return v;
		}

		if ("mp3".equals(codec)) {
			v.setImageResource(R.drawable.mp3);
			return v;
		}

		if ("ogg".equals(codec)) {
			v.setImageResource(R.drawable.ogg);
			return v;
		}

		if ("wma".equals(codec)) {
			v.setImageResource(R.drawable.wma);
			return v;
		}

		return null;

	}

	protected ImageView setVideoCodec(String codec) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		v.setLayoutParams(new LayoutParams(154, 58));
		

		if ("divx".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.divx);
			return v;
		}

		if ("vc-1".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.vc_1);
			return v;
		}

		if ("h264".equalsIgnoreCase(codec) || "mpeg4".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.h264);
			return v;
		}

		if ("mpeg2".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.mpeg2video);
			return v;
		}

		if ("mpeg1".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.mpeg1video);
			return v;
		}

		if ("xvid".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.xvid);
			return v;
		}

		return null;

	}

	protected ImageView setVideoResolution(String res) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		v.setLayoutParams(new LayoutParams(154, 58));
		

		if ("sd".equalsIgnoreCase(res) || "480".equalsIgnoreCase(res)
				|| "540".equalsIgnoreCase(res) || "576".equalsIgnoreCase(res)) {
			v.setImageResource(R.drawable.sd);
			return v;
		}

		if ("720".equalsIgnoreCase(res)) {
			v.setImageResource(R.drawable.res720);
			return v;
		}

		if ("1080".equalsIgnoreCase(res)) {
			v.setImageResource(R.drawable.res1080);
			return v;
		}

		return null;

	}

	public void onNothingSelected(AdapterView<?> av) {

	}

}
