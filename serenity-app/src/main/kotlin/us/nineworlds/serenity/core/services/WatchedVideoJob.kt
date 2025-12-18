package us.nineworlds.serenity.core.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.SerenityClient
import javax.inject.Inject

object WatchedVideoJob {

  @Inject
  lateinit var serenityClient: SerenityClient

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.IO + job)

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  fun updateWatchedStatus(videoId: String) {
    scope.launch {
      try {
        Timber.d("Id: $videoId")
        serenityClient.watched(videoId)
      } catch (e: Exception) {
        Timber.e(e, "Error updating watched status")
      }
    }
  }
}
