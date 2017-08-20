package us.nineworlds.serenity.injection;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;

public abstract class InjectingJob extends Job {

  public InjectingJob() {
    super(new Params(500).requireNetwork());
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }
}
