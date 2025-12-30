package us.nineworlds.serenity.ui.leanback.search

import android.app.Activity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoContentInfo

@RunWith(RobolectricTestRunner::class)
class CardPresenterTest {

    private companion object {
        private val mockImageCardView = mockk<ImageCardView>(relaxed = true)
        private val mockVideoContentInfo = mockk<VideoContentInfo>(relaxed = true)
        private val mockViewHolder = mockk<CardPresenter.CardPresenterViewHolder>(relaxed = true)
    }

    private lateinit var presenter: CardPresenter

    @Before
    fun setUp() {
        presenter = CardPresenter(getApplicationContext())
    }

    @Test
    fun onCreateViewHolderReturnsExpectedViewHolderInstance() {
        val linearLayout = LinearLayout(getApplicationContext())
        val spy = spyk(presenter)
        every { spy.createImageView() } returns mockImageCardView

        val result = spy.onCreateViewHolder(linearLayout)

        assertThat(result).isInstanceOf(CardPresenter.CardPresenterViewHolder::class.java)
    }

    @Test
    fun imageCardViewHasExpectedValuesSetWhenViewHolderIsCreated() {
        val linearLayout = LinearLayout(getApplicationContext())
        val spy = spyk(presenter)
        every { spy.createImageView() } returns mockImageCardView

        spy.onCreateViewHolder(linearLayout)

        verify { mockImageCardView.isFocusable = true }
        verify { mockImageCardView.isFocusableInTouchMode = true }
        verify { mockImageCardView.setBackgroundColor(ContextCompat.getColor(linearLayout.context, R.color.holo_color)) }
    }

    @Test
    fun onBindViewHolderSetsExpectedImageDimensions() {
        val activity = Robolectric.buildActivity(Activity::class.java).create().get()
        val spy = spyk(presenter)
        every { mockViewHolder.cardView } returns mockImageCardView
        every { mockVideoContentInfo.getImageURL() } returns ""
        every { spy.getActivity(any()) } returns activity

        spy.onBindViewHolder(mockViewHolder, mockVideoContentInfo)

        verify { mockViewHolder.movie = mockVideoContentInfo }
        verify(atLeast = 2) { mockVideoContentInfo.getImageURL() }
        verify { spy.getActivity(any()) }
        verify { mockImageCardView.setMainImageDimensions(any(), any()) }
        verify { mockViewHolder.updateCardViewImage(any()) }
    }

    @Test
    fun unBindViewHolderResetsViews() {
        presenter.onUnbindViewHolder(mockViewHolder)

        verify { mockViewHolder.reset() }
    }
}
