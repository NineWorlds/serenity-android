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

package us.nineworlds.serenity.ui.browser.tv;

import android.os.Handler;
import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemSelectedListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.imageloader.SerenityBackgroundLoaderListener;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.R;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Display selected TV Show Information.
 * 
 * @author dcarver
 * 
 */
public class TVShowGridOnItemSelectedListener implements
		OnItemSelectedListener {

	private View bgLayout;
	private Activity context;
	private ImageLoader imageLoader;
	private View previous;
	private ImageSize bgImageSize = new ImageSize(1280, 720);
	private SeriesContentInfo videoInfo;
	private Handler handler = new Handler();
	private Runnable runnable;

	/**
	 * 
	 */
	public TVShowGridOnItemSelectedListener(View bgv, Activity activity) {
		bgLayout = bgv;
		context = activity;

		imageLoader = SerenityApplication.getImageLoader();

	}

	@Override
	public void onItemSelected(TwoWayAdapterView<?> av, View v, int position, long id) {

		videoInfo = (SeriesContentInfo) av.getItemAtPosition(position);
		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
		}

		previous = v;

		v.setPadding(5, 5, 5, 5);
		
		final ImageView imageView = (ImageView) v.findViewById(R.id.posterImageView);

		if (runnable != null) {
			handler.removeCallbacks(runnable);
		}
		runnable = new Runnable() {
			@Override
			public void run() {
				changeBackgroundImage(imageView);
				runnable = null;
			}
		};
		handler.postDelayed(runnable, 500);

		TextView titleView = (TextView) context.findViewById(R.id.tvShowGridTitle);
		if (titleView != null) {
			titleView.setText(videoInfo.getTitle());
		}
	}

	/**
	 * Change the background image of the activity.
	 * 
	 * Should be a background activity
	 * 
	 * @param v
	 */
	private void changeBackgroundImage(View v) {

		ImageView mpiv = (ImageView) v;

		imageLoader.loadImage(videoInfo.getBackgroundURL(), bgImageSize,
				new SerenityBackgroundLoaderListener(bgLayout,
						R.drawable.tvshows));
	}

	@Override
	public void onNothingSelected(TwoWayAdapterView<?> arg0) {

	}

}
