package us.nineworlds.serenity;

import android.support.v7.widget.RecyclerView;
import us.nineworlds.serenity.injection.SerenityObjectGraph;

public abstract class InjectingRecyclerViewAdapter extends RecyclerView.Adapter {

    public InjectingRecyclerViewAdapter() {
        SerenityObjectGraph.getInstance().inject(this);
    }
}
