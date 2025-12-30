package us.nineworlds.serenity.injection.modules

import toothpick.config.Module
import us.nineworlds.serenity.MainPresenter

class MainPresenterModule : Module() {

    init {
        bind(MainPresenter::class.java).to(MainPresenter::class.java)
    }
}
