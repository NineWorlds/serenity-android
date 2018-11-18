package us.nineworlds.serenity.common.android.injection

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants

@Suppress("LeakingThis")
abstract class InjectingJob : Job(Params(500).requireNetwork()) {
  init {
    val jobScope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
    Toothpick.inject(this, jobScope)
  }

}
