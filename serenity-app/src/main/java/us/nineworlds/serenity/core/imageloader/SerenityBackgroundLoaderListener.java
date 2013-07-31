package us.nineworlds.serenity.core.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class SerenityBackgroundLoaderListener extends
		SimpleImageLoadingListener {

	private View bgLayout;
	private int defaultImage;

	/**
	 * 
	 */
	public SerenityBackgroundLoaderListener(View view, int defaultImage) {
		bgLayout = view;
		this.defaultImage = defaultImage;

	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

		Activity activity = (Activity) bgLayout.getContext();
		activity.runOnUiThread(new BitmapDisplayer(loadedImage, defaultImage,
				bgLayout));

	}
	
}