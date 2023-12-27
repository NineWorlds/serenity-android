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
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package us.nineworlds.serenity

import us.nineworlds.serenity.injection.BaseInjector
import javax.inject.Inject
import android.content.SharedPreferences
import android.app.Activity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import us.nineworlds.serenity.core.menus.MenuItem
import us.nineworlds.serenity.fragments.mainmenu.MainMenuPresenter
import javax.inject.Provider

class GalleryOnItemSelectedListener(private val adapter: MainMenuTextViewAdapter, private val presenter: MainMenuPresenter) : BaseInjector(), IGalleryOnItemSelectedListener {
    @Inject
    lateinit var preferences: SharedPreferences

    lateinit var mainGalleryBackgroundView: ImageView

    private var currentlySelectedItem: MenuItem? = null

    private fun shouldFadeIn(): Boolean {
        return preferences.getBoolean("animation_background_mainmenu_fadein", true)
    }

    fun getBackgroundImageId(menuItem: MenuItem): Int {
        if ("movie" == menuItem.type || "movies" == menuItem.type) {
            return R.drawable.movies
        }
        if ("show" == menuItem.type || "tvshows" == menuItem.type) {
            return R.drawable.tvshows
        }
        if ("artist" == menuItem.type || "music" == menuItem.type) {
            return R.drawable.music
        }
        if ("settings" == menuItem.type) {
            return R.drawable.settings
        }
        if ("options" == menuItem.type) {
            return R.drawable.settings
        }
        return if ("search" == menuItem.type) {
            R.drawable.search
        } else R.drawable.serenity_bonsai_logo
    }

    fun onItemSelected(view: View, hasFocus: Boolean, position: Int) {
        mainGalleryBackgroundView = view.findViewById(R.id.mainGalleryBackground);
        val menuItem = adapter.getItemAtPosition(position)
        val section = menuItem.section
        if (section != null) {
            if (currentlySelectedItem != menuItem) {
                currentlySelectedItem = menuItem
                presenter.populateMovieCategories(section, menuItem.type!!)
            }
        }

        val context = view!!.context as Activity
        if (context.isDestroyed) {
            return
        }
        view.clearAnimation()
        view.background = null
        mainGalleryBackgroundView = context.findViewById(R.id.mainGalleryBackground);
        if (hasFocus && view != null) {
            mainGalleryBackgroundView = context.findViewById(R.id.mainGalleryBackground)
            mainGalleryBackgroundView.clearAnimation()
            GlideApp.with(context).load(getBackgroundImageId(menuItem)).into(mainGalleryBackgroundView)
            view.clearAnimation()
            view.background = ContextCompat.getDrawable(view.context, R.drawable.rounded_transparent_border)
            if (shouldFadeIn()) {
                val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
                fadeIn.duration = 500
                mainGalleryBackgroundView.startAnimation(fadeIn)
            }
        }
    }
}