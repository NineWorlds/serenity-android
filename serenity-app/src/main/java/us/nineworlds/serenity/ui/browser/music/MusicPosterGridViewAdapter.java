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

import javax.inject.Inject;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.SerenityImageLoader;
import us.nineworlds.serenity.core.model.impl.MusicArtistContentInfo;
import us.nineworlds.serenity.core.services.MusicRetrievalIntentService;
import us.nineworlds.serenity.injection.InjectingBaseAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.SerenityMusicImageView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jess.ui.TwoWayAbsListView;
import com.jess.ui.TwoWayGridView;

/**
 *
 * @author dcarver
 *
 */
public class MusicPosterGridViewAdapter extends InjectingBaseAdapter {

	protected static MusicPosterGridViewAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private Handler posterGalleryHandler;
	private static List<MusicArtistContentInfo> posterList;
	private static Activity context;
	private final String key;
	private final String category;

	@Inject
	SerenityImageLoader serenityImageLoader;

	public MusicPosterGridViewAdapter(Activity context, String key,
			String category) {
		super();

		notifyAdapter = this;
		MusicPosterGridViewAdapter.context = context;
		this.key = key;
		this.category = category;
		posterList = new ArrayList<MusicArtistContentInfo>();
		fetchDataFromService();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MusicArtistContentInfo pi = posterList.get(position);
		SerenityMusicImageView mpiv = new SerenityMusicImageView(context, pi);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(180, context);
		int height = ImageUtils.getDPI(180, context);
		mpiv.setLayoutParams(new TwoWayAbsListView.LayoutParams(width, height));
		serenityImageLoader.displayImage(pi.getImageURL(), mpiv,
				serenityImageLoader.getMusicOptions());

		return mpiv;
	}

	protected void fetchDataFromService() {
		posterGalleryHandler = new MusicHandler();
		Messenger messenger = new Messenger(posterGalleryHandler);
		Intent intent = new Intent(context, MusicRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		intent.putExtra("category", category);
		context.startService(intent);
	}

	private static class MusicHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			posterList = (List<MusicArtistContentInfo>) msg.obj;
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
