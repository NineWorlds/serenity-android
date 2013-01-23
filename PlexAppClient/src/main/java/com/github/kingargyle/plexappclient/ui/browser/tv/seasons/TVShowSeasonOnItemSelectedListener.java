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

package com.github.kingargyle.plexappclient.ui.browser.tv.seasons;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.ui.browser.tv.TVShowBannerInfo;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class TVShowSeasonOnItemSelectedListener implements
		OnItemSelectedListener {

	private View bgLayout;
	private Activity context;
	private ImageManager imageManager;
	private View previous;
	private ExecutorService imageExecutorService;
	private static final int MAX_IMAGE_THREADS = 5;


	/**
	 * 
	 */
	public TVShowSeasonOnItemSelectedListener(View bgv, Activity activity) {
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
		TVShowSeasonImageView mpiv = (TVShowSeasonImageView) v;

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.refreshDrawableState();
		}

		previous = v;

		v.setBackgroundColor(Color.BLUE);
		v.setPadding(5, 5, 5, 5);
		v.refreshDrawableState();
		
		TextView seasonsTitle = (TextView) context.findViewById(R.id.tvShowSeasonsTitle);
		seasonsTitle.setText(mpiv.getPosterInfo().getTitle());

		changeBackgroundImage(v);

	}


	/**
	 * Change the background image of the activity.
	 * 
	 * Should be a background activity
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {
		
		TVShowSeasonImageView mpiv = (TVShowSeasonImageView) v;
		TVShowBannerInfo mi = mpiv.getPosterInfo();

		if (mi.getBackgroundURL() != null) {
			ImageLoader im = new ImageLoader(mi);
			imageExecutorService.submit(im);			
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
	
	protected class ImageLoader implements Runnable {

		private TVShowBannerInfo mpi;

		/**
		 * 
		 */
		public ImageLoader(TVShowBannerInfo mpi) {
			this.mpi = mpi;
		}

		/**
		 * Call and fetch an image directly.
		 */
		public void run() {

			CacheManager cm = imageManager.getCacheManager();
			Bitmap bm = cm.get(mpi.getBackgroundURL(), 1280, 720);

			if (bm == null) {
				FileManager fm = imageManager.getFileManager();
				File f = fm.getFile(mpi.getBackgroundURL());
				LoaderSettings settings = SerenityApplication
						.getLoaderSettings();
				if (!f.exists()) {
					settings.getNetworkManager().retrieveImage(
							mpi.getBackgroundURL(), f);
				}

				BitmapUtil bmu = SerenityApplication.getLoaderSettings()
						.getBitmapUtil();
				bm = bmu.decodeFile(f, 1280, 720);
			}

			Activity activity = (Activity) bgLayout.getContext();
			activity.runOnUiThread(new BitmapDisplayer(bm));
		}
	}

	protected class BitmapDisplayer implements Runnable {

		private Bitmap bm;

		/**
		 * 
		 */
		public BitmapDisplayer(Bitmap bm) {
			this.bm = bm;
		}

		public void run() {
			if (bm == null) {
				bgLayout.setBackgroundResource(R.drawable.tvshows);
				return;
			}

			BitmapDrawable bmd = new BitmapDrawable(bm);
			bgLayout.setBackgroundDrawable(bmd);
		}

	}	
}
