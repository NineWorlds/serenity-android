package us.nineworlds.serenity.ui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FocusableLinearSmoothScrollerTest {

  @get:Rule val rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

  private lateinit var smoothScroller: FocusableLinearSmoothScroller

  @Before
  fun setUp() {
    smoothScroller = FocusableLinearSmoothScroller(ApplicationProvider.getApplicationContext())
  }

  @Test
  fun targetViewIsSetToMockView() {
    val mockView = mock<View>()
    val mockState = mock<RecyclerView.State>()
    val mockAction = mock<RecyclerView.SmoothScroller.Action>()
    smoothScroller.onTargetFound(mockView, mockState, mockAction)

    assertThat(smoothScroller.targetView).isEqualTo(mockView)
  }

  @Test
  fun onTargetViewSetToExpectedView() {
    val mockView = mock<View>()

    smoothScroller.targetView = mockView
    doReturn(false).whenever(mockView).hasFocus()

    smoothScroller.onStop()

    verify(mockView).requestFocusFromTouch()
  }
}