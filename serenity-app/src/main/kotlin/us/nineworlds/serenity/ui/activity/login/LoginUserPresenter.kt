package us.nineworlds.serenity.ui.activity.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.birbit.android.jobqueue.JobManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.injection.SerenityObjectGraph
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.events.users.AllUsersEvent
import us.nineworlds.serenity.events.users.AuthenticatedUserEvent
import us.nineworlds.serenity.jobs.AuthenticateUserJob
import us.nineworlds.serenity.jobs.RetrieveAllUsersJob
import us.nineworlds.serenity.ui.activity.login.LoginUserContract.LoginUserView
import javax.inject.Inject

@InjectViewState
class LoginUserPresenter : MvpPresenter<LoginUserContract.LoginUserView>(), LoginUserContract.LoginUserPresnter {

  @Inject
  lateinit var logger: Logger

  @Inject
  lateinit var client: SerenityClient

  @Inject
  lateinit var eventBus: EventBus

  @Inject
  lateinit var jobManager: JobManager

  lateinit var server: Server

  init {
    SerenityObjectGraph.instance.inject(this)
  }

  override fun attachView(view: LoginUserView?) {
    super.attachView(view)
    eventBus.register(this)
  }

  override fun detachView(view: LoginUserView?) {
    super.detachView(view)
    eventBus.unregister(this)
  }

  override fun initPresenter(server: Server) {
    this.server = server

    client.updateBaseUrl("http://${server.ipAddress}:8096")
  }

  override fun retrieveAllUsers() {
    jobManager.addJobInBackground(RetrieveAllUsersJob())
  }

  override fun loadUser(user: SerenityUser) {
    jobManager.addJobInBackground(AuthenticateUserJob(user))
  }

  @Subscribe(threadMode = MAIN)
  fun processAllUsersEvent(allUsersEvent: AllUsersEvent) {
    viewState.displayUsers(allUsersEvent.users)
  }

  @Subscribe(threadMode = MAIN)
  fun showUsersStartScreen(authenticatUserEvent: AuthenticatedUserEvent) {
    viewState.launchNextScreen()
  }

}