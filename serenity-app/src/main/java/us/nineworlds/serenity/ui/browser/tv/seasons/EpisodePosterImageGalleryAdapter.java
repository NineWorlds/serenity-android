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
import com.jess.ui.TwoWayAbsListView.LayoutParams;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.EpisodeRetrievalIntentService;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
		AbstractPosterImageGalleryAdapter {

	private static EpisodePosterImageGalleryAdapter notifyAdapter;
	private Animation shrink;
	private static final float WATCHED_PERCENT = 0.98f;

	public EpisodePosterImageGalleryAdapter(Context c, String key) {
		super(c, key);
		notifyAdapter = this;
		shrink = AnimationUtils.loadAnimation(c, R.anim.shrink);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View galleryCellView = context.getLayoutInflater().inflate(
				R.layout.poster_indicator_view, null);

		VideoContentInfo pi = posterList.get(position);
		SerenityPosterImageView mpiv = (SerenityPosterImageView) galleryCellView
				.findViewById(R.id.posterImageView);
		mpiv.setPosterInfo(pi);
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
			toggleProgressIndicator(galleryCellView, pi.getResumeOffset(), pi.getDuration(),  watchedView);
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
		Messenger messenger = new Messenger(handler);
		Intent intent = new Intent(context, EpisodeRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);

		context.startService(intent);
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
