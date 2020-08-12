package us.nineworlds.serenity.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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