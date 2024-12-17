package us.nineworlds.serenity.emby.server

import android.content.Context
import io.mockk.mockk
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import toothpick.config.Module
import us.nineworlds.serenity.common.android.injection.ApplicationContext
import us.nineworlds.serenity.emby.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class EmbyServerJobTest : InjectingTest() {

  override val modules: List<Any>
    get() = mutableListOf(TestModule())

  private lateinit var job: EmbyServerJob

  @Before
  override fun setUp() {
    ShadowLog.stream = System.out

    super.setUp()
    job = EmbyServerJob()
  }

  @Test
  fun locateEmbyServers() {
    job.onRun()
  }

  override fun installModules() {
    scope.installTestModules(TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(Context::class.java).withName(ApplicationContext::class.java).toInstance(RuntimeEnvironment.application)
    }
  }
}