package us.nineworlds.serenity.ui.activity.login

import com.arellomobile.mvp.MvpView
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.rest.SerenityUser

interface LoginUserContract {

  interface LoginUserView : MvpView {

    fun displayUsers(serenityUser: List<SerenityUser>)

    fun launchNextScreen()
  }

  interface LoginUserPresnter {

    fun initPresenter(server: Server)

    fun retrieveAllUsers()

    fun loadUser(user: SerenityUser)
  }

}