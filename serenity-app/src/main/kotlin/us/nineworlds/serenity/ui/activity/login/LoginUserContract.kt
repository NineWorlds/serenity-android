package us.nineworlds.serenity.ui.activity.login

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser

interface LoginUserContract {

  interface LoginUserView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayUsers(serenityUser: List<SerenityUser>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun launchNextScreen()
  }

  interface LoginUserPresnter {

    fun initPresenter(server: Server)

    fun retrieveAllUsers()

    fun loadUser(user: SerenityUser)
  }
}