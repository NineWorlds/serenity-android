package us.nineworlds.serenity.core.services

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
import javax.inject.Inject

object WatchedVideoJob {

  @Inject
  lateinit var serenityClient: SerenityClient

  private var job: Job? = null
  private var scope: CoroutineScope? = null

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  fun updateWatchedStatus(videoId: String) {
    if (scope == null) {
      job = SupervisorJob()
      scope = CoroutineScope(Dispatchers.IO + job!!)
    }

    scope?.launch {
      try {
        Timber.d("Id: $videoId")
        serenityClient.watched(videoId)
      } catch (e: Exception) {
        if (e is CancellationException) {
          // ignore
          return@launch
        }
        Timber.e(e, "Error updating watched status")
      }
    }
  }

  fun onFinish() {
    scope?.cancel()
    scope = null
    job = null
  }
}
