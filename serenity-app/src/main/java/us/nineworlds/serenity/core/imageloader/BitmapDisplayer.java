package us.nineworlds.serenity.core.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class BitmapDisplayer implements Runnable {

	private Bitmap bm;
	private int defaultImage;
	private View bgLayout;

	/**
	 * 
	 */
	public BitmapDisplayer(Bitmap bm, int defaultImage, View bgLayout) {
		this.bm = bm;
		this.defaultImage = defaultImage;
		this.bgLayout = bgLayout;
	}

	public void run() {
		if (bm == null) {
			bgLayout.setBackgroundResource(defaultImage);
			return;
		}

		BitmapDrawable bmd = new BitmapDrawable(bm);
		bgLayout.setBackgroundDrawable(bmd);
	}

}