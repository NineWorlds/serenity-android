package us.nineworlds.serenity.injection

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants

abstract class BaseInjector {

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  protected fun getActivity(contextWrapper: Context): Activity? {
    var context = contextWrapper
    while (context is ContextWrapper) {
      if (context is Activity) {
        return context
      }
      context = context.baseContext
    }
    return null
  }
}
