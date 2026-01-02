package us.nineworlds.serenity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import javax.inject.Provider
import moxy.ktx.moxyPresenter
import us.nineworlds.serenity.core.ServerConfig
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.databinding.ActivityMainBinding
import us.nineworlds.serenity.databinding.IncludeLoadingProgressBinding
import us.nineworlds.serenity.injection.InjectingMvpActivity
import us.nineworlds.serenity.ui.activity.login.LoginUserActivity
import us.nineworlds.serenity.ui.util.DisplayUtils

open class MainActivity :
    InjectingMvpActivity(),
    MainView {

    @Inject
    lateinit var androidHelper: AndroidHelper

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var presenterProvider: Provider<MainPresenter>

    internal val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var mainMenuContainer: RecyclerView
    lateinit var dataLoadingContainer: View

    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBinding: IncludeLoadingProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
        setContentView(binding.root)

        DisplayUtils.overscanCompensation(this, window.decorView)
        mainMenuContainer = binding.mainGalleryMenu
        dataLoadingContainer = progressBinding.dataLoadingContainer

        val watchedStatusFirstTime = preferences.getBoolean("watched_status_firsttime", true)
        if (watchedStatusFirstTime) {
            presenter.clearCache(this)

            preferences.edit()
                .putBoolean("watched_status_firsttime", false)
                .apply()
        }

        binding.ivUsers.setOnClickListener {
            startActivity(Intent(this, LoginUserActivity::class.java))
            finish()
        }

        presenter.showOrHideUserSelection()
    }

    override fun onResume() {
        super.onResume()

        mainMenuContainer.isFocusable = true
        mainMenuContainer.requestFocusFromTouch()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == MAIN_MENU_PREFERENCE_RESULT_CODE) {
            recreate()
        }
    }

    private fun initPreferences() {
        val config = ServerConfig.getInstance() as? ServerConfig
        config?.let {
            preferences.registerOnSharedPreferenceChangeListener((ServerConfig.getInstance() as ServerConfig).getServerConfigChangeListener())
        }
    }

    private fun initializeDefaultPlayer() {
        val initialRun = preferences.getBoolean("serenity_first_run", true)
        if (initialRun) {
            val editor = preferences.edit()
            if (!androidHelper.isAndroidTV &&
                !androidHelper.isAmazonFireTV &&
                !androidHelper.isLeanbackSupported
            ) {
                editor.putBoolean("external_player", false)
            }
            editor.putBoolean("serenity_first_run", false)
            editor.apply()
        }
    }

    override fun hideMultipleUsersOption() {
        binding.ivUsers.isVisible = false
    }

    override fun showMultipleUsersOption() {
        binding.ivUsers.isVisible = true
    }

    companion object {
        const val MAIN_MENU_PREFERENCE_RESULT_CODE = 100
    }
}
