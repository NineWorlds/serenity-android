package us.nineworlds.serenity.emby.test

import org.junit.Before
import us.nineworlds.serenity.common.injection.SerenityObjectGraph

abstract class InjectingTest {

  @Before @Throws(Exception::class)
  open fun setUp() {
    val objectGraph = SerenityObjectGraph.instance
    objectGraph.createObjectGraph(modules)
    objectGraph.inject(this)
  }

  abstract val modules: List<Any>
}
