/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jess.ui.TwoWayAbsListView;
import com.jess.ui.TwoWayGridView;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.DBMetaData;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.core.services.YouTubeTrailerSearchIntentService;
import us.nineworlds.serenity.core.util.DBMetaDataSource;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.TrailerGridHandler;
import us.nineworlds.serenity.ui.listeners.TrailerHandler;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.GridSubtitleVolleyResponseListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @author dcarver
 * 
 */
public class MoviePosterImageAdapter extends AbstractPosterImageGalleryAdapter {

	protected static AbstractPosterImageGalleryAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private static SerenityMultiViewVideoActivity movieContext;
	private DBMetaDataSource datasource;

	public MoviePosterImageAdapter(Context c, String key, String category) {
		super(c, key, category);
		movieContext = (SerenityMultiViewVideoActivity) c;
		notifyAdapter = this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position > posterList.size()) {
			position = posterList.size() - 1;
		}
		if (position < 0) {
			position = 0;
		}
		View galleryCellView = null;
		if (convertView != null) {
			galleryCellView = convertView;
			galleryCellView.findViewById(R.id.posterInprogressIndicator)
					.setVisibility(View.INVISIBLE);
			galleryCellView.findViewById(R.id.posterWatchedIndicator)
					.setVisibility(View.INVISIBLE);
			galleryCellView.findViewById(R.id.infoGraphicMeta).setVisibility(
					View.GONE);
		} else {
			galleryCellView = context.getLayoutInflater().inflate(
					R.layout.poster_indicator_view, null);
		}

		VideoContentInfo pi = posterList.get(position);
		gridViewMetaData(galleryCellView, pi);

		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);

		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = 0;
		int height = 0;

		width = ImageUtils.getDPI(130, context);
		height = ImageUtils.getDPI(200, context);
		mpiv.setMaxHeight(height);
		mpiv.setMaxWidth(width);
		if (!movieContext.isGridViewActive()) {
			mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			galleryCellView.setLayoutParams(new SerenityGallery.LayoutParams(
					width, height));
		} else {
			width = ImageUtils.getDPI(120, context);
			height = ImageUtils.getDPI(180, context);
			mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			galleryCellView.setLayoutParams(new TwoWayAbsListView.LayoutParams(
					width, height));
		}

		shrinkPosterAnimation(mpiv, movieContext.isGridViewActive());
		SerenityApplication.displayImage(pi.getImageURL(), mpiv);

		setWatchedStatus(galleryCellView, pi);

		return galleryCellView;
	}

	/**
	 * @param galleryCellView
	 * @param pi
	 */
	protected void gridViewMetaData(View galleryCellView, VideoContentInfo pi) {
		if (movieContext.isGridViewActive()) {
			checkDataBaseForTrailer(pi);

			if (pi.hasTrailer() == false) {
				if (YouTubeInitializationResult.SUCCESS
						.equals(YouTubeApiServiceUtil
								.isYouTubeApiServiceAvailable(context))) {
					fetchTrailer(pi, galleryCellView);
				}
			} else {
				View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
				v.setVisibility(View.VISIBLE);
				v.findViewById(R.id.trailerIndicator).setVisibility(
						View.VISIBLE);
			}

			if (pi.getAvailableSubtitles() != null) {
				View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
				v.setVisibility(View.VISIBLE);
				v.findViewById(R.id.subtitleIndicator).setVisibility(
						View.VISIBLE);
			} else {
				fetchSubtitle(pi, galleryCellView);
			}
		}
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

		TrailerHandler trailerHandler = new TrailerGridHandler(mpi, context,
				view);
		Messenger messenger = new Messenger(trailerHandler);
		Intent intent = new Intent(context,
				YouTubeTrailerSearchIntentService.class);
		intent.putExtra("videoTitle", mpi.getTitle());
		intent.putExtra("year", mpi.getYear());
		intent.putExtra("MESSENGER", messenger);
		context.startService(intent);
	}

	public void fetchSubtitle(VideoContentInfo mpi, View view) {
		PlexappFactory factory = SerenityApplication.getPlexFactory();
		String url = factory.getMovieMetadataURL("/library/metadata/"
				+ mpi.id());
		VolleyUtils.volleyXmlGetRequest(url,
				new GridSubtitleVolleyResponseListener(mpi, context, view),
				new DefaultLoggingVolleyErrorListener());
	}

	@Override
	protected void fetchDataFromService() {
		pd = ProgressDialog.show(context, "",
				context.getString(R.string.retrieving_movies));

		final PlexappFactory factory = SerenityApplication.getPlexFactory();
		String url = factory.getSectionsURL(key, category);

		VolleyUtils.volleyXmlGetRequest(url, new MoviePosterResponseListener(),
				new MoviePosterResponseErrorListener());
	}

	private class MoviePosterResponseErrorListener implements
			Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (pd != null) {
				pd.dismiss();
			}
		}

	}

	/**
	 * @author dcarver
	 * 
	 */
	private class MoviePosterResponseListener implements
			Response.Listener<MediaContainer> {

		@Override
		public void onResponse(MediaContainer response) {
			try {
				MediaContainer mc = response;
				populatePosters(mc);
			} catch (Exception e) {
				Log.e(getClass().getName(), "Error populating posters.", e);
			}
			if (pd.isShowing()) {
				pd.dismiss();
			}
		}

		/**
		 * @param mc
		 */
		protected void populatePosters(MediaContainer mc) {
			MovieMediaContainer movies = new MovieMediaContainer(mc);
			posterList = movies.createVideos();
			notifyAdapter.notifyDataSetChanged();
			if (!movieContext.isGridViewActive()) {
				SerenityGallery posterGallery = (SerenityGallery) movieContext
						.findViewById(R.id.moviePosterGallery);
				posterGallery.requestFocusFromTouch();
			} else {
				TwoWayGridView gridView = (TwoWayGridView) movieContext
						.findViewById(R.id.movieGridView);
				gridView.requestFocusFromTouch();
			}
		}

	}

}
