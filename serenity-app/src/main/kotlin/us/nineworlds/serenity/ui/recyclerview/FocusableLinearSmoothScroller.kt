package us.nineworlds.serenity.ui.recyclerview

import android.content.Context
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.View

class FocusableLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

  var targetView : View? = null

  override fun onTargetFound(targetView: View?, state: RecyclerView.State?, action: Action?) {
    super.onTargetFound(targetView, state, action)
    this.targetView = targetView
  }

  override fun onStop() {
    super.onStop()

    if (targetView != null && !targetView!!.hasFocus()) {
      targetView!!.requestFocusFromTouch()
    }
  }
}