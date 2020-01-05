package us.nineworlds.serenity.ui.adapters.viewholders

import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import us.nineworlds.serenity.R
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener

abstract class AbstractPosterAdapterDelegate<I : T, T, VH : ViewHolder> : AbsListItemAdapterDelegate<I, T, VH>(),
  View.OnKeyListener {

  internal var handler: Handler? = null

  internal var triggerFocusSelection = true

  private var scaleSmallAnimation: Animation? = null
  private var scaleBigAnimation: Animation? = null

  protected var onItemClickListener: AbstractVideoOnItemClickListener? = null
  var onItemSelectedListener: AbstractVideoOnItemSelectedListener? = null

  protected open fun zoomIn(view: View) {
    view.clearAnimation()
    if (scaleSmallAnimation == null) {
      scaleSmallAnimation =
        AnimationUtils.loadAnimation(view.context, R.anim.anim_scale_small)
    }
    view.startAnimation(scaleSmallAnimation)
  }

  protected open fun zoomOut(view: View) {
    if (scaleBigAnimation == null) {
      scaleBigAnimation = AnimationUtils.loadAnimation(view.context, R.anim.anim_scale_big)
    }
    view.startAnimation(scaleBigAnimation)
  }

  override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
    triggerFocusSelection = true
    if (event.action == KeyEvent.ACTION_UP && isDirectionalPadKeyCode(keyCode)) {
      v.background = ContextCompat.getDrawable(
        v.context,
        R.drawable.rounded_transparent_border
      )
      return true
    }
    if (event.repeatCount > 1 && isDirectionalPadKeyCode(keyCode)) {
      triggerFocusSelection = false
    }
    return false
  }

  protected open fun isDirectionalPadKeyCode(keyCode: Int): Boolean {
    return keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
  }
}