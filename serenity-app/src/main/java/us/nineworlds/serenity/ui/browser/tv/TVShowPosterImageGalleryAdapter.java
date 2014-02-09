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

import com.jess.ui.TwoWayAbsListView;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.ImageUtils;

import us.nineworlds.serenity.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 
 * @author dcarver
 * 
 */
public class TVShowPosterImageGalleryAdapter extends
		TVShowBannerImageGalleryAdapter {


	private String key;

	public TVShowPosterImageGalleryAdapter(Context c, String key,
			String category) {
		super(c, key, category);
		showActivity = (SerenityMultiViewVideoActivity) c;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View galleryCellView = showActivity.getLayoutInflater().inflate(
				R.layout.poster_tvshow_indicator_view, null);

		SeriesContentInfo pi = tvShowList.get(position);
		ImageView mpiv = (ImageView) galleryCellView
				.findViewById(R.id.posterImageView);
		
		createImage(galleryCellView, pi, 120, 180);

		toggleWatchedIndicator(galleryCellView, pi);
		return galleryCellView;
	}
	
	@Override
	protected void createImage(View galleryCellView, SeriesContentInfo pi,
			int imageWidth, int imageHeight) {
		int width = ImageUtils.getDPI(imageWidth, context);
		int height = ImageUtils.getDPI(imageHeight, context);
		
		initPosterMetaData(galleryCellView, pi, width, height, true);
		
		SerenityMultiViewVideoActivity c = showActivity;
		
		if (c.isGridViewActive()) {
			galleryCellView.setLayoutParams(new TwoWayAbsListView.LayoutParams(width, height));

		} else {
			galleryCellView.setLayoutParams(new Gallery.LayoutParams(width,
					height));
		}		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.kingargyle.plexappclient.ui.adapters.
	 * AbstractPosterImageGalleryAdapter#fetchDataFromService()
	 */
	@Override
	protected void fetchDataFromService() {
		// TODO Auto-generated method stub

	}

}
