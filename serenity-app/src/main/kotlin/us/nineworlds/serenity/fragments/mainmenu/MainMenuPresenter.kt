package us.nineworlds.serenity.fragments.mainmenu

import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.fragments.mainmenu.repository.MainMenuRepository
import us.nineworlds.serenity.common.repository.Result

import javax.inject.Inject

@InjectViewState
@StateStrategyType(SkipStrategy::class)
class MainMenuPresenter : MvpPresenter<MainMenuView>() {

    @Inject
    lateinit var repository: MainMenuRepository

    init {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
    }

    fun retrieveMainMenu() {
        presenterScope.launch {
            when (val result = repository.loadMainMenu()) {
                is Result.Success -> viewState.loadMenu(result.data)
                else -> {
                    // need to handle when the server can't communicate
                }
            }
        }

    }

}