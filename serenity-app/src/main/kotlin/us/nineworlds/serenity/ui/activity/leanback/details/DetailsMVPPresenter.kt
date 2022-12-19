package us.nineworlds.serenity.ui.activity.leanback.details

import kotlinx.coroutines.*
import moxy.MvpPresenter
import moxy.presenterScope
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer
import us.nineworlds.serenity.core.model.impl.SeasonsMediaContainer
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo
import us.nineworlds.serenity.core.repository.VideoRepository
import javax.inject.Inject

class DetailsMVPPresenter : MvpPresenter<DetailsView>() {

    @Inject
    lateinit var repository: VideoRepository

    init {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
    }

    fun loadItem(itemId: String, type: String) {
        presenterScope.launch {
            val result = repository.fetchItemById(itemId)
            when (type) {
                "tvshows" -> {
                    viewState.updateDetails(SeriesMediaContainer(result).createSeries()[0])
                    updateSeries(itemId)
                }
                else -> {
                    viewState.updateDetails(MovieMediaContainer(result).createVideos()[0])
                    loadSimilarItems(itemId, type)
                }
            }
        }
    }

    fun loadSimilarItems(itemId: String, type: String) {
        presenterScope.launch {
            val itemType = when(type) {
                "tvshows" -> Types.SERIES
                else -> Types.MOVIES
            }

            val result = repository.fetchSimilarItems(itemId, itemType)
            when (itemType) {
                Types.MOVIES -> {
                    val videos = MovieMediaContainer(result)
                            .createVideos()
                            .filterNot { item ->
                                item.type == Types.SERIES
                            }

                    viewState.addSimilarItems(videos)
                }
                else ->  viewState.addSimilarSeries(SeriesMediaContainer(result).createSeries())
            }
        }
    }

    private suspend fun updateSeries(itemId: String) {
        val result = repository.fetchSeasons(itemId)
        val seasons = SeasonsMediaContainer(result).createSeries()
        withContext(Dispatchers.Main) {
            viewState.addSeasons(seasons)
        }
        presenterScope.launch(Dispatchers.IO) {
            seasons.forEach { season ->
                async {
                    val result = repository.fetchEpisodes(season.key)
                    withContext(Dispatchers.Main) {
                        viewState.updateSeasonEpisodes(season, result)
                    }
                }
            }
        }
    }

}