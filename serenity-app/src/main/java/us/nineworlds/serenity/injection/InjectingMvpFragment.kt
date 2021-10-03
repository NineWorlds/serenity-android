package us.nineworlds.serenity.injection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants

abstract class InjectingMvpFragment : MvpAppCompatFragment() {

    fun inject() {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
    }
}