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

package us.nineworlds.serenity.ui.browser.music.albums;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.impl.MusicAlbumContentInfo;
import us.nineworlds.serenity.core.services.MusicAlbumRetrievalIntentService;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.MusicAlbumImageView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jess.ui.TwoWayAbsListView;
import com.jess.ui.TwoWayGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author dcarver
 * 
 */
public class MusicAlbumsCoverAdapter extends BaseAdapter {

	protected static MusicAlbumsCoverAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private Handler posterGalleryHandler;
	protected ImageLoader imageLoader;
	private static List<MusicAlbumContentInfo> posterList;
	private static Activity context;
	private final String key;

	public MusicAlbumsCoverAdapter(Activity context, String key) {
		notifyAdapter = this;
		MusicAlbumsCoverAdapter.context = context;
		this.key = key;
		imageLoader = SerenityApplication.getImageLoader();
		posterList = new ArrayList<MusicAlbumContentInfo>();
		fetchDataFromService();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MusicAlbumContentInfo pi = posterList.get(position);
		MusicAlbumImageView mpiv = new MusicAlbumImageView(context, pi);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(180, context);
		int height = ImageUtils.getDPI(180, context);
		mpiv.setLayoutParams(new TwoWayAbsListView.LayoutParams(width, height));
		if (pi.getImageURL() != null && pi.getImageURL().length() > 0) {
			SerenityApplication.displayImage(pi.getImageURL(), mpiv,
					SerenityApplication.getMusicOptions());
		} else {
			mpiv.setImageResource(R.drawable.default_music);
		}

		return mpiv;
	}

	protected void fetchDataFromService() {
		posterGalleryHandler = new MusicHandler();
		Messenger messenger = new Messenger(posterGalleryHandler);
		Intent intent = new Intent(context,
				MusicAlbumRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		context.startService(intent);
	}

	private static class MusicHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			posterList = (List<MusicAlbumContentInfo>) msg.obj;
			TwoWayGridView gridView = (TwoWayGridView) context
					.findViewById(R.id.musicGridView);
			gridView.requestFocus();
			notifyAdapter.notifyDataSetChanged();
			// pd.dismiss();
		}

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

}
