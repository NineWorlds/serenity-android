package us.nineworlds.serenity.core.imageloader;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * A runnable that transitions between a drawable and a bitmap.
 * <p>
 * It supports not doing transitions when the bitmaps are the same.
 * <p>
 * It currently depends on setting transitions at all if there is a fade in
 * transition preference enabled in the app. Remove that section of code if you
 * don't need this support.
 * <p>
 * If transition support isn't enabled then it doesn't do transitions and just
 * sets the bitmap as the background to the view passed in the constructor.
 * <p>
 * Code licensed under an MIT license so do what you want just give attribution
 * where it was obtained.
 *
 * @author davidcarver
 */

public class BackgroundBitmapDisplayer implements Runnable {

    protected Bitmap bitmap;
    protected int defaultImageId;
    protected View backgroundView;

    public BackgroundBitmapDisplayer(Bitmap bitmap, int defaultImage, View view) {
        this.bitmap = bitmap;
        this.defaultImageId = defaultImage;
        this.backgroundView = view;
    }

    @Override
    public void run() {
        if (bitmap == null) {
            backgroundView.setBackgroundResource(defaultImageId);
            return;
        }

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(backgroundView.getContext());
        boolean shouldFadeIn = preferences.getBoolean(
                "animation_background_fadein", false);

        BitmapDrawable bitmapdrawable = new BitmapDrawable(backgroundView
                .getContext().getResources(), bitmap);

        if (!shouldFadeIn) {
            backgroundView.setBackgroundDrawable(bitmapdrawable);
            return;
        }

        crossfadeImages(bitmapdrawable);
    }

    private void crossfadeImages(BitmapDrawable newBitmapDrawable) {
        Drawable currentDrawable = backgroundView.getBackground();
        if (currentDrawable instanceof TransitionDrawable) {
            currentDrawable = ((TransitionDrawable) currentDrawable)
                    .getDrawable(1);
        }

        if (currentDrawable == null) {
            currentDrawable = backgroundView.getContext().getResources()
                    .getDrawable(defaultImageId);
        }

        Drawable[] drawables = {currentDrawable, newBitmapDrawable};
        TransitionDrawable transitionDrawable = new TransitionDrawable(
                drawables);
        backgroundView.setBackgroundDrawable(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(true);
        int crossfadeduration = 200;
        if (currentDrawable instanceof BitmapDrawable) {
            BitmapDrawable currentBitmapDrawable = (BitmapDrawable) currentDrawable;
            if (currentBitmapDrawable.getBitmap().sameAs(
                    newBitmapDrawable.getBitmap())) {
                transitionDrawable.setCrossFadeEnabled(false);
                crossfadeduration = 0;
            }
        }
        transitionDrawable.startTransition(crossfadeduration);
    }
}