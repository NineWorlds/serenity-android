package us.nineworlds.serenity.emby.test

import org.junit.Before
import toothpick.Scope
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants

abstract class InjectingTest {

  lateinit var scope: Scope

  @Before
  @Throws(Exception::class)
  open fun setUp() {
    scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
    installModules()
    Toothpick.inject(this, scope)
  }

  abstract fun installModules()

  abstract val modules: List<Any>
}
