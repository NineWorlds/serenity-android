package us.nineworlds.serenity

import java.lang.reflect.Method
import org.robolectric.TestLifecycleApplication
import us.nineworlds.serenity.SerenityApplication

class TestSerenityApplication :
    SerenityApplication(),
    TestLifecycleApplication {

    override fun beforeTest(method: Method) {
    }

    override fun prepareTest(test: Any) {
    }

    override fun afterTest(method: Method) {
    }
}
