package us.nineworlds.serenity.jobs

import com.birbit.android.jobqueue.RetryConstraint
import javax.inject.Inject
import org.greenrobot.eventbus.EventBus
import us.nineworlds.serenity.common.android.injection.InjectingJob
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import us.nineworlds.serenity.events.users.AuthenticatedUserEvent

class AuthenticateUserJob(val user: SerenityUser) : InjectingJob() {

  @Inject lateinit var client: SerenityClient

  var eventBus = EventBus.getDefault()

  override fun onAdded() {

  }

  @Throws(Throwable::class)
  override fun onRun() {
    val authenticatedUser = client.authenticateUser(user)

    eventBus.post(AuthenticatedUserEvent(authenticatedUser))
  }

  override fun onCancel(cancelReason: Int, throwable: Throwable?) {

  }

  override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint? {
    return null
  }
}
