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

package us.nineworlds.serenity.ui.browser.tv;


import us.nineworlds.serenity.core.model.impl.AbstractSeriesContentInfo;
import us.nineworlds.serenity.ui.adapters.AbstractSeriesContentInfoAdapter;
import us.nineworlds.serenity.ui.util.ImageUtils;

import us.nineworlds.serenity.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 
 * @author dcarver
 * 
 */
public class TVShowBannerImageGalleryAdapter extends
		AbstractSeriesContentInfoAdapter {

	private static final int BANNER_PIXEL_HEIGHT = 140;
	private static final int BANNER_PIXEL_WIDTH = 758;

	public TVShowBannerImageGalleryAdapter(Activity c, String key,
			String category) {
		super(c, key, category);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AbstractSeriesContentInfo pi = tvShowList.get(position);
		TVShowImageView mpiv = new TVShowImageView(context, pi);
		mpiv.setBackgroundResource(R.drawable.gallery_item_background);
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		int width = ImageUtils.getDPI(BANNER_PIXEL_WIDTH, context);
		int height = ImageUtils.getDPI(BANNER_PIXEL_HEIGHT, context);
		mpiv.setLayoutParams(new Gallery.LayoutParams(width, height));

		imageLoader.displayImage(pi.getImageURL(), mpiv);
		return mpiv;
	}

}
