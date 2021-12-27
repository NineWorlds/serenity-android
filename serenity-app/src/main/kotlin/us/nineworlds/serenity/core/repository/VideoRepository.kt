package us.nineworlds.serenity.core.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer
import us.nineworlds.serenity.events.SeasonsRetrievalEvent




@InjectConstructor
class VideoRepository constructor(private val client: SerenityClient) {

    suspend fun fetchItemById(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.fetchItemById(itemId)
    }

    suspend fun fetchSeasons(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.retrieveSeasons(itemId)
    }

    suspend fun fetchEpisodes(itemId: String): List<VideoContentInfo> = withContext(Dispatchers.IO) {
        val result = client.retrieveEpisodes(itemId)
        EpisodeMediaContainer(result).createVideos()
    }

}