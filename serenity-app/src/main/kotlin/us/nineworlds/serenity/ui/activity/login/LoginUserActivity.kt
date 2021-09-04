package us.nineworlds.serenity.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import androidx.annotation.VisibleForTesting
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import moxy.ktx.moxyPresenter
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.databinding.ActivityLoginUserBinding
import us.nineworlds.serenity.databinding.IncludeLoadingProgressBinding
import us.nineworlds.serenity.injection.InjectingMvpActivity
import javax.inject.Inject
import javax.inject.Provider

class LoginUserActivity : InjectingMvpActivity(), LoginUserContract.LoginUserView {

  @VisibleForTesting
  internal val presenter by moxyPresenter { presenterProvider.get() }

  @Inject
  lateinit var presenterProvider: Provider<LoginUserPresenter>

  lateinit var adapter: LoginUserAdapter

  internal lateinit var binding: ActivityLoginUserBinding
  internal lateinit var progressBinding: IncludeLoadingProgressBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)

    binding = ActivityLoginUserBinding.inflate(layoutInflater)
    progressBinding = IncludeLoadingProgressBinding.bind(binding.root)

    setContentView(binding.root)

    setupProfileContainer()
    progressBinding.dataLoadingContainer.visibility = VISIBLE

    if (intent.hasExtra("server")) {
      val server: Server? = intent.getSerializableExtra("server") as Server
      server?.let { presenter.initPresenter(server) }
    }

    presenter.retrieveAllUsers()
  }

  override fun onResume() {
    super.onResume()

    binding.loginUserContainer.requestFocusFromTouch()
  }

  private fun setupProfileContainer() {
    val context = this
    binding.apply {
      loginUserContainer.visibility = GONE
      adapter = LoginUserAdapter(presenter)
      loginUserContainer.adapter = adapter
      val layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
      layoutManager.justifyContent = JustifyContent.CENTER
      loginUserContainer.layoutManager = layoutManager
      loginUserContainer.addItemDecoration(FlexboxItemDecoration(context))
    }
  }

  override fun displayUsers(serenityUser: List<SerenityUser>) {
    progressBinding.dataLoadingContainer.visibility = GONE
    binding.apply {
      loginUserContainer.visibility = VISIBLE
      adapter.loadUsers(serenityUser)
      loginUserContainer.requestFocusFromTouch()
    }
  }

  override fun launchNextScreen() {
    val intent = Intent(this, AndroidTV::class.java)
    startActivity(intent)
    finish()
  }
}