package us.nineworlds.serenity.fragments.mainmenu

import androidx.annotation.UiThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.fragments.mainmenu.repository.MainMenuRepository
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.VideoContentInfo

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

    fun populateMovieCategories(itemId: String, type: String) {
        presenterScope.launch {
            when (val result = repository.retrieveCategories(itemId)) {
                is Result.Success -> {
                    processesCategories(result.data, itemId, type)
                }
                else -> {
                }
            }
        }
    }

    private suspend fun processesCategories(categories: List<CategoryInfo>, itemId: String, type: String) {
        val categoriesMap = mutableMapOf<String, List<VideoContentInfo>>()
        categories.filter { category -> category.category != "unwatched" }
                .forEach { category ->
                    categoriesMap[category.category] = emptyList()
                }
        if (type == "movies" || type == "tv show" || type == "tvshows") {
            categories.filter { category -> category.category != "unwatched" }
                .forEach { category ->
                when (val result = repository.fetchItemsByCategory(category.category, itemId, type)) {
                    is Result.Success -> {
                        if (!result.data.isNullOrEmpty()) {
                            categoriesMap[category.category] = result.data
                        } else {
                            categoriesMap.remove(category.category)
                        }

                        withContext(Dispatchers.Main) {
                            viewState.loadCategories(categoriesMap)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
        viewState.loadCategories(categoriesMap)
    }

}