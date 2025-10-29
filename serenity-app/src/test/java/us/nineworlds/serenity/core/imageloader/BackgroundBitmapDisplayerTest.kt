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

package us.nineworlds.serenity.core.imageloader

import android.graphics.Bitmap
import android.graphics.drawable.TransitionDrawable
import android.preference.PreferenceManager
import android.view.View
import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import us.nineworlds.serenity.R

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.LEGACY)
class BackgroundBitmapDisplayerTest {

  private lateinit var backgroundView: View

  private val mockBitmap: Bitmap = mockk(relaxed = true)

  private lateinit var backgroundBitmapDisplayer: BackgroundBitmapDisplayer

  @Before
  fun setUp() {
    Robolectric.getBackgroundThreadScheduler().pause()
    Robolectric.getForegroundThreadScheduler().pause()
    backgroundView = View(ApplicationProvider.getApplicationContext())
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `background view does not have animation when fade in not set`() {
    backgroundBitmapDisplayer = BackgroundBitmapDisplayer(mockBitmap, R.drawable.movies, backgroundView)
    backgroundBitmapDisplayer.run()

    val animation = backgroundView.animation
    assertThat(animation).isNull()
  }

  @Test
  fun `background view has transition drawable set when fade in is enabled`() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
    prefs.edit {
      putBoolean("animation_background_fadein", true)
    }

    backgroundBitmapDisplayer = BackgroundBitmapDisplayer(mockBitmap, R.drawable.movies, backgroundView)
    backgroundBitmapDisplayer.run()

    assertThat(backgroundView.background).isInstanceOf(TransitionDrawable::class)
  }

  @Test
  fun `background view has transition drawable with crossfade`() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
    prefs.edit {
      putBoolean("animation_background_fadein", true)
    }

    backgroundBitmapDisplayer = BackgroundBitmapDisplayer(mockBitmap, R.drawable.movies, backgroundView)
    backgroundBitmapDisplayer.run()

    val transitionDrawable = backgroundView.background as TransitionDrawable
    assertThat(transitionDrawable.isCrossFadeEnabled).isTrue()
  }
}