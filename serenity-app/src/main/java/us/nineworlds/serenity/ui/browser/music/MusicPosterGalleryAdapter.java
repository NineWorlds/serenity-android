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

package us.nineworlds.serenity.ui.browser.music;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.impl.MusicArtistContentInfo;
import us.nineworlds.serenity.core.services.MusicRetrievalIntentService;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.SerenityMusicImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author dcarver
 * 
 */
public class MusicPosterGalleryAdapter extends BaseAdapter {

	private static List<MusicArtistContentInfo> posterList = null;

	private final String key;
	private final String category;
	private final ImageLoader imageLoader;
	private Handler handler;
	private static Activity context;

	public MusicPosterGalleryAdapter(Context c, String key, String category) {
		posterList = new ArrayList<MusicArtistContentInfo>();

		imageLoader = SerenityApplication.getImageLoader();
		context = (Activity) c;
		this.key = key;
		this.category = category;

		try {
			fetchData();
		} catch (Exception ex) {
			Log.e(getClass().getName(), "Error connecting to plex.", ex);
		}
	}

	protected void fetchData() {
		handler = new ArtistRetrievalHandler();
		Messenger messenger = new Messenger(handler);
		Intent intent = new Intent(context, MusicRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		intent.putExtra("category", category);
		context.startService(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return posterList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return posterList.get(position);
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

		MusicArtistContentInfo pi = posterList.get(position);
		SerenityMusicImageView mpiv = new SerenityMusicImageView(context, pi);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(180, context);
		int height = ImageUtils.getDPI(180, context);
		mpiv.setLayoutParams(new Gallery.LayoutParams(width, height));

		if (pi.getImageURL() != null) {
			SerenityApplication.displayImage(pi.getImageURL(), mpiv,
					SerenityApplication.getMusicOptions());
		} else {
			mpiv.setImageResource(R.drawable.default_music);
		}
		return mpiv;
	}

	private class ArtistRetrievalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			posterList = (List<MusicArtistContentInfo>) msg.obj;
			Gallery posterGallery = (Gallery) context
					.findViewById(R.id.musicArtistGallery);
			notifyDataSetChanged();
			posterGallery.requestFocus();

		}
	}
}
