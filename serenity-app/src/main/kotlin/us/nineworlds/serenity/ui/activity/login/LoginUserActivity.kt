package us.nineworlds.serenity.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.FrameLayout
import app.com.tvrecyclerview.TvRecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.injection.InjectingMvpActivity
import javax.inject.Inject
import javax.inject.Provider

class LoginUserActivity : InjectingMvpActivity(), LoginUserContract.LoginUserView {

  @InjectPresenter
  lateinit var presenter: LoginUserPresenter

  @Inject
  lateinit var presenterProvider: Provider<LoginUserPresenter>

  @ProvidePresenter
  fun providePresenter(): LoginUserPresenter = presenterProvider.get()

  @BindView(R.id.login_user_container)
  lateinit var profileContainer: TvRecyclerView

  @BindView(R.id.data_loading_container)
  lateinit var dataLoadingContainer: FrameLayout

  lateinit var adapter: LoginUserAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)

    setContentView(R.layout.activity_login_user)

    ButterKnife.bind(this)

    val server = intent.getSerializableExtra("server") as Server

    setupProfileContainer()

    dataLoadingContainer.visibility = VISIBLE

    presenter.initPresenter(server)
    presenter.retrieveAllUsers()
  }

  private fun setupProfileContainer() {
    profileContainer.visibility = GONE
    adapter = LoginUserAdapter(presenter)
    profileContainer.adapter = adapter
    val layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW)
    layoutManager.justifyContent = JustifyContent.CENTER
    profileContainer.layoutManager = layoutManager
    profileContainer.addItemDecoration(FlexboxItemDecoration(this))
    profileContainer.setOnItemStateListener(adapter)
  }

  override fun displayUsers(serenityUser: List<SerenityUser>) {
    dataLoadingContainer.visibility = GONE
    profileContainer.visibility = VISIBLE
    adapter.loadUsers(serenityUser)
  }

  override fun launchNextScreen() {
    val intent = Intent(this, AndroidTV::class.java)
    startActivity(intent)
    finish()
  }
}