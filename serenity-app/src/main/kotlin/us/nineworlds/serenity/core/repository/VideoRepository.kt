package us.nineworlds.serenity.core.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient

@InjectConstructor
class VideoRepository constructor(private val client: SerenityClient) {

    suspend fun fetchItemById(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.fetchItemById(itemId)
    }

    fun fetchSeasons(itemId: String) {
        client.retrieveSeasons(itemId)
    }

}