package us.nineworlds.serenity.injection;

import android.widget.BaseAdapter;

public abstract class InjectingBaseAdapter extends BaseAdapter {

  public InjectingBaseAdapter() {
    SerenityObjectGraph.getInstance().inject(this);
  }
}
