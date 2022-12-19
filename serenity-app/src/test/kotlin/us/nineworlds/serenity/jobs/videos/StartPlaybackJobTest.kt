package us.nineworlds.serenity.jobs.videos

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
import us.nineworlds.serenity.jobs.video.StartPlaybackJob
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class StartPlaybackJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  lateinit var job: StartPlaybackJob

  val expectedVideoId = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = StartPlaybackJob(expectedVideoId)
  }

  @Test
  fun onRunNotifiesServerThatPlaybackHasStarted() {
    doNothing().whenever(mockClient).startPlaying(anyString())
    job.onRun()

    verify(mockClient).startPlaying(expectedVideoId)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}
