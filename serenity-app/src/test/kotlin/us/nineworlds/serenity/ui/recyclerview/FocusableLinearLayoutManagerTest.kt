package us.nineworlds.serenity.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import us.nineworlds.serenity.test.InjectingTest

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FocusableLinearLayoutManagerTest {

  private val mockRecyclerView = mock<RecyclerView>()

  private lateinit var layoutManager: FocusableLinearLayoutManager

  @Before
  fun setUp() {
    layoutManager = spy(FocusableLinearLayoutManager(ApplicationProvider.getApplicationContext()))
  }

  @Test
  fun setsSmoothScroller() {
    doNothing().whenever(layoutManager).startSmoothScroll(any<FocusableLinearSmoothScroller>())
    layoutManager.smoothScrollToPosition(mockRecyclerView, null, 0)

    verify(layoutManager).startSmoothScroll(any<FocusableLinearSmoothScroller>())
  }

}