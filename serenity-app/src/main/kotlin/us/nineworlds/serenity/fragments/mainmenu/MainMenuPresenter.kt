package us.nineworlds.serenity.fragments.mainmenu

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.repository.CategoryRepository
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoCategory

import javax.inject.Inject

@InjectViewState
@StateStrategyType(SkipStrategy::class)
class MainMenuPresenter : MvpPresenter<MainMenuView>() {

    @Inject
    lateinit var repository: CategoryRepository

    private var galleryJob: Job? = null

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

    fun populateMovieCategories(itemId: String, type: String) {
        galleryJob?.let { job ->
            job.cancel()
        }
        galleryJob = presenterScope.launch {
            when (val result = repository.retrieveCategories(itemId)) {
                is Result.Success -> {
                    processesCategories(result.data, itemId, type)
                }
            }
        }
    }

    private suspend fun processesCategories(categories: List<CategoryInfo>, itemId: String, type: String) {
        if (type == "movies" || type == "tv show" || type == "tvshows") {

            val filteredCategories = categories.filter { category -> category.category != "unwatched" }
            val categoryVideoContentInfo = CategoryVideoInfo(categories = filteredCategories)

            withContext(Dispatchers.Main) {
                viewState.loadCategories(categoryVideoContentInfo)
            }

            presenterScope.launch(Dispatchers.IO) {
                filteredCategories.forEach { category ->
                    async {
                        when (val result = repository.fetchItemsByCategory(category.category, itemId, type)) {
                            is Result.Success -> {
                                withContext(Dispatchers.Main) {
                                    val videos = result.data.map { videoContentInfo ->
                                        VideoCategory(
                                                type = getType(type),
                                                item = videoContentInfo)
                                    }
                                    viewState.updateCategories(category, videos)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                viewState.clearCategories()
            }
        }
    }

    private fun getType(type: String): Types = when (type) {
        "movies", "movie" -> Types.MOVIES
        "tvshows" -> Types.SERIES
        else -> Types.UNKNOWN
    }

}