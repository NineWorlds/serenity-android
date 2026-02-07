package us.nineworlds.serenity.ui.activity.leanback.details

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer
import us.nineworlds.serenity.core.model.impl.SeasonsMediaContainer
import us.nineworlds.serenity.core.model.impl.SeriesMediaContainer
import us.nineworlds.serenity.core.paging.EpisodePagingSource
import us.nineworlds.serenity.core.paging.SimilarItemsPagingSource
import us.nineworlds.serenity.core.repository.VideoRepository

class DetailsMVPPresenter : MvpPresenter<DetailsView>() {

    @Inject
    lateinit var repository: VideoRepository

    init {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
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
        val itemType = when (type) {
            "tvshows" -> Types.SERIES
            else -> Types.MOVIES
        }

        if (itemType == Types.MOVIES) {
            val pagingFlow = Pager(
                config = PagingConfig(
                    pageSize = 15,
                    initialLoadSize = 30,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    SimilarItemsPagingSource(repository, itemId, itemType)
                }
            ).flow
                .map { pagingData ->
                    pagingData.filter { item -> item.getType() != Types.SERIES }
                }
                .cachedIn(presenterScope)

            presenterScope.launch {
                pagingFlow.collectLatest { pagingData ->
                    viewState.addSimilarItems(pagingData)
                }
            }
        } else {
            presenterScope.launch {
                val result = repository.fetchSimilarItems(itemId, itemType)
                viewState.addSimilarSeries(SeriesMediaContainer(result).createSeries())
            }
        }
    }

    private suspend fun updateSeries(itemId: String) {
        val result = repository.fetchSeasons(itemId)
        val seasons = SeasonsMediaContainer(result).createSeries()
        withContext(Dispatchers.Main) {
            viewState.addSeasons(seasons)
        }

        seasons.forEach { season ->
            val seasonsKey = season.key
            if (seasonsKey.isNullOrEmpty()) return@forEach

            val pagingFlow = Pager(
                config = PagingConfig(
                    pageSize = 15,
                    initialLoadSize = 30,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    EpisodePagingSource(repository, seasonsKey)
                }
            ).flow
                .cachedIn(presenterScope)

            presenterScope.launch {
                pagingFlow.collectLatest { pagingData ->
                    viewState.updateSeasonEpisodes(season, pagingData)
                }
            }
        }
    }
}
