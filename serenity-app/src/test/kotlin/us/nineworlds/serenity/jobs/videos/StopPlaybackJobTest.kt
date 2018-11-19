package us.nineworlds.serenity.jobs.videos

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.jobs.video.StopPlaybackJob
import us.nineworlds.serenity.test.InjectingTest
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class StopPlaybackJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  lateinit var job: StopPlaybackJob

  val expectedVideoId = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = StopPlaybackJob(expectedVideoId)
  }

  @Test
  fun onRunNotifiesServerThatPlaybackHasStopped() {
    doNothing().whenever(mockClient).stopPlaying(anyString())
    job.onRun()

    verify(mockClient).stopPlaying(expectedVideoId)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}
