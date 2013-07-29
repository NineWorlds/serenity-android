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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.services.MovieMetaDataRetrievalIntentService;
import us.nineworlds.serenity.ui.listeners.SubtitleSpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemSelectedListener;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class EpisodePosterOnItemSelectedListener implements
		OnItemSelectedListener {

	private static final String CRLF = "\r\n";
	private static final String DISPLAY_DATE_FORMAT = "MMMMMMMMM d, yyyy";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final int WATCHED_VIEW_ID = 1000;
	private View bgLayout;
	private static  Activity context;
	private ImageLoader imageLoader;
	private View previous;
	private ImageSize bgImageSize = new ImageSize(1280, 720);
	private Handler subtitleHandler;
	private Animation shrink;
	private Animation fadeIn;



	/**
	 * 
	 */
	public EpisodePosterOnItemSelectedListener(View bgv, Activity activity) {
		bgLayout = bgv;
		context = activity;
		imageLoader = SerenityApplication.getImageLoader();
		shrink = AnimationUtils.loadAnimation(activity, R.anim.shrink);
		fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
	}

	public void onItemSelected(SerenityAdapterView<?> av, View v, int position,
			long id) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean shouldShrink = preferences.getBoolean(
				"animation_shrink_posters", false);

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.setBackgroundColor(Color.BLACK);
			if (shouldShrink) {
				previous.setAnimation(shrink);
			}
		}

		previous = v;

		v.setBackgroundColor(Color.CYAN);
		v.setPadding(5, 5, 5, 5);
		v.clearAnimation();

		createMovieDetail((SerenityPosterImageView) v);
		createMovieMetaData((SerenityPosterImageView) v);
		createInfographicDetails((SerenityPosterImageView) v);
		changeBackgroundImage(v);

	}

	private void createMovieDetail(SerenityPosterImageView v) {
		View metaData = context.findViewById(R.id.metaDataRow);
		metaData.setVisibility(View.GONE);
		
		TextView seriesTitle = (TextView) context.findViewById(R.id.episodeTVSeriesTitle);
		if (v.getPosterInfo().getSeriesTitle() != null) {
			seriesTitle.setVisibility(View.VISIBLE);
			seriesTitle.setText(v.getPosterInfo().getSeriesTitle());
		} else {
			seriesTitle.setVisibility(View.GONE);
		}
		
		TextView summary = (TextView) context.findViewById(R.id.movieSummary);
		summary.setText(v.getPosterInfo().getSummary());

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
				Date airDate = new SimpleDateFormat(DATE_FORMAT).parse(v
						.getPosterInfo().getOriginalAirDate());
				SimpleDateFormat format = new SimpleDateFormat(
						DISPLAY_DATE_FORMAT);
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
		TextView ty = (TextView) context.findViewById(R.id.video_year);
		TextView tg = (TextView) context.findViewById(R.id.video_genre);
		TextView tw = (TextView) context.findViewById(R.id.video_writers);
		TextView td = (TextView) context.findViewById(R.id.video_directors);

		ty.setText(context.getString(R.string.unknown));
		tg.setText(context.getString(R.string.unknown));
		tw.setText(context.getString(R.string.unknown));
		td.setText(context.getString(R.string.unknown));

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
		
		ImageView fanArt = (ImageView) context.findViewById(R.id.fanArt);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean shouldFadeIn = preferences.getBoolean(
				"animation_background_fadein", false);
		if (shouldFadeIn) {
			fanArt.setAnimation(fadeIn);
		}
		imageLoader.displayImage(ei.getBackgroundURL(), fanArt, SerenityApplication.getMovieOptions());
		
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

		ImageView acv = imageUtils.createAudioCodecImage(epi.getAudioCodec(),
				v.getContext());
		if (acv != null) {
			infographicsView.addView(acv);
		}

		ImageView achannelsv = imageUtils.createAudioChannlesImage(
				epi.getAudioChannels(), v.getContext());
		if (achannelsv != null) {
			infographicsView.addView(achannelsv);
		}

		ImageView resv = imageUtils.createVideoResolutionImage(
				epi.getVideoResolution(), v.getContext());
		if (resv != null) {
			infographicsView.addView(resv);
		}

		ImageInfographicUtils imageUtilsAR = new ImageInfographicUtils(100, 58);
		ImageView aspectv = imageUtilsAR.createAspectRatioImage(
				epi.getAspectRatio(), v.getContext());
		if (aspectv != null) {
			infographicsView.addView(aspectv);
		}
		
		fetchSubtitle(epi);
	}
	
	protected void fetchSubtitle(VideoContentInfo mpi) {
		subtitleHandler = new SubtitleHandler(mpi);
		Messenger messenger = new Messenger(subtitleHandler);
		Intent intent = new Intent(context, MovieMetaDataRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", mpi.id());
		context.startService(intent);
}
	

	public void onNothingSelected(SerenityAdapterView<?> av) {

	}
	
	private static class SubtitleHandler extends Handler {
		
		private VideoContentInfo video;
		
		public SubtitleHandler(VideoContentInfo video) {
			this.video = video;
		}

		@Override
		public void handleMessage(Message msg) {
			List<Subtitle> subtitles = (List<Subtitle>) msg.obj;
			if (subtitles == null || subtitles.isEmpty()) {
				return;
			}
			
			View metaData = context.findViewById(R.id.metaDataRow);
			metaData.setVisibility(View.VISIBLE);
			View category = context.findViewById(R.id.movieCategoryName);
			category.setVisibility(View.GONE);
			View categoryFilter = context.findViewById(R.id.movieCategoryFilter);
			categoryFilter.setVisibility(View.GONE);
			View categoryFilter2 = context.findViewById(R.id.movieCategoryFilter2);
			categoryFilter2.setVisibility(View.GONE);
					
			
			TextView subtitleText = (TextView) context.findViewById(R.id.subtitleFilter);
			subtitleText.setVisibility(View.VISIBLE);			
			Spinner subtitleSpinner = (Spinner) context.findViewById(R.id.videoSubtitle);
			
			ArrayList<Subtitle> spinnerSubtitles = new ArrayList<Subtitle>();
			Subtitle noSubtitle = new Subtitle();
			noSubtitle.setDescription("None");
			noSubtitle.setFormat("none");
			noSubtitle.setKey(null);
			
			spinnerSubtitles.add(noSubtitle);
			spinnerSubtitles.addAll(subtitles);
			
			ArrayAdapter<Subtitle> subtitleAdapter = new ArrayAdapter<Subtitle>(context, R.layout.serenity_spinner_textview,
					spinnerSubtitles);
			subtitleAdapter
			.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
			subtitleSpinner.setAdapter(subtitleAdapter);
			subtitleSpinner.setOnItemSelectedListener(new SubtitleSpinnerOnItemSelectedListener(video, context));
			subtitleSpinner.setVisibility(View.VISIBLE);
			
		}

	}
	

}
