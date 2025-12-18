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

object UnWatchVideoJob {

  @Inject
  lateinit var serenityClient: SerenityClient

  private var job: Job? = null
  private var scope: CoroutineScope? = null

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  fun markUnwatched(videoId: String) {
    if (scope == null) {
      job = SupervisorJob()
      scope = CoroutineScope(Dispatchers.IO + job!!)
    }

    scope?.launch {
      try {
        serenityClient.unwatched(videoId)
      } catch (e: Exception) {
        if (e is CancellationException) {
          return@launch
        }
        Timber.e(e, "Error updating unwatched status")
      }
    }
  }

  fun onFinish() {
    scope?.cancel()
    scope = null
    job = null
  }
}
