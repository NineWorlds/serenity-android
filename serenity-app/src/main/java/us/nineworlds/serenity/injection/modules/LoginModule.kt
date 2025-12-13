package us.nineworlds.serenity.injection.modules

import toothpick.config.Module
import us.nineworlds.serenity.ui.activity.login.LoginUserPresenter

class LoginModule : Module() {
    init {
        bind(LoginUserPresenter::class.java).to(LoginUserPresenter::class.java)
    }
}
