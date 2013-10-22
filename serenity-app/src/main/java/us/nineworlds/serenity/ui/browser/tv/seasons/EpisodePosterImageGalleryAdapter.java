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

import com.jess.ui.TwoWayAbsListView;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;


import us.nineworlds.serenity.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
public class EpisodePosterImageGalleryAdapter extends
		us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterImageGalleryAdapter {

	private static EpisodePosterImageGalleryAdapter notifyAdapter;

	public EpisodePosterImageGalleryAdapter(Context c, String key) {
		super(c, key);
		notifyAdapter = this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View galleryCellView = context.getLayoutInflater().inflate(
				R.layout.poster_indicator_view, null);

		VideoContentInfo pi = posterList.get(position);
		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(270, context);
		int height = ImageUtils.getDPI(147, context);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		galleryCellView.setLayoutParams(new TwoWayAbsListView.LayoutParams(width, height));

		imageLoader.displayImage(pi.getImageURL(), mpiv);

		ImageView watchedView = (ImageView) galleryCellView
				.findViewById(R.id.posterWatchedIndicator);
 
		if (pi.getViewCount() > 0) {
			watchedView.setImageResource(R.drawable.overlaywatched);
		}

		if (pi.getViewCount() > 0) {
			watchedView.setImageResource(R.drawable.overlaywatched);
		}

		if (pi.getViewCount() > 0 && pi.getDuration() > 0
				&& pi.getResumeOffset() != 0) {
			ImageUtils.toggleProgressIndicator(galleryCellView, pi.getResumeOffset(), pi.getDuration());
		}
		
		
		TextView metaData = (TextView) galleryCellView.findViewById(R.id.metaOverlay);
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
		
		
		TextView title = (TextView) galleryCellView.findViewById(R.id.posterOverlayTitle);
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
