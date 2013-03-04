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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.imageloader.BackgroundImageLoader;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemSelectedListener;

import us.nineworlds.serenity.R;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.cache.CacheManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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

	private static final String CRLF = "\r\n";
	private static final String DEFAULT_UNKNOWN = "Unknown";
	private static final String DISPLAY_DATE_FORMAT = "MMMMMMMMM d, yyyy";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final int WATCHED_VIEW_ID = 1000;
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

	public void onItemSelected(SerenityAdapterView<?> av, View v, int position, long id) {

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.setBackgroundColor(Color.BLACK);
		}

		previous = v;

		v.setBackgroundColor(Color.CYAN);
		v.setPadding(5, 5, 5, 5);

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
		String epTitle = v.getPosterInfo().getTitle();
		String season = v.getPosterInfo().getSeason();
		String episode = v.getPosterInfo().getEpisodeNumber();
		
		if (season != null && episode != null) {
			epTitle = epTitle + " - " + season + " " + episode;
		}
		
		title.setText(epTitle);
		TextView vte = (TextView) context.findViewById(R.id.videoTextExtra);
		vte.setVisibility(View.INVISIBLE);
		if (v.getPosterInfo().getOriginalAirDate() != null) {
			try {
				Date airDate = new SimpleDateFormat(DATE_FORMAT).parse(v.getPosterInfo().getOriginalAirDate());
				SimpleDateFormat format = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
				String formatedDate = format.format(airDate);
				vte.setVisibility(View.VISIBLE);
				vte.setText("Aired " + formatedDate);
			} catch (ParseException ex) {
				Log.i(getClass().getName(), "Unable to parse date");
			}
					
		}
		
	}
	
	private void createMovieMetaData(SerenityPosterImageView v) {
		SerenityPosterImageView mpiv = (SerenityPosterImageView) v;
		VideoContentInfo mi = mpiv.getPosterInfo();
		TextView ty  = (TextView) context.findViewById(R.id.video_year);
		TextView tg  = (TextView) context.findViewById(R.id.video_genre);
		TextView tw  = (TextView) context.findViewById(R.id.video_writers);
		TextView td  = (TextView) context.findViewById(R.id.video_directors);

		ty.setText(DEFAULT_UNKNOWN);
		tg.setText(DEFAULT_UNKNOWN);
		tw.setText(DEFAULT_UNKNOWN);
		td.setText(DEFAULT_UNKNOWN);
		
		if (mi.getYear() != null) {
			ty.setText(mi.getYear());
		}
		
		if (mi.getGenres() != null) {
			StringBuilder details = new StringBuilder();
			for (String genre : mi.getGenres()) {
				details.append(genre);
				details.append(CRLF);
			}
			tg.setText(details.toString());
		}
		
		if (mi.getWriters() != null) {
			StringBuilder details = new StringBuilder();
			for (String writers : mi.getWriters()) {
				details.append(writers);
				details.append(CRLF);
			}
			tw.setText(details.toString());			
		}
		
		if (mi.getDirectors() != null) {
			StringBuilder details = new StringBuilder();
			for (String directors : mi.getDirectors()) {
				details.append(directors);
				details.append(CRLF);
			}
			td.setText(details.toString());			
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
		viewed.setId(WATCHED_VIEW_ID);
		
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
	}


	public void onNothingSelected(SerenityAdapterView<?> av) {

	}

}
