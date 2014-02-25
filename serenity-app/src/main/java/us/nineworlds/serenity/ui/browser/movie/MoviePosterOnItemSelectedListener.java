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

package us.nineworlds.serenity.ui.browser.movie;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.ImageUtils;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue.RequestFilter;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * When a poster is selected, update the information displayed in the browser.
 * 
 * @author dcarver
 * 
 */
public class MoviePosterOnItemSelectedListener extends
		AbstractVideoOnItemSelectedListener implements OnItemSelectedListener {

	/**
	 * 
	 */
	public MoviePosterOnItemSelectedListener(Activity activity) {
		super(activity);
	}

	@Override
	protected void createVideoDetail(ImageView v) {
		ImageView posterImage = (ImageView) context
				.findViewById(R.id.video_poster);
		posterImage.setVisibility(View.VISIBLE);
		posterImage.setScaleType(ScaleType.FIT_XY);
		posterImage.setMaxWidth(posterImage.getWidth());
		posterImage.setMaxHeight(posterImage.getHeight());
		ImageLoader imageLoader = SerenityApplication.getImageLoader();
		imageLoader.cancelDisplayTask(posterImage);
		SerenityApplication.displayImage(videoInfo.getImageURL(), posterImage);

		TextView summary = (TextView) context.findViewById(R.id.movieSummary);
		summary.setText(videoInfo.getSummary());

		TextView title = (TextView) context
				.findViewById(R.id.movieBrowserPosterTitle);
		title.setText(videoInfo.getTitle());

		ImageInfographicUtils imageUtilsNormal = new ImageInfographicUtils(100,
				58);

		ImageView crv = imageUtilsNormal.createContentRatingImage(
				videoInfo.getContentRating(), context);
		Drawable drawable = crv.getDrawable();
		BitmapDrawable bmd = (BitmapDrawable) drawable;

		int w = ImageUtils.getDPI(100, (Activity) v.getContext());
		int h = ImageUtils.getDPI(58, (Activity) v.getContext());
		Bitmap bitmap = bmd.getBitmap();

		Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, w, h, false);

		title.setCompoundDrawablesWithIntrinsicBounds(
				null,
				null,
				new BitmapDrawable(v.getContext().getResources(), bitmapResized),
				null);
	}

	@Override
	protected void createVideoMetaData(ImageView v) {
		super.createVideoMetaData(v);
		TextView subt = (TextView) context.findViewById(R.id.subtitleFilter);
		subt.setVisibility(View.GONE);
		Spinner subtitleSpinner = (Spinner) context
				.findViewById(R.id.videoSubtitle);
		subtitleSpinner.setVisibility(View.GONE);
	}

	@Override
	public void onNothingSelected(SerenityAdapterView<?> av) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener
	 * #fetchSubtitle(us.nineworlds.serenity.core.model.VideoContentInfo)
	 */
	@Override
	public void fetchSubtitle(VideoContentInfo mpi) {
		if (queue != null) {
			queue.cancelAll(new RequestFilter() {
				@Override
				public boolean apply(Request<?> request) {
					request.cancel();
					return true;
				}

			});
		}
		super.fetchSubtitle(mpi);
	}

}
