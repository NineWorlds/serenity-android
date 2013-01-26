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

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
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
public class TVShowBannerOnItemSelectedListener implements
		OnItemSelectedListener {

	private View bgLayout;
	private Activity context;
	private ImageManager imageManager;
	private View previous;
	// Sets up a Executor service for handling image loading
	private ExecutorService imageExecutorService;
	private static final int MAX_IMAGE_THREADS = 5;
	private ImageTagFactory imageTagFactory;

	/**
	 * 
	 */
	public TVShowBannerOnItemSelectedListener(View bgv, Activity activity) {
		bgLayout = bgv;
		context = activity;

		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.newInstance(400, 500,
				R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);

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

		createTVShowDetail((TVShowBannerImageView) v);
		changeBackgroundImage(v);

	}

	private void createTVShowDetail(TVShowBannerImageView v) {

		TextView summary = (TextView) context
				.findViewById(R.id.tvShowSeriesSummary);
		summary.setText(v.getPosterInfo().getPlotSummary());

		TextView title = (TextView) context.findViewById(R.id.tvBrowserTitle);
		title.setText(v.getPosterInfo().getTitle());

		TextView genreView = (TextView) context
				.findViewById(R.id.tvShowBrowserGenre);
		List<String> genres = v.getPosterInfo().getGeneres();
		String genreText = "";
		for (String genre : genres) {
			genreText = genreText + genre + "/";
		}
		genreText = genreText.substring(0, genreText.lastIndexOf("/"));
		genreView.setText(genreText);

		TextView watchedUnwatched = (TextView) context
				.findViewById(R.id.tvShowWatchedUnwatched);
		String wu = "Watched: " + v.getPosterInfo().getShowsWatched();
		wu = wu + " Unwatched: " + v.getPosterInfo().getShowsUnwatched();
		watchedUnwatched.setText(wu);

	}

	/**
	 * Change the background image of the activity.
	 * 
	 * Should be a background activity
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {

		TVShowBannerImageView mpiv = (TVShowBannerImageView) v;
		TVShowBannerInfo mi = mpiv.getPosterInfo();

		ImageLoader im = new ImageLoader(mi);
		imageExecutorService.submit(im);

		ImageView showImage = (ImageView) context
				.findViewById(R.id.tvShowImage);
		showImage.setScaleType(ScaleType.FIT_XY);
		showImage.setLayoutParams(new LinearLayout.LayoutParams(400, 500));

		if (mi.getThumbNailURL() != null) {
			showImage.setTag(imageTagFactory.build(mi.getThumbNailURL(),
					context));
		} else {
			showImage.setTag(imageTagFactory.build(SerenityApplication
					.getPlexFactory().baseURL()
					+ ":/resources/movie-fanart.jpg", context));
		}

		imageManager.getLoader().load(showImage);

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
				bgLayout.setBackgroundResource(R.drawable.movies);
				return;
			}

			BitmapDrawable bmd = new BitmapDrawable(bm);
			bgLayout.setBackgroundDrawable(bmd);
		}

	}

}
