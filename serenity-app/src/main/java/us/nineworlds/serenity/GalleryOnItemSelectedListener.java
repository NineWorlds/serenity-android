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

package us.nineworlds.serenity;

import us.nineworlds.serenity.ui.views.MainMenuTextView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;

public class GalleryOnItemSelectedListener implements OnItemSelectedListener {

	private final ImageView mainGalleryBackgroundView;
	private MainMenuTextView preSelected;

	public GalleryOnItemSelectedListener(View v) {
		mainGalleryBackgroundView = (ImageView) v;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long arg3) {
		if (v instanceof MainMenuTextView) {
			mainGalleryBackgroundView.clearAnimation();
			MainMenuTextView tv = (MainMenuTextView) v;

			tv.setTextColor(v.getContext().getResources()
					.getColor(android.R.color.white));
			tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);

			if (preSelected != null) {
				preSelected.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				preSelected.setTextColor(Color.parseColor("#414141"));
			}

			preSelected = tv;

			String url = "drawable://" + tv.getBackgroundImageId();
			SerenityApplication.displayImage(url, mainGalleryBackgroundView,
					tv.getBackgroundImageId());

			// mainGalleryBackgroundView.setImageResource(tv.getBackgroundImageId());
			if (shouldFadeIn()) {
				Animation fadeIn = AnimationUtils.loadAnimation(v.getContext(),
						R.anim.fade_in);
				fadeIn.setDuration(500);
				mainGalleryBackgroundView.startAnimation(fadeIn);
			}

		}

	}

	/**
	 * @return
	 */
	protected boolean shouldFadeIn() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mainGalleryBackgroundView
						.getContext());
		boolean shouldFadein = preferences.getBoolean(
				"animation_background_mainmenu_fadein", true);
		return shouldFadein;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}
