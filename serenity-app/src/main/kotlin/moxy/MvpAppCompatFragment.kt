package moxy

import android.os.Bundle
import androidx.fragment.app.Fragment

@Suppress("unused")
open class MvpAppCompatFragment : Fragment(), MvpDelegateHolder {

  private var isStateSaved = false

  private val _mvpDelegate: MvpDelegate<out MvpAppCompatFragment> by lazy {
    MvpDelegate<MvpAppCompatFragment>(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    getMvpDelegate().onCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    isStateSaved = false
    getMvpDelegate().onAttach()
  }

  override fun onResume() {
    super.onResume()
    isStateSaved = false
    getMvpDelegate().onAttach()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    isStateSaved = true
    getMvpDelegate().onSaveInstanceState(outState)
    getMvpDelegate().onDetach()
  }

  override fun onStop() {
    super.onStop()
    getMvpDelegate().onDetach()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    getMvpDelegate().onDetach()
    getMvpDelegate().onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()

    // We leave the screen and respectively all fragments will be destroyed
    if (requireActivity().isFinishing) {
      getMvpDelegate().onDestroy()
      return
    }

    // When we rotate device isRemoving() return true for fragment placed in backstack
    // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
    if (isStateSaved) {
      isStateSaved = false
      return
    }

    var anyParentIsRemoving = false
    var parent = parentFragment
    while (!anyParentIsRemoving && parent != null) {
      anyParentIsRemoving = parent.isRemoving
      parent = parent.parentFragment
    }

    if (isRemoving || anyParentIsRemoving) {
      getMvpDelegate().onDestroy()
    }
  }

  /**
   * @return The [MvpDelegate] being used by this Fragment.
   */
  override fun getMvpDelegate(): MvpDelegate<*> = _mvpDelegate
}
