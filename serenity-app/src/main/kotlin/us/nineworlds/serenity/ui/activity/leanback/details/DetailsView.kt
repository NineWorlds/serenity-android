package us.nineworlds.serenity.ui.activity.leanback.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.core.model.ContentInfo

interface DetailsView : MvpView {

    @StateStrategyType(AddToEndStrategy::class)
    fun updateDetails(videoInfo: ContentInfo)

}