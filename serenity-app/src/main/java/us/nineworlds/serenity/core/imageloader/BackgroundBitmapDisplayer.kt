package us.nineworlds.serenity.core.imageloader

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager

class BackgroundBitmapDisplayer(private val bitmap: Bitmap?, private val defaultImageId: Int, private val backgroundView: View) : Runnable {

    override fun run() {
        if (bitmap == null) {
            backgroundView.setBackgroundResource(defaultImageId)
            return
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(backgroundView.context)
        val shouldFadeIn = preferences.getBoolean("animation_background_fadein", false)

        val bitmapDrawable = BitmapDrawable(backgroundView.context.resources, bitmap)

        if (!shouldFadeIn) {
            backgroundView.background = bitmapDrawable
            return
        }

        crossfadeImages(bitmapDrawable)
    }

    private fun crossfadeImages(newBitmapDrawable: BitmapDrawable) {
        var currentDrawable: Drawable? = backgroundView.background
        if (currentDrawable is TransitionDrawable) {
            currentDrawable = currentDrawable.getDrawable(1)
        }

        if (currentDrawable == null) {
            currentDrawable = ContextCompat.getDrawable(backgroundView.context, defaultImageId)
        }

        val drawables = arrayOf(currentDrawable, newBitmapDrawable)
        val transitionDrawable = TransitionDrawable(drawables)
        backgroundView.background = transitionDrawable
        transitionDrawable.isCrossFadeEnabled = true
        var crossFadeDuration = 200
        if (currentDrawable is BitmapDrawable) {
            if (currentDrawable.bitmap == newBitmapDrawable.bitmap) {
                transitionDrawable.isCrossFadeEnabled = false
                crossFadeDuration = 0
            }
        }
        transitionDrawable.startTransition(crossFadeDuration)
    }
}
