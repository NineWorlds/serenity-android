package moxy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

@Suppress("unused")
open class MvpAppCompatActivity :
    AppCompatActivity(),
    MvpDelegateHolder {

    private val _mvpDelegate: MvpDelegate<out MvpAppCompatActivity> by lazy {
        MvpDelegate<MvpAppCompatActivity>(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()

        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        getMvpDelegate().onDestroyView()

        if (isFinishing()) {
            getMvpDelegate().onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Activity.
     */
    override fun getMvpDelegate(): MvpDelegate<*> = _mvpDelegate
}
