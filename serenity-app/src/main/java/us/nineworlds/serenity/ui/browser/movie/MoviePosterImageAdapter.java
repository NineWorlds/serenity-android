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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.events.MovieRetrievalEvent;
import us.nineworlds.serenity.jobs.MovieRetrievalJob;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.widgets.RoundedImageView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.birbit.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class MoviePosterImageAdapter extends AbstractPosterImageGalleryAdapter {

	@Inject
	JobManager jobManager;

	@Inject
	EventBus eventBus;

	protected static AbstractPosterImageGalleryAdapter notifyAdapter;
	private static SerenityMultiViewVideoActivity movieContext;

	public MoviePosterImageAdapter(Context c, String key, String category) {
		super(c, key, category);
		movieContext = (SerenityMultiViewVideoActivity) c;
		notifyAdapter = this;
		eventBus.register(this);
	}

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

		RoundedImageView mpiv = (RoundedImageView) galleryCellView
				.findViewById(R.id.posterImageView);

		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = 0;
		int height = 0;

		width = ImageUtils.getDPI(130, context);
		height = ImageUtils.getDPI(200, context);
		mpiv.setMaxHeight(height);
		mpiv.setMaxWidth(width);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		galleryCellView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width, height));

		serenityImageLoader.displayImage(pi.getImageURL(), mpiv);

		setWatchedStatus(galleryCellView, pi);

		return galleryCellView;
	}

	protected void gridViewMetaData(View galleryCellView, VideoContentInfo pi) {
		if (movieContext.isGridViewActive()) {
			if (pi.getAvailableSubtitles() != null) {
				View v = galleryCellView.findViewById(R.id.infoGraphicMeta);
				v.setVisibility(View.VISIBLE);
				v.findViewById(R.id.subtitleIndicator).setVisibility(
						View.VISIBLE);
			}
		}
	}

	@Override
	protected void fetchDataFromService() {
		MovieRetrievalJob movieRetrievalJob = new MovieRetrievalJob(key, category);
		jobManager.addJobInBackground(movieRetrievalJob);
	}

	@Override
	public int getItemCount() {
		Log.i(this.getClass().getSimpleName(), "Item Count Called for Grid.");
		return super.getItemCount();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(movieContext).inflate(R.layout.poster_indicator_view, parent, false);
		return new MoviePosterViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		MoviePosterViewHolder viewHolder = (MoviePosterViewHolder) holder;
		if (position > posterList.size()) {
			position = posterList.size() - 1;
		}

		if (position < 0) {
			position = 0;
		}

		viewHolder.poseterInprogressIndicator.setVisibility(View.INVISIBLE);
		viewHolder.posterWatchedIndicator.setVisibility(View.INVISIBLE);
		viewHolder.infoGraphicMetaContainer.setVisibility(View.GONE);

		VideoContentInfo pi = posterList.get(position);

		RoundedImageView mpiv = viewHolder.posterImageView;

		int width = 0;
		int height = 0;

		width = ImageUtils.getDPI(130, context);
		height = ImageUtils.getDPI(200, context);
		mpiv.setMaxHeight(height);
		mpiv.setMaxWidth(width);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		viewHolder.itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width, height));

		serenityImageLoader.displayImage(pi.getImageURL(), mpiv);

		setWatchedStatus(viewHolder.itemView, pi);
	}

	private class MoviePosterResponseErrorListener implements
			Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			context.setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMoviePosterResponse(MovieRetrievalEvent event) {
		populatePosters(event.getMediaContainer());

	}

	protected void populatePosters(MediaContainer mc) {
		MovieMediaContainer movies = new MovieMediaContainer(mc);
		posterList = movies.createVideos();
		notifyAdapter.notifyDataSetChanged();
		DpadAwareRecyclerView posterGallery = (DpadAwareRecyclerView) movieContext
				.findViewById(R.id.moviePosterView);
		posterGallery.requestFocusFromTouch();
	}

	protected class MoviePosterViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.posterInprogressIndicator)
		public ProgressBar poseterInprogressIndicator;

		@BindView(R.id.posterWatchedIndicator)
		public ImageView posterWatchedIndicator;

		@BindView(R.id.infoGraphicMeta)
		public LinearLayout infoGraphicMetaContainer;

		@BindView(R.id.posterImageView)
		public RoundedImageView posterImageView;

		public MoviePosterViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

	}

}
