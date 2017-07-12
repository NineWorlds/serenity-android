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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import net.ganin.darv.DpadAwareRecyclerView;

import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.ImageUtils;


public class TVShowPosterImageGalleryAdapter extends TVShowRecyclerAdapter {

    public TVShowPosterImageGalleryAdapter(String key, String category) {
        super(key, category);
    }

    @Override
    protected void createImage(View galleryCellView, SeriesContentInfo pi, int imageWidth, int imageHeight) {
        int width = ImageUtils.getDPI(130, (Activity) galleryCellView.getContext());
        int height = ImageUtils.getDPI(200, (Activity) galleryCellView.getContext());

        initPosterMetaData(galleryCellView, pi, width, height, true);

        galleryCellView.setLayoutParams(new DpadAwareRecyclerView.LayoutParams(width, height));
    }

    @Override
    protected void fetchDataFromService() {

    }

}
