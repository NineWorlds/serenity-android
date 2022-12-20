package us.nineworlds.serenity.emby.server

import android.content.Context
import com.birbit.android.jobqueue.RetryConstraint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import us.nineworlds.serenity.common.android.injection.ApplicationContext
import us.nineworlds.serenity.common.android.injection.InjectingJob
import javax.inject.Inject

@Deprecated("Use Coroutine enabled EmbyServerDiscover class as pat of an async coroutine")
class EmbyServerJob : InjectingJob() {

  @field:[Inject ApplicationContext]
  lateinit var context: Context

  override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint? {
    return null
  }

  override fun onAdded() {
  }

  override fun onCancel(cancelReason: Int, throwable: Throwable?) {
  }

  override fun onRun() {
    runBlocking {
      launch {
        EmbyServerDiscover().findServers()
      }
    }
  }

}