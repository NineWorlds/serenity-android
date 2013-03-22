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

package us.nineworlds.serenity.ui.browser.tv.episodes;

import java.util.List;

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
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Implementation of the Poster Image Gallery class for TV Shows.
 * 
 * @author dcarver
 * 
 */
public class EpisodePosterImageGalleryAdapter extends
		AbstractPosterImageGalleryAdapter {

	private static EpisodePosterImageGalleryAdapter notifyAdapter;
	private static ProgressDialog pd;

	public EpisodePosterImageGalleryAdapter(Context c, String key) {
		super(c, key);
		notifyAdapter = this;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		VideoContentInfo pi = posterList.get(position);
		SerenityPosterImageView mpiv = new SerenityPosterImageView(context, pi);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(375, context);
		int height = ImageUtils.getDPI(195, context);
		mpiv.setLayoutParams(new SerenityGallery.LayoutParams(width, height));

		imageLoader.displayImage(pi.getPosterURL(), mpiv);

		return mpiv;
	}

	@Override
	protected void fetchDataFromService() {
		pd = ProgressDialog.show(context, "", context.getString(R.string.retrieving_episodes));
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
			pd.dismiss();
			SerenityGallery gallery = (SerenityGallery) context
					.findViewById(R.id.moviePosterGallery);
			gallery.requestFocus();
			notifyAdapter.notifyDataSetChanged();

		}
	}
}
