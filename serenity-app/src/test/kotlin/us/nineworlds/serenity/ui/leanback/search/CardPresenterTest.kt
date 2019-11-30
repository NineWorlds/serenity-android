package us.nineworlds.serenity.ui.leanback.search

import android.app.Activity
import androidx.leanback.widget.ImageCardView
import androidx.core.content.ContextCompat
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.atLeast
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.VideoContentInfo

@RunWith(RobolectricTestRunner::class)
class CardPresenterTest {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

  @Mock
  lateinit var mockImageCardView: ImageCardView
  @Mock
  lateinit var mockVideoContentInfo: VideoContentInfo
  @Mock
  lateinit var mockViewHolder: CardPresenter.CardPresenterViewHolder

  lateinit var presenter: CardPresenter

  @Before
  fun setUp() {
    presenter = CardPresenter(getApplicationContext())
  }

  @Test
  fun onCreateViewHolderReturnsExpectedViewHolderInstance() {
    val linearLayout = LinearLayout(getApplicationContext())
    val spy = spy(presenter)
    doReturn(mockImageCardView).whenever(spy).createImageView()

    val result = spy.onCreateViewHolder(linearLayout)

    assertThat(result).isInstanceOf(CardPresenter.CardPresenterViewHolder::class.java)
  }

  @Test
  fun imageCardViewHasExpectedValuesSetWhenViewHolderIsCreated() {
    val linearLayout = LinearLayout(getApplicationContext())
    val spy = spy(presenter)
    doReturn(mockImageCardView).whenever(spy).createImageView()

    spy.onCreateViewHolder(linearLayout)

    verify(mockImageCardView).isFocusable = true
    verify(mockImageCardView).isFocusableInTouchMode = true
    verify(mockImageCardView).setBackgroundColor(ContextCompat.getColor(linearLayout.context, R.color.holo_color))
  }

  @Test
  fun onBindViewHolderSetsExpectedImageDimensions() {
    val activity = Robolectric.buildActivity(Activity::class.java).create().get()
    val spy = spy(presenter)
    doReturn(mockImageCardView).whenever(mockViewHolder).cardView
    doReturn("").whenever(mockVideoContentInfo).imageURL
    doReturn(activity).whenever(spy).getActivity(anyOrNull())

    spy.onBindViewHolder(mockViewHolder, mockVideoContentInfo)

    verify(mockViewHolder).movie = mockVideoContentInfo
    verify(mockVideoContentInfo, atLeast(2)).imageURL
    verify(mockImageCardView).titleText = null
    verify(mockImageCardView).contentText = null
    verify(spy).getActivity(anyOrNull())
    verify(mockImageCardView).setMainImageDimensions(anyInt(), anyInt())
    verify(mockViewHolder).updateCardViewImage(anyOrNull())
  }

  @Test
  fun unBindViewHolderResetsViews() {

    presenter.onUnbindViewHolder(mockViewHolder)

    verify(mockViewHolder).reset()

  }
}
