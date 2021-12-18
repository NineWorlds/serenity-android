package us.nineworlds.serenity.fragments.mainmenu

import com.google.android.material.circularreveal.CircularRevealHelper
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.events.MainMenuEvent

interface MainMenuView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadMenu(mainMenuEvent: MainMenuEvent)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadCategories(categoriesMap: Map<String, List<VideoContentInfo>>)

}