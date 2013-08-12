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

package us.nineworlds.serenity.core.imageloader;

import us.nineworlds.serenity.R;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * @author dcarver
 *
 */
public class MainMenuBackgroundBitmapDisplayer extends BitmapDisplayer {

	/**
	 * @param bm
	 * @param defaultImage
	 * @param bgLayout
	 */
	public MainMenuBackgroundBitmapDisplayer(Bitmap bm, int defaultImage,
			View bgLayout) {
		super(bm, defaultImage, bgLayout);
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.imageloader.BitmapDisplayer#run()
	 */
	@Override
	public void run() {
		if (bm == null) {
			((ImageView) bgLayout).setImageResource(defaultImage);
			return;
		}
		ImageView imageView = (ImageView) bgLayout;
		imageView.setImageBitmap(bm);
		if (shouldFadeIn()) {
			Animation fadeIn = AnimationUtils.loadAnimation(bgLayout.getContext(), R.anim.fade_in);
			fadeIn.setDuration(700);
			imageView.startAnimation(fadeIn);
		}
	}

	/**
	 * @return
	 */
	protected boolean shouldFadeIn() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(bgLayout.getContext());
		boolean shouldFadein = preferences.getBoolean(
				"animation_background_mainmenu_fadein", true);
		return shouldFadein;
	}

}
