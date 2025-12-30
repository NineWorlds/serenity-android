package us.nineworlds.serenity.ui.views.statusoverlayview

import android.app.Activity
import assertk.assertThat
import assertk.assertions.isNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StatusOverlayFrameLayoutTest {

    private lateinit var activity: Activity
    private lateinit var view: StatusOverlayFrameLayout

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(Activity::class.java).create().visible().get()
        view = StatusOverlayFrameLayout(activity)
    }

    @After
    fun tearDown() {
        activity.finish()
        if (view.presenter != null) {
            view.onDetachedFromWindow()
        }
    }

    @Test
    fun verifyViewsAreInflated() {
        assertThat(view.infoGraphicMetaContainer).isNotNull()
        assertThat(view.metaOverlay).isNotNull()
        assertThat(view.posterInProgressIndicator).isNotNull()
        assertThat(view.posterOverlayTitle).isNotNull()
        assertThat(view.posterWatchedIndicator).isNotNull()
        assertThat(view.roundedImageView).isNotNull()
        assertThat(view.subtitleIndicator).isNotNull()
        assertThat(view.trailerIndicator).isNotNull()
    }

    @Test
    fun verifyPresenterSet() {
        assertThat(view.presenter).isNotNull()
    }
}
