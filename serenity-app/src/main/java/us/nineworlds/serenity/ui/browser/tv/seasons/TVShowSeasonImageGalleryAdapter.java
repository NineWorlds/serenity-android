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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.SerenityImageLoader;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.SeasonsMediaContainer;
import us.nineworlds.serenity.injection.InjectingRecyclerViewAdapter;
import us.nineworlds.serenity.ui.activity.SerenityDrawerLayoutActivity;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import us.nineworlds.serenity.widgets.BadgeView;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;

import net.ganin.darv.DpadAwareRecyclerView;

/**
 * An adapter that handles the population of views for TV Show Seasons.
 *
 * The fetching of the data is handled by a RESTFul Volley call to plex and then
 * the data is parsed and returned.
 *
 * @author dcarver
 *
 */
public class TVShowSeasonImageGalleryAdapter extends InjectingRecyclerViewAdapter {

	private List<SeriesContentInfo> seasonList = null;
	private final SerenityDrawerLayoutActivity context;

	private final String key;

	@Inject
	PlexappFactory plexFactory;

	@Inject
	SerenityImageLoader serenityImageLoader;

	@Inject
	VolleyUtils volley;

	public TVShowSeasonImageGalleryAdapter(Context c, String key) {
		super();
		context = (SerenityDrawerLayoutActivity) c;
		this.key = key;

		seasonList = new ArrayList<SeriesContentInfo>();

		fetchData();
	}

	protected void fetchData() {
		String url = plexFactory.getSeasonsURL(key);
		volley.volleyXmlGetRequest(url, new SeaonsResponseListener(),
				new DefaultLoggingVolleyErrorListener());
	}


	public Object getItem(int position) {
		return seasonList.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_tvshow_indicator_view, null);
		SeasonViewHolder seasonViewHolder = new SeasonViewHolder(view);
		return seasonViewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		SeriesContentInfo pi = seasonList.get(position);
		ImageView mpiv = (ImageView) holder.itemView
				.findViewById(R.id.posterImageView);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(120, context);
		int height = ImageUtils.getDPI(180, context);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

		serenityImageLoader.displayImage(pi.getImageURL(), mpiv);
		holder.itemView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width,
				height));

		int unwatched = 0;

		if (pi.getShowsUnwatched() != null) {
			unwatched = Integer.parseInt(pi.getShowsUnwatched());
		}

		ImageView watchedView = (ImageView) holder.itemView
				.findViewById(R.id.posterWatchedIndicator);

		TextView badgeCount = (TextView) holder.itemView.findViewById(R.id.badge_count);
		badgeCount.setText(pi.getShowsUnwatched());
		badgeCount.setVisibility(View.VISIBLE);

		if (pi.isWatched()) {
			watchedView.setImageResource(R.drawable.overlaywatched);
			watchedView.setVisibility(View.VISIBLE);
			badgeCount.setVisibility(View.INVISIBLE);
		}

		int watched = 0;
		if (pi.getShowsWatched() != null) {
			watched = Integer.parseInt(pi.getShowsWatched());
		}

		if (pi.isPartiallyWatched()) {
			toggleProgressIndicator(holder.itemView, watched, pi.totalShows(),
					watchedView);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return seasonList.size();
	}

	protected void toggleProgressIndicator(View galleryCellView, int dividend,
			int divisor, ImageView watchedView) {
		final float percentWatched = Float.valueOf(dividend)
				/ Float.valueOf(divisor);

		final ProgressBar view = (ProgressBar) galleryCellView
				.findViewById(R.id.posterInprogressIndicator);
		int progress = Float.valueOf(percentWatched * 100).intValue();
		if (progress < 10) {
			progress = 10;
		}
		view.setProgress(progress);
		view.setVisibility(View.VISIBLE);
		watchedView.setVisibility(View.INVISIBLE);
	}

	private class SeaonsResponseListener implements
	Response.Listener<MediaContainer> {

		@Override
		public void onResponse(MediaContainer response) {
			seasonList = new SeasonsMediaContainer(response).createSeries();
			notifyDataSetChanged();

			if (seasonList != null) {
				if (!seasonList.isEmpty()) {
					TextView titleView = (TextView) context
							.findViewById(R.id.tvShowSeasonsDetailText);
					titleView.setText(seasonList.get(0).getParentTitle());
					TextView textView = (TextView) context
							.findViewById(R.id.tvShowSeasonsItemCount);
					textView.setText(Integer.toString(seasonList.size())
							+ context.getString(R.string._item_s_));

				}
			}

			DpadAwareRecyclerView gallery = (DpadAwareRecyclerView) context
					.findViewById(R.id.tvShowSeasonImageGallery);
			if (gallery != null) {
				gallery.requestFocusFromTouch();
			}
		}
	}

	public class SeasonViewHolder extends DpadAwareRecyclerView.ViewHolder {

		public SeasonViewHolder(View itemView) {
			super(itemView);
		}

	}
}
