package us.nineworlds.serenity.injection;

import android.widget.BaseAdapter;
import toothpick.Toothpick;
import us.nineworlds.serenity.common.annotations.InjectionConstants;

public abstract class InjectingBaseAdapter extends BaseAdapter {

  public InjectingBaseAdapter() {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }
}
