package us.nineworlds.serenity.ui.recyclerview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class FocusableLinearLayoutManager(val context: Context) : LinearLayoutManager(context) {

   override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
     val smoothScroller = FocusableLinearSmoothScroller(context)
     smoothScroller.targetPosition = position
     startSmoothScroll(smoothScroller)
  }
}