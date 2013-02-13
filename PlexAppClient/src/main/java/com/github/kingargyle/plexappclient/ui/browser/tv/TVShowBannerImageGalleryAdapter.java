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

package com.github.kingargyle.plexappclient.ui.browser.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.Genre;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.github.kingargyle.plexappclient.core.model.impl.AbstractSeriesContentInfo;
import com.github.kingargyle.plexappclient.core.model.impl.TVShowSeriesInfo;
import com.github.kingargyle.plexappclient.core.services.MoviesRetrievalIntentService;
import com.github.kingargyle.plexappclient.core.services.ShowRetrievalIntentService;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;
import android.widget.TextView;

/**
 * 
 * @author dcarver
 *
 */
public class TVShowBannerImageGalleryAdapter extends BaseAdapter {
	
	private static List<TVShowSeriesInfo> tvShowList = null;
	private static Activity context;
	
	private ImageManager imageManager;
	private ImageTagFactory imageTagFactory;
	private static final int SIZE_HEIGHT = 200;
	private static final int SIZE_WIDTH = 400;
	private Handler handler;
	private String baseUrl;
	private String key;
	private static TVShowBannerImageGalleryAdapter notifyAdapter;
	private static ProgressDialog pd;
	
	public TVShowBannerImageGalleryAdapter(Context c, String key) {
		context = (Activity)c;
		tvShowList = new ArrayList<TVShowSeriesInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.newInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
		this.key = key;
		
		notifyAdapter = this;
		try {
			baseUrl = SerenityApplication.getPlexFactory().baseURL();
			fetchData();
		} catch (Exception ex) {
			Log.e(getClass().getName(), "Error connecting to plex.", ex);
		}
	}
	
	protected void fetchData() {
		pd = ProgressDialog.show(context, "", "Retrieving Shows.");
		handler = new ShowRetrievalHandler();
		Messenger messenger = new Messenger(handler);
		Intent intent = new Intent(context, ShowRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		context.startService(intent);
	}
	

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return tvShowList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return tvShowList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AbstractSeriesContentInfo pi = tvShowList.get(position);
		TVShowBannerImageView mpiv = new TVShowBannerImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL(), context));
		} else {
			mpiv.setTag(imageTagFactory.build(baseUrl + ":/resources/show-fanart.jpg", context));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = 768;
		int height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		mpiv.setLayoutParams(new Gallery.LayoutParams(width, height));
		
		imageManager.getLoader().load(mpiv);
		return mpiv;
	}
	
	private static class ShowRetrievalHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			
			tvShowList = (List<TVShowSeriesInfo>) msg.obj;
			if (tvShowList != null) {
				TextView tv = (TextView)context.findViewById(R.id.tvShowItemCount);
				tv.setText(Integer.toString(tvShowList.size()) + " Item(s)");
				notifyAdapter.notifyDataSetChanged();
				pd.dismiss();
			}
		}
	}

}
