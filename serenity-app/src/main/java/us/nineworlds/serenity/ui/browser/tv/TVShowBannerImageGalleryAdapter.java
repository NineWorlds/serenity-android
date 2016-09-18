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

package us.nineworlds.serenity.ui.browser.tv;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import butterknife.BindView;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.widgets.BadgeView;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.jess.ui.TwoWayGridView;

public class TVShowBannerImageGalleryAdapter extends AbstractPosterImageGalleryAdapter {

	private static final int BANNER_PIXEL_HEIGHT = 140;
	private static final int BANNER_PIXEL_WIDTH = 758;

	protected static List<SeriesContentInfo> tvShowList = null;

	private final String key;
	protected SerenityMultiViewVideoActivity showActivity;

	public TVShowBannerImageGalleryAdapter(Context c, String key,
			String category) {
		super(c, key, category);
		showActivity = (SerenityMultiViewVideoActivity) c;
		tvShowList = new ArrayList<SeriesContentInfo>();
		this.key = key;
		fetchData();
	}

	protected void fetchData() {
		String url = factory.getSectionsURL(key, category);
		volley.volleyXmlGetRequest(url, new SeriesResponseListener(),
				new DefaultLoggingVolleyErrorListener());
	}

	public int getItemCount() {
		return tvShowList.size();
	}

	@Override
	public Object getItem(int position) {
		return tvShowList.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.poster_tvshow_indicator_view, parent, false);
		return new TVShowViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		SeriesContentInfo pi = tvShowList.get(position);
		createImage(holder.itemView, pi, BANNER_PIXEL_WIDTH,
				BANNER_PIXEL_HEIGHT);

		toggleWatchedIndicator(holder.itemView, pi);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected void createImage(View galleryCellView, SeriesContentInfo pi,
			int imageWidth, int imageHeight) {
		int width = ImageUtils.getDPI(imageWidth, context);
		int height = ImageUtils.getDPI(imageHeight, context);

		initPosterMetaData(galleryCellView, pi, width, height, false);

		galleryCellView.setLayoutParams(new SerenityGallery.LayoutParams(width,
				height));
	}

	/**
	 * @param galleryCellView
	 * @param pi
	 * @param width
	 * @param height
	 */
	protected void initPosterMetaData(View galleryCellView,
			SeriesContentInfo pi, int width, int height, boolean isPoster) {
		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		mpiv.setMaxHeight(height);
		mpiv.setMaxWidth(width);

		if (isPoster) {
			serenityImageLoader.displayImage(pi.getThumbNailURL(), mpiv);
		} else {
			serenityImageLoader.displayImage(pi.getImageURL(), mpiv);
		}
	}

	/**
	 * @param galleryCellView
	 * @param pi
	 */
	protected void toggleWatchedIndicator(View galleryCellView,
			SeriesContentInfo pi) {

		int watched = 0;
		if (pi.getShowsWatched() != null) {
			watched = Integer.parseInt(pi.getShowsWatched());
		}
		ImageView watchedView = (ImageView) galleryCellView
				.findViewById(R.id.posterWatchedIndicator);
		watchedView.setVisibility(View.INVISIBLE);

		if (pi.isPartiallyWatched()) {
			toggleProgressIndicator(galleryCellView, watched, pi.totalShows(),
					watchedView);
		}

		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);
		BadgeView badgeView = new BadgeView(context, mpiv);
		badgeView.setTag("badge");
		Drawable backgroundDrawable = context.getResources().getDrawable(
				R.drawable.episode_count_background);
		badgeView.setBackgroundDrawable(backgroundDrawable);
		badgeView.setText(pi.getShowsUnwatched());
		badgeView.show();

		if (pi.isWatched()) {
			watchedView.setImageResource(R.drawable.overlaywatched);
			watchedView.setVisibility(View.VISIBLE);
			badgeView.hide();
		}
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

	@Override
	protected void fetchDataFromService() {

	}

	protected class SeriesResponseListener implements
			Response.Listener<MediaContainer> {

		@Override
		public void onResponse(MediaContainer response) {
			tvShowList = new SeriesMediaContainer(response).createSeries();
			DpadAwareRecyclerView recyclerView = (DpadAwareRecyclerView) (context
					.findViewById(R.id.tvShowBannerGallery) != null ? context.findViewById(R.id.tvShowBannerGallery) : context.findViewById(R.id.tvShowGridView));
			if (tvShowList != null) {
				TextView tv = (TextView) context
						.findViewById(R.id.tvShowItemCount);
				if (tv == null) {
					context.setSupportProgressBarIndeterminateVisibility(false);
					return;
				}
				if (tvShowList.isEmpty()) {
					Toast.makeText(
							context,
							context.getString(R.string.no_shows_found_for_the_category_)
									+ category, Toast.LENGTH_LONG).show();
				}
				tv.setText(Integer.toString(tvShowList.size())
						+ context.getString(R.string._item_s_));
			}
			notifyDataSetChanged();
			recyclerView.requestFocus();
		}
	}

	public class TVShowViewHolder extends RecyclerView.ViewHolder {

		public TVShowViewHolder(View itemView) {
			super(itemView);
		}
	}
}
