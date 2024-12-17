package us.nineworlds.serenity.ui.video.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo

@InjectConstructor
class PlaybackRepository(private val serenityClient: SerenityClient) {

    suspend fun startPlaying(videoId: String) = withContext(Dispatchers.IO) {
        serenityClient.startPlaying(videoId)
    }

    suspend fun stopPlaying(videoId: String, offset: Long) = withContext(Dispatchers.IO){
        serenityClient.stopPlaying(videoId, offset)
    }

    suspend fun updatePlaybackPosition(video: VideoContentInfo) = withContext(Dispatchers.IO){
        val videoId: String = video.id()
        if (video.isWatched()) {
            serenityClient.watched(videoId)
            serenityClient.progress(videoId, "0")
        } else {
            serenityClient.progress(videoId, video.getResumeOffset().toString())
        }
    }

    suspend fun watched(videoId: String) = withContext(Dispatchers.IO) {
        serenityClient.watched(videoId)
    }


}