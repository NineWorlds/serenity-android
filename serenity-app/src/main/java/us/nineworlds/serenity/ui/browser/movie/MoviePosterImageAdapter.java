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

import java.util.List;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jess.ui.TwoWayAbsListView;
import com.jess.ui.TwoWayGridView;

import us.nineworlds.serenity.core.model.DBMetaData;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.MovieMetaDataRetrievalIntentService;
import us.nineworlds.serenity.core.services.MoviesRetrievalIntentService;
import us.nineworlds.serenity.core.services.YouTubeTrailerSearchIntentService;
import us.nineworlds.serenity.core.util.DBMetaDataSource;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.listeners.GridSubtitleHandler;
import us.nineworlds.serenity.ui.listeners.SubtitleHandler;
import us.nineworlds.serenity.ui.listeners.TrailerGridHandler;
import us.nineworlds.serenity.ui.listeners.TrailerHandler;
import us.nineworlds.serenity.ui.util.DisplayUtils;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.method.MovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * @author dcarver
 * 
 */
public class MoviePosterImageAdapter extends AbstractPosterImageGalleryAdapter {

	protected static AbstractPosterImageGalleryAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private Handler posterGalleryHandler;
	private static SerenityMultiViewVideoActivity movieContext;
	private DBMetaDataSource datasource;
	
	public MoviePosterImageAdapter(Context c, String key, String category) {
		super(c, key, category);
		movieContext = (SerenityMultiViewVideoActivity) c;
		pd = ProgressDialog
				.show(c, "", c.getString(R.string.retrieving_movies));
		notifyAdapter = this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View galleryCellView = null;
		if (convertView != null) {
			galleryCellView = convertView;
			galleryCellView.findViewById(R.id.posterInprogressIndicator).setVisibility(View.INVISIBLE);
			galleryCellView.findViewById(R.id.posterWatchedIndicator).setVisibility(View.INVISIBLE);
			galleryCellView.findViewById(R.id.infoGraphicMeta).setVisibility(View.GONE);
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
		imageLoader.displayImage(pi.getImageURL(), mpiv);

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
				if (YouTubeInitializationResult.SUCCESS.equals(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(context))) {
					fetchTrailer(pi, galleryCellView);
				}
			} else {
				View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
				v.setVisibility(View.VISIBLE);
				v.findViewById(R.id.trailerIndicator).setVisibility(View.VISIBLE);
			}
			
			if (pi.getAvailableSubtitles() != null) {
				View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
				v.setVisibility(View.VISIBLE);
				v.findViewById(R.id.subtitleIndicator).setVisibility(View.VISIBLE);
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
		
		TrailerHandler trailerHandler = new TrailerGridHandler(mpi, context, view);
		Messenger messenger = new Messenger(trailerHandler);
		Intent intent = new Intent(context, YouTubeTrailerSearchIntentService.class);
		intent.putExtra("videoTitle", mpi.getTitle());
		intent.putExtra("year", mpi.getYear());
		intent.putExtra("MESSENGER", messenger);
		context.startService(intent);
	}
	
	public void fetchSubtitle(VideoContentInfo mpi, View view) {
		GridSubtitleHandler subtitleHandler = new GridSubtitleHandler(mpi, view);
		Messenger messenger = new Messenger(subtitleHandler);
		Intent intent = new Intent(context,
				MovieMetaDataRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", mpi.id());
		context.startService(intent);
	}
	

	@Override
	protected void fetchDataFromService() {
		posterGalleryHandler = new MoviePosterHandler();
		Messenger messenger = new Messenger(posterGalleryHandler);
		Intent intent = new Intent(context, MoviesRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		intent.putExtra("category", category);
		context.startService(intent);
	}

	private static class MoviePosterHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			posterList = (List<VideoContentInfo>) msg.obj;
			notifyAdapter.notifyDataSetChanged();
			if (!movieContext.isGridViewActive()) {
				SerenityGallery posterGallery = (SerenityGallery) context
						.findViewById(R.id.moviePosterGallery);
				posterGallery.requestFocusFromTouch();
			} else {
				TwoWayGridView gridView = (TwoWayGridView) context
						.findViewById(R.id.movieGridView);
				gridView.requestFocusFromTouch();
			}
			pd.dismiss();
		}

	}
	
}
