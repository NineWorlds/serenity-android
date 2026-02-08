package us.nineworlds.serenity.core.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer

@InjectConstructor
class VideoRepository constructor(private val client: SerenityClient) {

    suspend fun fetchItemById(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.fetchItemById(itemId)
    }

    suspend fun fetchSeasons(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.retrieveSeasons(itemId)
    }

    suspend fun fetchEpisodes(itemId: String, startIndex: Int = 0, limit: Int? = null): List<VideoContentInfo> = withContext(Dispatchers.IO) {
        val result = client.retrieveEpisodes(itemId, startIndex, limit)
        EpisodeMediaContainer(result).createVideos()
    }

    suspend fun fetchSimilarItems(itemId: String, type: Types, startIndex: Int = 0, limit: Int? = null): IMediaContainer = withContext(Dispatchers.IO) {
        client.fetchSimilarItemById(itemId, type, startIndex, limit)
    }

    suspend fun fetchSimilarItemsList(itemId: String, type: Types, startIndex: Int = 0, limit: Int? = null): List<VideoContentInfo> = withContext(Dispatchers.IO) {
        val result = client.fetchSimilarItemById(itemId, type, startIndex, limit)
        MovieMediaContainer(result).createVideos()
    }
}
