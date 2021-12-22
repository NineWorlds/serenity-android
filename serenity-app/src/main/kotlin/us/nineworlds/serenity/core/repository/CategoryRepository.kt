package us.nineworlds.serenity.core.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.repository.Result.*
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.CategoryMediaContainer
import us.nineworlds.serenity.events.MainMenuEvent
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer

@InjectConstructor
class CategoryRepository constructor(private val client: SerenityClient) {

    suspend fun loadMainMenu(): Result<MainMenuEvent> = withContext(Dispatchers.IO) {
        try {
            val mediaContainer = client.retrieveItemByCategories()
            val event = MainMenuEvent(mediaContainer)
            Success(event)
        } catch (ex: Exception) {
            Error(ex)
        }
    }

    suspend fun retrieveCategories(videoId: String): Result<List<CategoryInfo>> = withContext(Dispatchers.IO) {
        try {
            val result = client.retrieveItemByIdCategory(videoId)
            val categoryMediaContainer = CategoryMediaContainer(result)
            val categories = categoryMediaContainer.createCategories()
            Success(categories)
        } catch (ex: Exception) {
            Error(ex)
        }
    }

    companion object {
        private const val MENU_TYPE_SEARCH = "search"
        private const val MENU_TYPE_SHOW = "show"
        private const val MENU_TYPE_MOVIE = "movie"
        private const val MENU_TYPE_MUSIC = "artist"
        private const val MENU_TYPE_OPTIONS = "options"
        private const val MENU_TYPE_MOVIES = "movies"
        private const val MENU_TYPE_TVSHOWS = "tvshows"
    }


    suspend fun fetchItemsByCategory(categoryId: String, itemId: String, type: String ): Result<List<VideoContentInfo>> = withContext(Dispatchers.IO) {
        try {
            val contentType = when(type) {
                MENU_TYPE_MOVIE, MENU_TYPE_MOVIES -> Types.MOVIES
                MENU_TYPE_SHOW, MENU_TYPE_TVSHOWS -> Types.SERIES
                else -> Types.UNKNOWN
            }
            val result = client.retrieveItemByIdCategory(itemId, categoryId, contentType)
            val movies = MovieMediaContainer(result)
            val posterList: List<VideoContentInfo> = movies.createVideos()
            Success(posterList)
        } catch (ex: Exception) {
           Error(ex)
        }
    }

}