package us.nineworlds.serenity.core.services

import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo

object UpdateProgressRequestJob {

    @Inject
    lateinit var serenityClient: SerenityClient

    private var job: Job? = null
    private var scope: CoroutineScope? = null

    init {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
    }

    fun updateProgress(position: Long, video: VideoContentInfo) {
        if (scope == null) {
            job = SupervisorJob()
            scope = CoroutineScope(Dispatchers.IO + job!!)
        }

        scope?.launch {
            val id = video.id()
            if (id == null) {
                Timber.w("Video ID is null. Cannot update progress for video: ${video.id()}")
                return@launch
            }

            try {
                if (video.isWatched) {
                    serenityClient.watched(id)
                    serenityClient.progress(id, "0")
                } else {
                    serenityClient.progress(id, position.toString())
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    return@launch
                }
                Timber.e(e, "Error updating progress")
            }
        }
    }

    fun updateProgress(position: Long, video: VideoContentInfo, playSessionId: String?) {
        if (scope == null) {
            job = SupervisorJob()
            scope = CoroutineScope(Dispatchers.IO + job!!)
        }

        scope?.launch {
            val id = video.id()
            if (id == null) {
                Timber.w("Video ID is null. Cannot update progress for video: ${video.id()}")
                return@launch
            }

            try {
                if (video.isWatched) {
                    serenityClient.watched(id)
                    serenityClient.progress(id, "0", playSessionId)
                } else {
                    serenityClient.progress(id, position.toString(), playSessionId)
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    return@launch
                }
                Timber.e(e, "Error updating progress")
            }
        }
    }

    fun onFinish() {
        scope?.cancel()
        scope = null
        job = null
    }
}
