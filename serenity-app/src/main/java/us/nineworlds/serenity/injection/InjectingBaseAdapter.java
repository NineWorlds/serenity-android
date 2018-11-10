package us.nineworlds.serenity.injection;

import android.widget.BaseAdapter;
import us.nineworlds.serenity.common.injection.SerenityObjectGraph;

public abstract class InjectingBaseAdapter extends BaseAdapter {

  public InjectingBaseAdapter() {
    SerenityObjectGraph.Companion.getInstance().inject(this);
  }
}
