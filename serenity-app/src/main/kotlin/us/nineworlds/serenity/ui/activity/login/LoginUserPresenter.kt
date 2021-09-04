package us.nineworlds.serenity.ui.activity.login

import com.birbit.android.jobqueue.JobManager
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import toothpick.Toothpick
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.events.users.AllUsersEvent
import us.nineworlds.serenity.events.users.AuthenticatedUserEvent
import us.nineworlds.serenity.jobs.AuthenticateUserJob
import us.nineworlds.serenity.jobs.RetrieveAllUsersJob
import us.nineworlds.serenity.ui.activity.login.LoginRepository.Result.*
import us.nineworlds.serenity.ui.activity.login.LoginUserContract.LoginUserView
import javax.inject.Inject

@InjectViewState
@StateStrategyType(SkipStrategy::class)
class LoginUserPresenter : MvpPresenter<LoginUserView>(), LoginUserContract.LoginUserPresnter {

  @Inject
  lateinit var logger: Logger

  @Inject
  lateinit var repository: LoginRepository

  @Inject
  lateinit var client: SerenityClient

  private lateinit var server: Server

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  override fun initPresenter(server: Server) {
    this.server = server

    if (server.port != null) {
      client.updateBaseUrl("http://${server.ipAddress}:${server.port}/")
    } else {
      client.updateBaseUrl("http://${server.ipAddress}:8096/")
    }
  }

  override fun retrieveAllUsers() {
    presenterScope.launch {
      when (val result = repository.loadAllUsers()) {
        is Success<List<SerenityUser>> -> viewState.displayUsers(result.data)
        else -> {
          // Error state
        }
      }
    }
  }

  override fun loadUser(user: SerenityUser) {
    presenterScope.launch {
      when (val result = repository.authenticateUser(user)) {
        is Success<SerenityUser> -> viewState.launchNextScreen()
        else -> {
          // Error State
        }
      }
    }
  }
}