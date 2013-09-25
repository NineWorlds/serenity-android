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

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.impl.AbstractSeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo;
import us.nineworlds.serenity.core.services.ShowSeasonRetrievalIntentService;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.TVShowImageView;

import us.nineworlds.serenity.R;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 
 * @author dcarver
 * 
 */
public class TVShowSeasonImageGalleryAdapter extends BaseAdapter {

	private static List<TVShowSeriesInfo> seasonList = null;
	private static Activity context;

	private ImageLoader imageLoader;
	private static ProgressDialog pd;
	private Handler handler;
	private String key;
	private static TVShowSeasonImageGalleryAdapter notifyAdapter;

	public TVShowSeasonImageGalleryAdapter(Context c, String key) {
		context = (Activity) c;
		this.key = key;

		seasonList = new ArrayList<TVShowSeriesInfo>();

		imageLoader = SerenityApplication.getImageLoader();
		notifyAdapter = this;

		fetchData();
	}

	protected void fetchData() {
		pd = ProgressDialog.show(context, "", "Retrieving Seasons");
		handler = new ShowSeasonRetrievalHandler();
		Messenger messenger = new Messenger(handler);
		Intent intent = new Intent(context,
				ShowSeasonRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		context.startService(intent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {

		return seasonList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {

		return seasonList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View galleryCellView = context.getLayoutInflater().inflate(R.layout.poster_tvshow_indicator_view, null);
		

		AbstractSeriesContentInfo pi = seasonList.get(position);
		TVShowImageView mpiv = (TVShowImageView) galleryCellView.findViewById(R.id.posterImageView);
		mpiv.setPosterInfo(pi);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(160, context);
		int height = ImageUtils.getDPI(220, context);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

		imageLoader.displayImage(pi.getImageURL(), mpiv);
		galleryCellView.setLayoutParams(new Gallery.LayoutParams(width, height));
		
		int unwatched = 0;
		
		if (pi.getShowsUnwatched() != null) {
			unwatched = Integer.parseInt(pi.getShowsUnwatched());
		}
		
		ImageView watchedView = (ImageView) galleryCellView.findViewById(R.id.posterWatchedIndicator);
		if (unwatched == 0) {
			watchedView.setImageResource(R.drawable.overlaywatched);
		}
		
		int watched = 0;
		if (pi.getShowsWatched() != null ) {
			watched = Integer.parseInt(pi.getShowsWatched());
		}
		
		int totalShows = unwatched + watched;
		if (unwatched != totalShows) {
			toggleProgressIndicator(galleryCellView, watched, totalShows, watchedView);
		}
		
		return galleryCellView;
	}
	
	/**
	 * @param galleryCellView
	 * @param pi
	 * @param watchedView
	 */
	protected void toggleProgressIndicator(View galleryCellView, int dividend,
			int divisor, ImageView watchedView) {
		final float percentWatched = Float.valueOf(dividend)
				/ Float.valueOf(divisor);
		if (percentWatched < 0.99f) {
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
	}
	

	private static class ShowSeasonRetrievalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			seasonList = (List<TVShowSeriesInfo>) msg.obj;

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
				notifyAdapter.notifyDataSetChanged();
				pd.dismiss();
			}

		}
	}

}
