package us.nineworlds.serenity.emby.server

import android.content.Context
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import us.nineworlds.serenity.common.android.injection.ApplicationContext
import us.nineworlds.serenity.emby.BuildConfig
import us.nineworlds.serenity.emby.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class EmbyServerJobTest : InjectingTest() {

  @Mock lateinit var mockEventBus: EventBus

  override val modules: List<Any>
    get() = mutableListOf(TestModule())

  lateinit var job: EmbyServerJob

  @Before override fun setUp() {
    initMocks(this)
    super.setUp()
    job = EmbyServerJob()
  }

  @Test
  fun locateEmbyServers() {
    job.onRun()
  }

  @Module(library = true,
      overrides = true,
      injects = arrayOf(EmbyServerJobTest::class, EmbyServerJob::class))
  inner class TestModule {

    @Provides
    fun providesEventBus(): EventBus {
      return mockEventBus
    }

    @Provides
    @ApplicationContext
    fun providesContext(): Context {
      return RuntimeEnvironment.application
    }

  }
}