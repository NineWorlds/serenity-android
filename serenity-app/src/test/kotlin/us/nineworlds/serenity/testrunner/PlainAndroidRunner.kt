package us.nineworlds.serenity.testrunner

import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.InitializationError
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.internal.SandboxTestRunner
import org.robolectric.internal.bytecode.Sandbox

import java.lang.reflect.Method

class PlainAndroidRunner @Throws(InitializationError::class)
constructor(testClass: Class<*>) : RobolectricTestRunner(testClass) {

  override fun afterTest(method: FrameworkMethod, bootstrappedMethod: Method?) {}

  @Throws(Throwable::class)
  override fun beforeTest(sandbox: Sandbox, method: FrameworkMethod, bootstrappedMethod: Method?) {
  }

  override fun getHelperTestRunner(bootstrappedTestClass: Class<*>): org.robolectric.internal.SandboxTestRunner.HelperTestRunner {
    try {
      return SandboxTestRunner.HelperTestRunner(bootstrappedTestClass)
    } catch (initializationError: InitializationError) {
      throw RuntimeException(initializationError)
    }
  }

  override fun buildGlobalConfig(): Config {
    return Config.Builder().setManifest(Config.NONE).build()
  }
}