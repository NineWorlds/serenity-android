package us.nineworlds.serenity.fragments.mainmenu.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.events.MainMenuEvent
import us.nineworlds.serenity.common.repository.Result.*
import us.nineworlds.serenity.common.repository.Result

import us.nineworlds.serenity.common.media.model.IMediaContainer

@InjectConstructor
class MainMenuRepository constructor(private val client: SerenityClient) {

    suspend fun loadMainMenu(): Result<MainMenuEvent> = withContext(Dispatchers.IO) {
        val mediaContainer = client.retrieveItemByCategories()
        val event = MainMenuEvent(mediaContainer)
        Success(event)
    }

}