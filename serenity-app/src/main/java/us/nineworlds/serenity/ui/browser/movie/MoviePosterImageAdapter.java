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

package us.nineworlds.serenity.ui.browser.movie;

import java.util.List;

import com.jess.ui.TwoWayAbsListView;
import com.jess.ui.TwoWayGridView;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.MoviesRetrievalIntentService;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.DisplayUtils;
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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * 
 * @author dcarver
 * 
 */
public class MoviePosterImageAdapter extends
		AbstractPosterImageGalleryAdapter {

	protected static MoviePosterImageAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private Handler posterGalleryHandler;
	private Animation shrink;
	private Animation fadeIn;

	public MoviePosterImageAdapter(Context c, String key, String category) {
		super(c, key, category);
		pd = ProgressDialog
				.show(c, "", c.getString(R.string.retrieving_movies));
		notifyAdapter = this;
		shrink = AnimationUtils.loadAnimation(c, R.anim.shrink);
		shrink.setInterpolator(new LinearInterpolator());
		fadeIn = AnimationUtils.loadAnimation(c, R.anim.fade_in);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		VideoContentInfo pi = posterList.get(position);
		SerenityPosterImageView mpiv = new SerenityPosterImageView(context, pi);
		// mpiv.setBackgroundColor(Color.BLACK);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = 0;
		int height = 0;
		int wdpi = DisplayUtils.screenWidthDP(context);

		if (wdpi < 600) {
			width = ImageUtils.getDPI(60, context);
			height = ImageUtils.getDPI(90, context);
		} else {
			width = ImageUtils.getDPI(160, context);
			height = ImageUtils.getDPI(220, context);
		}
		if (!MovieBrowserActivity.IS_GRID_VIEW) {
			mpiv.setLayoutParams(new SerenityGallery.LayoutParams(width, height));
		} else {
			width = ImageUtils.getDPI(120, context);
			height = ImageUtils.getDPI(180, context);
			mpiv.setLayoutParams(new TwoWayAbsListView.LayoutParams(width,
					height));
		}

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean shouldShrink = preferences.getBoolean(
				"animation_shrink_posters", false);
		if (shouldShrink && !MovieBrowserActivity.IS_GRID_VIEW) {
			mpiv.setAnimation(shrink);
		}
		imageLoader.displayImage(pi.getImageURL(), mpiv);

		return mpiv;
	}

	@Override
	protected void fetchDataFromService() {
		posterGalleryHandler = new MoviePosterHandler();
		Messenger messenger = new Messenger(posterGalleryHandler);
		Intent intent = new Intent(context, MoviesRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		intent.putExtra("category", category);
		context.startService(intent);
	}

	private static class MoviePosterHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			posterList = (List<VideoContentInfo>) msg.obj;
			notifyAdapter.notifyDataSetChanged();
			if (!MovieBrowserActivity.IS_GRID_VIEW) {
				SerenityGallery posterGallery = (SerenityGallery) context
						.findViewById(R.id.moviePosterGallery);
				posterGallery.requestFocus();
			} else {
				TwoWayGridView gridView = (TwoWayGridView) context
						.findViewById(R.id.movieGridView);
				gridView.requestFocus();
			}
			pd.dismiss();
		}

	}

}
