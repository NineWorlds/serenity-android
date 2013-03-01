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

package us.nineworlds.serenity.ui.browser.tv;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.imageloader.BackgroundImageLoader;
import us.nineworlds.serenity.core.model.impl.AbstractSeriesContentInfo;

import us.nineworlds.serenity.R;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.graphics.Color;
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
		imageTagFactory = ImageTagFactory.newInstance(250, 350,
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
		String plotSummary = v.getPosterInfo().getPlotSummary();
		if (plotSummary == null) {
			summary.setText("");
		} else {
			summary.setText(plotSummary);
		}

		TextView title = (TextView) context.findViewById(R.id.tvBrowserTitle);
		title.setText(v.getPosterInfo().getTitle());

		TextView genreView = (TextView) context
				.findViewById(R.id.tvShowBrowserGenre);
		List<String> genres = v.getPosterInfo().getGeneres();
		String genreText = "";
		if (genres != null && !genres.isEmpty()) {
			for (String genre : genres) {
				genreText = genreText + genre + "/";
			}
			genreText = genreText.substring(0, genreText.lastIndexOf("/"));
		}
		genreView.setText(genreText);

		TextView watchedUnwatched = (TextView) context
				.findViewById(R.id.tvShowWatchedUnwatched);
		
		String watched = v.getPosterInfo().getShowsWatched();
		String unwatched = v.getPosterInfo().getShowsUnwatched();
		
		String wu = "";
		if (watched != null) {
			wu = "Watched: " + watched;
		}
		
		if (unwatched != null) {
			wu = wu + " Unwatched: " + unwatched;
			
		}
		
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
		AbstractSeriesContentInfo mi = mpiv.getPosterInfo();
		
		if (mi.getBackgroundURL() != null) {
			BackgroundImageLoader im = new BackgroundImageLoader(mi.getBackgroundURL(), bgLayout, R.drawable.tvshows);
			imageExecutorService.submit(im);
		} else {
			bgLayout.setBackgroundResource(R.drawable.tvshows);
		}

		ImageView showImage = (ImageView) context
				.findViewById(R.id.tvShowImage);
		showImage.setScaleType(ScaleType.FIT_XY);
		showImage.setLayoutParams(new LinearLayout.LayoutParams(250, 350));

		if (mi.getThumbNailURL() != null) {
			showImage.setTag(imageTagFactory.build(mi.getThumbNailURL(),
					context));
		} else {
			showImage.setTag(imageTagFactory.build(SerenityApplication
					.getPlexFactory().baseURL()
					+ ":/resources/show-fanart.jpg", context));
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

}
