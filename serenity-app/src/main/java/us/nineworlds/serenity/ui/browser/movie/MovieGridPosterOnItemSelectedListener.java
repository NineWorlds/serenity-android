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

package us.nineworlds.serenity.ui.browser.movie;

import java.util.ArrayList;
import java.util.List;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemSelectedListener;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.services.MovieMetaDataRetrievalIntentService;
import us.nineworlds.serenity.ui.listeners.SubtitleSpinnerOnItemSelectedListener;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * When a poster is selected, update the information displayed in the browser.
 * 
 * @author dcarver
 * 
 */
public class MovieGridPosterOnItemSelectedListener implements
		OnItemSelectedListener {

	private static Activity context;
	private View previous;
	private Handler subtitleHandler;

	/**
	 * 
	 */
	public MovieGridPosterOnItemSelectedListener(View bgv, Activity activity) {
		context = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(TwoWayAdapterView<?> av, View v, int position,
			long id) {

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
		}

		previous = v;

		v.setPadding(5, 5, 5, 5);

		createMovieMetaData((SerenityPosterImageView) v);
	}


	private void createMovieMetaData(SerenityPosterImageView v) {
		SerenityPosterImageView mpiv = v;
		VideoContentInfo mi = mpiv.getPosterInfo();
		TextView subt = (TextView) context.findViewById(R.id.subtitleFilter);
		subt.setVisibility(View.GONE);
		Spinner subtitleSpinner = (Spinner) context.findViewById(R.id.videoSubtitle);
		subtitleSpinner.setVisibility(View.GONE);
		
		TextView posterTitle = (TextView) context.findViewById(R.id.movieBrowserPosterTitle);
		posterTitle.setText(mi.getTitle());
		
		//fetchSubtitle(mi);

	}
	
	protected void fetchSubtitle(VideoContentInfo mpi) {
			subtitleHandler = new SubtitleHandler(mpi);
			Messenger messenger = new Messenger(subtitleHandler);
			Intent intent = new Intent(context, MovieMetaDataRetrievalIntentService.class);
			intent.putExtra("MESSENGER", messenger);
			intent.putExtra("key", mpi.id());
			context.startService(intent);
	}

	@Override
	public void onNothingSelected(TwoWayAdapterView<?> av) {
		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.refreshDrawableState();
		}

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
