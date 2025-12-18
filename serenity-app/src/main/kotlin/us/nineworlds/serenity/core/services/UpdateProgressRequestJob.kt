package us.nineworlds.serenity.core.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo
import javax.inject.Inject

object UpdateProgressRequestJob {

  @Inject
  lateinit var serenityClient: SerenityClient

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.IO + job)

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  fun updateProgress(position: Long, video: VideoContentInfo) {
    scope.launch {
      val id = video.id().orEmpty()
      try {
        if (video.isWatched) {
          serenityClient.watched(id)
          serenityClient.progress(id, "0")
        } else {
          serenityClient.progress(id, position.toString())
        }
      } catch (e: Exception) {
        Timber.e(e, "Error updating progress")
      }
    }
  }
}
