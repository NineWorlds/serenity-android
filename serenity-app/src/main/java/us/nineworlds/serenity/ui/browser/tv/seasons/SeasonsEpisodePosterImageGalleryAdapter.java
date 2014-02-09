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

package us.nineworlds.serenity.ui.browser.tv.seasons;

import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jess.ui.TwoWayAbsListView;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.DBMetaData;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.YouTubeTrailerSearchIntentService;
import us.nineworlds.serenity.core.util.DBMetaDataSource;
import us.nineworlds.serenity.ui.listeners.TrailerGridHandler;
import us.nineworlds.serenity.ui.listeners.TrailerHandler;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.GridSubtitleVolleyResponseListener;
import us.nineworlds.serenity.volley.SimpleXmlRequest;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Implementation of the Poster Image Gallery class for TV Shows.
 * 
 * @author dcarver
 * 
 */
public class SeasonsEpisodePosterImageGalleryAdapter
		extends
		us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter {

	private static SeasonsEpisodePosterImageGalleryAdapter notifyAdapter;
	private DBMetaDataSource datasource;
	private SharedPreferences prefs;
	
	public SeasonsEpisodePosterImageGalleryAdapter(Context c, String key) {
		super(c, key);
		notifyAdapter = this;
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View galleryCellView = context.getLayoutInflater().inflate(
				R.layout.poster_indicator_view, null);

		VideoContentInfo pi = posterList.get(position);
		gridViewMetaData(galleryCellView, pi);
		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(270, context);
		int height = ImageUtils.getDPI(147, context);
		mpiv.setMaxWidth(width);
		mpiv.setMaxHeight(height);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		galleryCellView.setLayoutParams(new TwoWayAbsListView.LayoutParams(
				width, height));

		imageLoader.displayImage(pi.getImageURL(), mpiv);

		ImageView watchedView = (ImageView) galleryCellView
				.findViewById(R.id.posterWatchedIndicator);

		if (pi.isPartiallyWatched()) {
			ImageUtils.toggleProgressIndicator(galleryCellView,
					pi.getResumeOffset(), pi.getDuration());
		} else if (pi.isWatched()) {
			watchedView.setImageResource(R.drawable.overlaywatched);
			watchedView.setVisibility(View.VISIBLE);
		} else {
			watchedView.setVisibility(View.INVISIBLE);
		}

		TextView metaData = (TextView) galleryCellView
				.findViewById(R.id.metaOverlay);
		String metaText = "";
		if (pi.getSeason() != null) {
			metaText = pi.getSeason() + " ";
		}

		if (pi.getEpisodeNumber() != null) {
			metaText = metaText + pi.getEpisodeNumber();
		}

		if (metaText.length() > 0) {
			metaData.setText(metaText);
			metaData.setVisibility(View.VISIBLE);
		}

		TextView title = (TextView) galleryCellView
				.findViewById(R.id.posterOverlayTitle);
		title.setText(pi.getTitle());
		title.setVisibility(View.VISIBLE);

		return galleryCellView;
	}

	@Override
	protected void fetchDataFromService() {
		handler = new EpisodeHandler();
		retrieveEpisodes();
		notifyAdapter = this;

	}

	/**
	 * @param galleryCellView
	 * @param pi
	 */
	protected void gridViewMetaData(View galleryCellView, VideoContentInfo pi) {
		checkDataBaseForTrailer(pi);

		boolean trailersEnabled = prefs.getBoolean("episode_trailers", false);
		if (trailersEnabled) {
			if (pi.hasTrailer() == false) {
				if (YouTubeInitializationResult.SUCCESS
						.equals(YouTubeApiServiceUtil
								.isYouTubeApiServiceAvailable(context))) {
					fetchTrailer(pi, galleryCellView);
				}
			} else {
				View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
				v.setVisibility(View.VISIBLE);
				v.findViewById(R.id.trailerIndicator).setVisibility(View.VISIBLE);
			}
		}

		if (pi.getAvailableSubtitles() != null) {
			View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
			v.setVisibility(View.VISIBLE);
			v.findViewById(R.id.subtitleIndicator).setVisibility(View.VISIBLE);
		} else {
			fetchSubtitle(pi, galleryCellView);
		}
	}

	public void fetchSubtitle(VideoContentInfo mpi, View view) {		
		PlexappFactory factory = SerenityApplication.getPlexFactory();
		String url = factory.getMovieMetadataURL("/library/metadata/"
				+ mpi.id());
		SimpleXmlRequest<MediaContainer> xmlRequest = new SimpleXmlRequest<MediaContainer>(
				Request.Method.GET, url, MediaContainer.class,
				new GridSubtitleVolleyResponseListener(mpi, context, view),
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		queue.add(xmlRequest);
		
	}
	
	/**
	 * @param pi
	 */
	protected void checkDataBaseForTrailer(VideoContentInfo pi) {
		datasource = new DBMetaDataSource(context);
		datasource.open();
		DBMetaData metaData = datasource.findMetaDataByPlexId(pi.id());
		if (metaData != null) {
			pi.setTrailer(true);
			pi.setTrailerId(metaData.getYouTubeID());
		}
		datasource.close();
	}
	
	public void fetchTrailer(VideoContentInfo mpi, View view) {
		
		TrailerHandler trailerHandler = new TrailerGridHandler(mpi, context, view);
		Messenger messenger = new Messenger(trailerHandler);
		Intent intent = new Intent(context, YouTubeTrailerSearchIntentService.class);
		intent.putExtra("videoTitle", mpi.getTitle());
		intent.putExtra("year", mpi.getYear());
		intent.putExtra("show", mpi.getSeriesTitle());
		intent.putExtra("season", mpi.getSeason());
		intent.putExtra("episodeNum", mpi.getEpisodeNumber());
		intent.putExtra("MESSENGER", messenger);
		context.startService(intent);
	}
	

	private static class EpisodeHandler extends Handler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			posterList = (List<VideoContentInfo>) msg.obj;
			notifyAdapter.notifyDataSetChanged();
		}
	}
}
