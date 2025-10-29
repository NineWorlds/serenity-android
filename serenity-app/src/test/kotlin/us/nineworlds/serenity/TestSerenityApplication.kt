package us.nineworlds.serenity

import org.robolectric.TestLifecycleApplication
import us.nineworlds.serenity.SerenityApplication

import java.lang.reflect.Method

class TestSerenityApplication : SerenityApplication(), TestLifecycleApplication {

    override fun beforeTest(method: Method) {

    }

    override fun prepareTest(test: Any) {

    }

    override fun afterTest(method: Method) {

    }
}