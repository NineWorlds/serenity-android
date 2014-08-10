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

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.TrailersYouTubeSearch;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import us.nineworlds.serenity.volley.YouTubeTrailerSearchResponseListener;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author dcarver
 *
 */
public class EpisodePosterOnItemSelectedListener extends
AbstractVideoOnItemSelectedListener implements OnItemSelectedListener {

	private static final String DISPLAY_DATE_FORMAT = "MMMMMMMMM d, yyyy";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	public ImageLoader imageLoader;

	private String prevTitle;
	private boolean fadeIn = true;
	private int fadeInCount = 0;

	@Override
	public void fetchTrailer(VideoContentInfo mpi, View view) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (prefs.getBoolean("episode_trailers", false) == false) {
			return;
		}
		checkDataBaseForTrailer(mpi);
		if (mpi.hasTrailer()) {
			return;
		}

		TrailersYouTubeSearch trailerSearch = new TrailersYouTubeSearch();
		String queryURL = trailerSearch.queryURL(mpi);

		VolleyUtils.volleyJSonGetRequest(queryURL,
				new YouTubeTrailerSearchResponseListener(view, mpi),
				new DefaultLoggingVolleyErrorListener());
	}

	/**
	 *
	 */
	public EpisodePosterOnItemSelectedListener(Activity activity) {
		super(activity);
		context = activity;
		imageLoader = SerenityApplication.getImageLoader();
	}

	@Override
	public void createVideoDetail(ImageView v) {
		View cardView = context.findViewById(R.id.video_details_container);
		if (cardView != null) {
			cardView.setVisibility(View.VISIBLE);
		}

		ImageView posterImage = (ImageView) context
				.findViewById(R.id.video_poster);
		posterImage.setVisibility(View.VISIBLE);
		posterImage.setScaleType(ScaleType.FIT_XY);
		if (videoInfo.getParentPosterURL() != null) {
			int width = ImageUtils.getDPI(240, context);
			int height = ImageUtils.getDPI(330, context);
			posterImage.setLayoutParams(new RelativeLayout.LayoutParams(width,
					height));
			SerenityApplication.displayImage(videoInfo.getParentPosterURL(),
					posterImage);
		} else if (videoInfo.getGrandParentPosterURL() != null) {
			int width = ImageUtils.getDPI(240, context);
			int height = ImageUtils.getDPI(330, context);
			posterImage.setLayoutParams(new RelativeLayout.LayoutParams(width,
					height));
			SerenityApplication.displayImage(
					videoInfo.getGrandParentPosterURL(), posterImage);
		} else {
			SerenityApplication.displayImage(videoInfo.getImageURL(),
					posterImage);
		}

		TextView seriesTitle = (TextView) context
				.findViewById(R.id.movieActionBarPosterTitle);
		if (videoInfo.getSeriesTitle() != null) {
			if (!videoInfo.getSeriesTitle().equals(prevTitle)) {
				fadeIn = true;
			} else {
				fadeInCount += 1;
				fadeIn = false;
			}
			seriesTitle.setVisibility(View.VISIBLE);
			seriesTitle.setText(videoInfo.getSeriesTitle());
			prevTitle = videoInfo.getSeriesTitle();
		} else {
			seriesTitle.setVisibility(View.GONE);
		}

		TextView summary = (TextView) context.findViewById(R.id.movieSummary);
		summary.setText(videoInfo.getSummary());

		TextView title = (TextView) context
				.findViewById(R.id.movieBrowserPosterTitle);
		String epTitle = videoInfo.getTitle();
		String season = videoInfo.getSeason();
		String episode = videoInfo.getEpisodeNumber();

		if (season != null || episode != null) {
			epTitle = epTitle + " - ";
		}

		if (season != null) {
			epTitle = epTitle + season + " ";
		}

		if (episode != null) {
			epTitle = epTitle + episode;
		}

		title.setText(epTitle);
		TextView vte = (TextView) context.findViewById(R.id.videoTextExtra);
		vte.setVisibility(View.INVISIBLE);
		if (videoInfo.getOriginalAirDate() != null) {
			try {
				Date airDate = new SimpleDateFormat(DATE_FORMAT)
				.parse(videoInfo.getOriginalAirDate());
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

	@Override
	public void changeBackgroundImage(View v) {
		if (fadeIn == true || fadeInCount == 0) {
			super.changeBackgroundImage(v);
			fadeIn = false;
			fadeInCount += 1;
			return;
		}

		VideoContentInfo ei = videoInfo;

		if (ei.getBackgroundURL() == null) {
			return;
		}

		ImageView fanArt = (ImageView) context.findViewById(R.id.fanArt);
		fanArt.clearAnimation();
		SerenityApplication.displayImage(ei.getBackgroundURL(), fanArt,
				SerenityApplication.getMovieOptions());
	}

	@Override
	public void onNothingSelected(SerenityAdapterView<?> av) {

	}

	@Override
	protected void createVideoMetaData(ImageView v) {
		super.createVideoMetaData(v);

		View categoryFilter = context.findViewById(R.id.categoryFilter);
		categoryFilter.setVisibility(View.GONE);
		View categoryFilter2 = context.findViewById(R.id.categoryFilter2);
		categoryFilter2.setVisibility(View.GONE);
		View categoryName = context.findViewById(R.id.movieCategoryName);
		categoryName.setVisibility(View.GONE);

		TextView subt = (TextView) context.findViewById(R.id.subtitleFilter);
		subt.setVisibility(View.GONE);
		Spinner subtitleSpinner = (Spinner) context
				.findViewById(R.id.videoSubtitle);
		subtitleSpinner.setVisibility(View.GONE);
	}

}
