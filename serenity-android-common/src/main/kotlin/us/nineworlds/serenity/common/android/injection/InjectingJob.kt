package us.nineworlds.serenity.common.android.injection

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import us.nineworlds.serenity.common.injection.SerenityObjectGraph

@Suppress("LeakingThis")
abstract class InjectingJob : Job(Params(500).requireNetwork()) {
  init {
    SerenityObjectGraph.instance.inject(this)
  }
}
