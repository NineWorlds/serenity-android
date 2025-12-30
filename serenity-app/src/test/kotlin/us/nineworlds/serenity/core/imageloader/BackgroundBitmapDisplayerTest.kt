package us.nineworlds.serenity.core.imageloader

import android.graphics.Bitmap
import android.graphics.drawable.TransitionDrawable
import android.view.View
import androidx.core.content.edit
import androidx.preference.PreferenceManager
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
        backgroundBitmapDisplayer =
            BackgroundBitmapDisplayer(mockBitmap, R.drawable.movies, backgroundView)
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

        backgroundBitmapDisplayer =
            BackgroundBitmapDisplayer(mockBitmap, R.drawable.movies, backgroundView)
        backgroundBitmapDisplayer.run()

        assertThat(backgroundView.background).isInstanceOf(TransitionDrawable::class)
    }

    @Test
    fun `background view has transition drawable with crossfade`() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        prefs.edit {
            putBoolean("animation_background_fadein", true)
        }

        backgroundBitmapDisplayer =
            BackgroundBitmapDisplayer(mockBitmap, R.drawable.movies, backgroundView)
        backgroundBitmapDisplayer.run()

        val transitionDrawable = backgroundView.background as TransitionDrawable
        assertThat(transitionDrawable.isCrossFadeEnabled).isTrue()
    }
}
