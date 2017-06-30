package us.nineworlds.serenity.core.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SerenityBackgroundLoaderListener extends
        SimpleImageLoadingListener {

    private final View bgLayout;
    private final int defaultImage;
    private final Activity activity;

    /**
     *
     */
    public SerenityBackgroundLoaderListener(View view, int defaultImage, Activity activity) {
        bgLayout = view;
        this.defaultImage = defaultImage;
        this.activity = activity;

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        activity.runOnUiThread(new BackgroundBitmapDisplayer(loadedImage, defaultImage,
                bgLayout));

    }

}