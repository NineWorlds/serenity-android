package us.nineworlds.serenity.emby.server

import android.content.Context
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import toothpick.config.Module
import us.nineworlds.serenity.common.android.injection.ApplicationContext
import us.nineworlds.serenity.emby.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class EmbyServerJobTest : InjectingTest() {

  @Mock
  lateinit var mockEventBus: EventBus

  override val modules: List<Any>
    get() = mutableListOf(TestModule())

  lateinit var job: EmbyServerJob

  @Before
  override fun setUp() {
    initMocks(this)
    super.setUp()
    job = EmbyServerJob()
    job.eventBus = mockEventBus
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