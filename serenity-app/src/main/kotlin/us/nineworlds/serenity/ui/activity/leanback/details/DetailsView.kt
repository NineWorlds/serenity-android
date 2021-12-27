package us.nineworlds.serenity.ui.activity.leanback.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.core.model.ContentInfo
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.core.model.VideoContentInfo

interface DetailsView : MvpView {

    @StateStrategyType(AddToEndStrategy::class)
    fun updateDetails(videoInfo: ContentInfo)

    @StateStrategyType(AddToEndStrategy::class)
    fun addSeasons(videoInfo: List<SeriesContentInfo>)

    @StateStrategyType(AddToEndStrategy::class)
    fun updateSeasonEpisodes(season: SeriesContentInfo, episodes: List<VideoContentInfo>)
}