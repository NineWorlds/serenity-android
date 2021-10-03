package us.nineworlds.serenity.fragments.mainmenu

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.events.MainMenuEvent

interface MainMenuView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadMenu(mainMenuEvent: MainMenuEvent)

}