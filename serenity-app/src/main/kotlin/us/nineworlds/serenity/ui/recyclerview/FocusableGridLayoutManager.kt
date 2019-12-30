package us.nineworlds.serenity.ui.recyclerview

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class FocusableGridLayoutManager(val context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) :
  GridLayoutManager(context, spanCount, orientation, reverseLayout) {

  override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
    val smoothScroller = FocusableLinearSmoothScroller(context)

    smoothScroller.targetPosition = position
    startSmoothScroll(smoothScroller)
  }
}