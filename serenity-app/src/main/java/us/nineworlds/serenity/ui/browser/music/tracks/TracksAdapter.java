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

package us.nineworlds.serenity.ui.browser.music.tracks;

import java.util.ArrayList;
import java.util.List;

import com.jess.ui.TwoWayAbsListView;
import com.jess.ui.TwoWayGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import us.nineworlds.serenity.core.model.impl.MusicAlbumContentInfo;
import us.nineworlds.serenity.core.model.impl.MusicArtistContentInfo;
import us.nineworlds.serenity.core.services.MoviesRetrievalIntentService;
import us.nineworlds.serenity.core.services.MusicAlbumRetrievalIntentService;
import us.nineworlds.serenity.core.services.MusicRetrievalIntentService;
import us.nineworlds.serenity.core.services.MusicTrackRetrievalIntentService;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.ui.views.MusicAlbumImageView;
import us.nineworlds.serenity.ui.views.SerenityMusicImageView;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityGallery;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @author dcarver
 * 
 */
public class TracksAdapter extends
		ArrayAdapter<AudioTrackContentInfo> {

	protected static TracksAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private Handler posterGalleryHandler;
	protected ImageLoader imageLoader;
	private static List<AudioTrackContentInfo> posterList;
	private static Activity context;
	private String key;

	public TracksAdapter(Activity context, String key) {
		super(context, R.layout.track_listview_layout, R.id.trackTitle);
		this.context = context;
		this.key = key;
		notifyAdapter = this;
		imageLoader = SerenityApplication.getImageLoader();
		posterList = new ArrayList<AudioTrackContentInfo>();
		addAll(posterList);
		fetchDataFromService();
	}

	protected void fetchDataFromService() {
		posterGalleryHandler = new MusicHandler();
		Messenger messenger = new Messenger(posterGalleryHandler);
		Intent intent = new Intent(context, MusicTrackRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		context.startService(intent);
	}
	
	private class MusicHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			posterList = (List<AudioTrackContentInfo>) msg.obj;
			clear();
			addAll(posterList);
			notifyAdapter.notifyDataSetChanged();
		}

	}

}
