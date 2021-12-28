package us.nineworlds.serenity.injection

import moxy.MvpAppCompatFragment
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants

abstract class InjectingMvpFragment : MvpAppCompatFragment() {

    fun inject() {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
    }
}