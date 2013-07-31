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

import com.nostra13.universalimageloader.core.ImageLoader;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemSelectedListener;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author dcarver
 * 
 */
public class EpisodePosterOnItemSelectedListener extends AbstractVideoOnItemSelectedListener implements
		OnItemSelectedListener {

	private static final String DISPLAY_DATE_FORMAT = "MMMMMMMMM d, yyyy";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	public ImageLoader imageLoader;



	/**
	 * 
	 */
	public EpisodePosterOnItemSelectedListener(Activity activity) {
		super(activity);
		context = activity;
		imageLoader = SerenityApplication.getImageLoader();
	}

	public void createVideoDetail(SerenityPosterImageView v) {
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

	public void onNothingSelected(SerenityAdapterView<?> av) {

	}
}
