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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.imageloader.SerenityBackgroundLoaderListener;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.ImageUtils;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Display selected TV Show Information.
 * 
 * @author dcarver
 * 
 */
public class TVShowGalleryOnItemSelectedListener implements
		OnItemSelectedListener {

	private static final String SEPERATOR = "/";
	private View bgLayout;
	private SerenityMultiViewVideoActivity context;
	private ImageLoader imageLoader;
	private View previous;
	private ImageSize bgImageSize = new ImageSize(1280, 720);
	private SeriesContentInfo info;

	/**
	 * 
	 */
	public TVShowGalleryOnItemSelectedListener(View bgv, SerenityMultiViewVideoActivity activity) {
		bgLayout = bgv;
		context = activity;

		imageLoader = SerenityApplication.getImageLoader();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> av, View v, int position, long id) {

		info = (SeriesContentInfo) av.getItemAtPosition(position);
		
		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
		}

		previous = v;

		v.setPadding(5, 5, 5, 5);
		
		ImageView imageView = (ImageView) v.findViewById(R.id.posterImageView);

		createTVShowDetail(imageView);
		changeBackgroundImage(imageView);

	}

	private void createTVShowDetail(ImageView v) {

		TextView summary = (TextView) context
				.findViewById(R.id.tvShowSeriesSummary);
		String plotSummary = info.getSummary();
		if (plotSummary == null) {
			summary.setText("");
		} else {
			summary.setText(plotSummary);
		}

		TextView title = (TextView) context.findViewById(R.id.tvBrowserTitle);
		title.setText(info.getTitle());

		TextView genreView = (TextView) context
				.findViewById(R.id.tvShowBrowserGenre);
		List<String> genres = info.getGeneres();
		StringBuilder genreText = new StringBuilder();
		if (genres != null && !genres.isEmpty()) {
			for (String genre : genres) {
				genreText.append(genre).append(SEPERATOR);
			}
			genreText = genreText.replace(0, genreText.length(),
					genreText.substring(0, genreText.lastIndexOf(SEPERATOR)));
		}
		genreView.setText(genreText.toString());

		TextView watchedUnwatched = (TextView) context
				.findViewById(R.id.tvShowWatchedUnwatched);

		String watched = info.getShowsWatched();
		String unwatched = info.getShowsUnwatched();

		String wu = "";
		if (watched != null) {
			wu = context.getString(R.string.watched_) + " " + watched;
		}

		if (unwatched != null) {
			wu = wu + " " + context.getString(R.string.unwatched_) + " " + unwatched;

		}

		watchedUnwatched.setText(wu);
		
		ImageView imageView = (ImageView) context.findViewById(R.id.tvShowRating);
		ImageInfographicUtils infog = new ImageInfographicUtils(74, 40);
		
		int w = ImageUtils.getDPI(74, (Activity)v.getContext());
		int h = ImageUtils.getDPI(40, (Activity)v.getContext());
		
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
		params.topMargin = 10;
		params.rightMargin = 5;			
		imageView.setLayoutParams(params);
		
		ImageView content = infog.createTVContentRating(info.getContentRating(), context);
		imageView.setImageDrawable(content.getDrawable());
		imageView.setScaleType(ScaleType.FIT_XY);
		
		ImageView studiov = (ImageView) context.findViewById(R.id.tvShowStudio);
		if (info.getStudio() != null ) {
			studiov.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams sparams = new LinearLayout.LayoutParams(w, h);
			sparams.rightMargin = 5;
			sparams.topMargin = 10;
			sparams.leftMargin = 0;
			imageView.setLayoutParams(params);
			
			studiov.setLayoutParams(sparams);
			String studio = info.getStudio();
			studio = fixStudio(studio);
			PlexappFactory factory = SerenityApplication.getPlexFactory();
			String studioUrl = factory.getMediaTagURL("studio", studio, info.getMediaTagIdentifier());
			SerenityApplication.getImageLoader().displayImage(studioUrl, studiov);
		} else {
			studiov.setVisibility(View.GONE);
		}
		
		RatingBar ratingBar = (RatingBar) context.findViewById(R.id.tvRatingbar);
		ratingBar.setMax(4);
		ratingBar.setIsIndicator(true);
		ratingBar.setStepSize(0.1f);
		ratingBar.setNumStars(4);
		ratingBar.setPadding(0, 0, 5, 0);
		double rating = info.getRating();
		ratingBar.setRating((float) (rating / 2.5));
		
		
	}
	
	private String fixStudio(String studio) {
		if ("FOX".equals(studio)) {
			return "Fox";
		}
		if ("Starz!".equals(studio)) {
			return "Starz";
		}
		return studio;
	}

	/**
	 * Change the background image of the activity.
	 * 
	 * Should be a background activity
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {

		SeriesContentInfo mi = info;

		imageLoader.loadImage(mi.getBackgroundURL(), bgImageSize,
				new SerenityBackgroundLoaderListener(bgLayout,
						R.drawable.tvshows));

		if (!context.isPosterLayoutActive()) {
			ImageView showImage = (ImageView) context
					.findViewById(R.id.tvShowImage);
			showImage.setVisibility(View.VISIBLE);
			showImage.setScaleType(ScaleType.FIT_XY);
			int width = ImageUtils.getDPI(250, context);
			int height = ImageUtils.getDPI(350, context);
			showImage.setMaxHeight(height);
			showImage.setMaxWidth(width);
			showImage.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			SerenityApplication.displayImage(mi.getThumbNailURL(), showImage);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
