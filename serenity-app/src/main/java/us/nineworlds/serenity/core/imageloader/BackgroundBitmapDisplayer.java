package us.nineworlds.serenity.core.imageloader;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.preference.PreferenceManager;
import android.view.View;

public class BackgroundBitmapDisplayer implements Runnable {

	protected Bitmap bm;
	protected int defaultImage;
	protected View bgLayout;

	public BackgroundBitmapDisplayer(Bitmap bm, int defaultImage, View bgLayout) {
		this.bm = bm;
		this.defaultImage = defaultImage;
		this.bgLayout = bgLayout;
	}

	@Override
	public void run() {
		if (bm == null) {
			bgLayout.setBackgroundResource(defaultImage);
			return;
		}

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(bgLayout.getContext());
		boolean shouldFadeIn = preferences.getBoolean(
				"animation_background_fadein", false);

		BitmapDrawable bmd = new BitmapDrawable(bgLayout.getContext()
				.getResources(), bm);
		if (shouldFadeIn) {
			Drawable currentDrawable = bgLayout.getBackground();
			if (currentDrawable instanceof TransitionDrawable) {
				currentDrawable = ((TransitionDrawable) currentDrawable)
						.getDrawable(1);
			}
			if (currentDrawable == null) {
				currentDrawable = bgLayout.getContext().getResources()
						.getDrawable(defaultImage);
			}
			Drawable[] drawables = { currentDrawable, bmd };
			TransitionDrawable transitionDrawable = new TransitionDrawable(
					drawables);
			bgLayout.setBackgroundDrawable(transitionDrawable);
			transitionDrawable.setCrossFadeEnabled(true);
			int crossfadeduration = 200;
			if (currentDrawable instanceof BitmapDrawable) {
				BitmapDrawable cbmp = (BitmapDrawable) currentDrawable;
				if (cbmp.getBitmap().sameAs(bmd.getBitmap())) {
					transitionDrawable.setCrossFadeEnabled(false);
					crossfadeduration = 0;
				}
			}
			transitionDrawable.startTransition(crossfadeduration);
		} else {
			bgLayout.setBackgroundDrawable(bmd);
		}

	}
}