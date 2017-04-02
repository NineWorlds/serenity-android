package us.nineworlds.serenity.injection;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;

public abstract class InjectingJob extends Job {

    public InjectingJob() {
        super(new Params(500).requireNetwork());
        SerenityObjectGraph.getInstance().inject(this);
    }
}
