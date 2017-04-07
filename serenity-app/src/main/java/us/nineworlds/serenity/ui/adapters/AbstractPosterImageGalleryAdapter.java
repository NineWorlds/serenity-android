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

import javax.inject.Inject;

import android.support.v7.app.AppCompatActivity;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.InjectingRecyclerViewAdapter;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.imageloader.SerenityImageLoader;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.util.ImageUtils;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

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
public abstract class AbstractPosterImageGalleryAdapter extends InjectingRecyclerViewAdapter {

	@Inject
	protected SerenityImageLoader serenityImageLoader;

	@Inject
	protected PlexappFactory factory;

	protected static List<VideoContentInfo> posterList = null;
	protected AppCompatActivity context;
	protected ImageLoader imageLoader;
	protected static final int SIZE_HEIGHT = 400;
	protected static final int SIZE_WIDTH = 200;

	protected Handler handler;
	protected String key;
	protected String category;

	public AbstractPosterImageGalleryAdapter(Context c, String key) {
		context = (AppCompatActivity) c;
		posterList = new ArrayList<VideoContentInfo>();
		imageLoader = serenityImageLoader.getImageLoader();
		this.key = key;
		fetchDataFromService();
	}

	public AbstractPosterImageGalleryAdapter(Context c, String key,
			String category) {
		context = (AppCompatActivity) c;
		this.key = key;
		this.category = category;
		posterList = new ArrayList<VideoContentInfo>();

		imageLoader = serenityImageLoader.getImageLoader();
		fetchDataFromService();
	}

	protected abstract void fetchDataFromService();

	@Override
	public int getItemCount() {
		return posterList.size();
	}

	public Object getItem(int position) {
		if (position >  posterList.size()) {
			return null;
		}
		try {
			return posterList.get(position);
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<VideoContentInfo> getItems() {
		return posterList;
	}

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
