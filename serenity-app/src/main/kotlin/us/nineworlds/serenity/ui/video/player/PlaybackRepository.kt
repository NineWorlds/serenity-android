package us.nineworlds.serenity.ui.video.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo

@InjectConstructor
class PlaybackRepository(private val serenityClient: SerenityClient) {

    suspend fun startPlaying(videoId: String): String? = withContext(Dispatchers.IO) {
        val playSessionId = serenityClient.startPlaying(videoId)
        Timber.d("Playback Session Id: ${playSessionId}")
        playSessionId
    }

    suspend fun stopPlaying(videoId: String, offset: Long) = withContext(Dispatchers.IO){
        serenityClient.stopPlaying(videoId, offset)
    }

    suspend fun updatePlaybackPosition(video: VideoContentInfo, playSessionId: String? = null) = withContext(Dispatchers.IO){
        val videoId: String = video.id().orEmpty()
        if (video.isWatched) {
            serenityClient.watched(videoId)
            serenityClient.progress(videoId, "0", playSessionId)
        } else {
            serenityClient.progress(videoId, video.resumeOffset.toString(), playSessionId)
        }
    }

    suspend fun watched(videoId: String) = withContext(Dispatchers.IO) {
        serenityClient.watched(videoId)
    }
}
