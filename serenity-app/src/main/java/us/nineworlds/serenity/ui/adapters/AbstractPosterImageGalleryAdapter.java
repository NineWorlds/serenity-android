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

package us.nineworlds.serenity.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.volley.VolleyUtils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * An abstract class for handling the creation of video content for use during
 * browsing. Implementations need to implement the abstract methods to provide
 * functionality for retrieval and display of video content when browsing the
 * episodes.
 * 
 * @author dcarver
 * 
 */
public abstract class AbstractPosterImageGalleryAdapter extends BaseAdapter {

	protected static List<VideoContentInfo> posterList = null;
	protected static Activity context;
	protected ImageLoader imageLoader;
	protected static final int SIZE_HEIGHT = 400;
	protected static final int SIZE_WIDTH = 200;
	protected PlexappFactory factory;
	protected Handler handler;
	protected String key;
	protected String category;
	protected Animation shrink;
	protected Animation fadeIn;
	protected RequestQueue queue;

	public AbstractPosterImageGalleryAdapter(Context c, String key) {
		queue = VolleyUtils.getRequestQueueInstance(c);
		context = (Activity) c;
		posterList = new ArrayList<VideoContentInfo>();
		imageLoader = SerenityApplication.getImageLoader();
		this.key = key;
		fetchDataFromService();
	}

	public AbstractPosterImageGalleryAdapter(Context c, String key,
			String category) {
		queue = VolleyUtils.getRequestQueueInstance(c);
		context = (Activity) c;
		this.key = key;
		this.category = category;
		posterList = new ArrayList<VideoContentInfo>();

		shrink = AnimationUtils.loadAnimation(c, R.anim.shrink);
		shrink.setInterpolator(new LinearInterpolator());
		fadeIn = AnimationUtils.loadAnimation(c, R.anim.fade_in);

		imageLoader = SerenityApplication.getImageLoader();
		fetchDataFromService();
	}

	protected abstract void fetchDataFromService();

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

	/**
	 * @param galleryCellView
	 * @param pi
	 * @param watchedView
	 */

	public List<VideoContentInfo> getItems() {
		return posterList;
	}

	/**
	 * @param image
	 */
	public void shrinkPosterAnimation(ImageView image, boolean isGridView) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean shouldShrink = preferences.getBoolean(
				"animation_shrink_posters", false);
		if (shouldShrink && !isGridView) {
			image.setAnimation(shrink);
		}
	}

	/**
	 * @param galleryCellView
	 * @param pi
	 */
	public void setWatchedStatus(View galleryCellView, VideoContentInfo pi) {
		ImageView watchedView = (ImageView) galleryCellView
				.findViewById(R.id.posterWatchedIndicator);

		if (pi.isPartiallyWatched()) {
			ImageUtils.toggleProgressIndicator(galleryCellView,
					pi.getResumeOffset(), pi.getDuration());
		} else if (pi.isWatched()) {
			watchedView.setImageResource(R.drawable.overlaywatched);
			watchedView.setVisibility(View.VISIBLE);
		} else {
			watchedView.setVisibility(View.INVISIBLE);
		}
	}

}
