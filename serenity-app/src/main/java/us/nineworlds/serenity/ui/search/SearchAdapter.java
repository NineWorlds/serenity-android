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

package us.nineworlds.serenity.ui.search;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;

import us.nineworlds.serenity.widgets.SerenityGallery;

/**
 * @author dcarver
 * 
 */
public class SearchAdapter extends AbstractPosterImageGalleryAdapter {

	public SearchAdapter(Context c, List<VideoContentInfo> videos) {
		super(c, null);
		posterList = videos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View galleryCellView = context.getLayoutInflater().inflate(
				R.layout.poster_indicator_view, null);
		
		VideoContentInfo pi = posterList.get(position);
		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);

		mpiv.setBackgroundColor(Color.BLACK);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(160, context);
		int height = ImageUtils.getDPI(220, context);
		mpiv.setLayoutParams(new RelativeLayout.LayoutParams(width,
				height));
		galleryCellView.setLayoutParams(new SerenityGallery.LayoutParams(width, height));
		SerenityApplication.displayImage(pi.getImageURL(), mpiv);

		if (pi.getViewCount() > 0) {
			ImageView viewed = (ImageView) galleryCellView
					.findViewById(R.id.posterWatchedIndicator);
			viewed.setImageResource(R.drawable.overlaywatched);
		}

		return galleryCellView;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter#
	 * fetchDataFromService()
	 */
	@Override
	protected void fetchDataFromService() {

	}

}
