package us.nineworlds.serenity.ui.browser.tv

import android.app.Activity
import android.content.SharedPreferences
import android.view.KeyEvent
import timber.log.Timber
import us.nineworlds.serenity.R
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.injection.BaseInjector
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter
import javax.inject.Inject

class OnKeyDownDelegate(internal var activity: Activity) : BaseInjector() {

  @Inject
  internal lateinit var preferences: SharedPreferences
  private var tvRecyclerView: androidx.recyclerview.widget.RecyclerView = activity.findViewById(R.id.tvShowRecyclerView)

  fun onKeyDown(keyCode: Int, keyEvent: KeyEvent): Boolean {
    val adapter = tvRecyclerView.adapter as AbstractPosterImageGalleryAdapter?
    if (adapter != null) {
      val itemsCount = adapter.itemCount

      if (contextMenuRequested(keyCode)) {
        val view = tvRecyclerView.focusedChild
        view.performLongClick()
        return true
      }

      if (tvRecyclerView.focusedChild != null) {
        val selectedItem = tvRecyclerView.layoutManager!!.getPosition(tvRecyclerView.focusedChild)
        Timber.d("SelectedItemPosition: %s", selectedItem)
        if (isKeyCodeSkipBack(keyCode)) {
          var newPosition = selectedItem - 10
          if (newPosition < 0) {
            newPosition = 0
          }
          tvRecyclerView.smoothScrollToPosition(newPosition)
          Timber.d("New ItemPosition: %s", newPosition)
          return true
        }

        if (isKeyCodeSkipForward(keyCode)) {
          var newPosition = selectedItem + 10
          if (newPosition > itemsCount) {
            newPosition = itemsCount - 1
          }
          tvRecyclerView.smoothScrollToPosition(newPosition)
          Timber.d("New ItemPosition: %s", newPosition)
          return true
        }
      }
    }

    if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY || keyCode == KeyEvent.KEYCODE_BUTTON_R1) {
      val selectedItem = tvRecyclerView.getChildAdapterPosition(tvRecyclerView.focusedChild)
      val info =
        (tvRecyclerView.adapter as AbstractPosterImageGalleryAdapter).getItem(selectedItem) as SeriesContentInfo
      FindUnwatchedAsyncTask(activity).execute(info)
      return true
    }

    return false
  }

  private fun contextMenuRequested(keyCode: Int): Boolean {
    val menuKeySlidingMenu = preferences.getBoolean("remote_control_menu", true)
    return (keyCode == KeyEvent.KEYCODE_C
      || keyCode == KeyEvent.KEYCODE_BUTTON_Y
      || keyCode == KeyEvent.KEYCODE_BUTTON_R2
      || keyCode == KeyEvent.KEYCODE_PROG_RED
      || keyCode == KeyEvent.KEYCODE_MENU && !menuKeySlidingMenu)
  }

  private fun isKeyCodeSkipForward(keyCode: Int): Boolean {
    return (keyCode == KeyEvent.KEYCODE_F
      || keyCode == KeyEvent.KEYCODE_PAGE_UP
      || keyCode == KeyEvent.KEYCODE_CHANNEL_UP
      || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
      || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
      || keyCode == KeyEvent.KEYCODE_BUTTON_R1)
  }

  private fun isKeyCodeSkipBack(keyCode: Int): Boolean {
    return (keyCode == KeyEvent.KEYCODE_R
      || keyCode == KeyEvent.KEYCODE_PAGE_DOWN
      || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN
      || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
      || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
      || keyCode == KeyEvent.KEYCODE_BUTTON_L1)
  }
}
