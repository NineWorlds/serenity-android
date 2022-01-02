package us.nineworlds.serenity.fragments.mainmenu

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoCategory
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.events.MainMenuEvent

interface MainMenuView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadMenu(mainMenuEvent: MainMenuEvent)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadCategories(videoCategories: CategoryVideoInfo)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateCategories(category: CategoryInfo, items: List<VideoCategory>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun clearCategories()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoading()

}