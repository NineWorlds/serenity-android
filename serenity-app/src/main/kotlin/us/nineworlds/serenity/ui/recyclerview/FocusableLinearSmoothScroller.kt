package us.nineworlds.serenity.ui.recyclerview

/**
 * The MIT License (MIT)
 * Copyright (c) 2018 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.view.View

open class FocusableLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

  var targetView: View? = null

  public override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
    super.onTargetFound(targetView, state, action)
    this.targetView = targetView
  }

  public override fun onStop() {
    super.onStop()

    targetView?.let { targetView ->
      if (!targetView.hasFocus()) {
        targetView.requestFocusFromTouch()
      }
    }
  }
}