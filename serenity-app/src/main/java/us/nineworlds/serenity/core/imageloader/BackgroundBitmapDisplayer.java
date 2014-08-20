package us.nineworlds.serenity.core.imageloader;

import us.nineworlds.serenity.R;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class BackgroundBitmapDisplayer implements Runnable {

	protected Bitmap bm;
	protected int defaultImage;
	protected View bgLayout;
	private final Animation fadeIn;

	public BackgroundBitmapDisplayer(Bitmap bm, int defaultImage, View bgLayout) {
		this.bm = bm;
		this.defaultImage = defaultImage;
		this.bgLayout = bgLayout;
		fadeIn = AnimationUtils.loadAnimation(bgLayout.getContext(),
				R.anim.fade_in);
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
		bgLayout.setBackgroundDrawable(bmd);
		if (shouldFadeIn) {
			bgLayout.startAnimation(fadeIn);
		}
	}

}