package us.nineworlds.serenity.ui.activity.leanback.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import us.nineworlds.serenity.core.model.ContentInfo
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.core.model.VideoContentInfo

interface DetailsView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateDetails(videoInfo: ContentInfo)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addSeasons(videoInfo: List<SeriesContentInfo>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateSeasonEpisodes(season: SeriesContentInfo, episodes: List<VideoContentInfo>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addSimilarItems(videoInfo: List<VideoContentInfo>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addSimilarSeries(videoInfo: List<SeriesContentInfo>)
}