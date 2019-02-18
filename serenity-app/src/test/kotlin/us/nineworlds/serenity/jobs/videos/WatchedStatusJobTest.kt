package us.nineworlds.serenity.jobs.videos

import com.nhaarman.mockito_kotlin.verify
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.jobs.video.WatchedStatusJob
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class WatchedStatusJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  lateinit var job: WatchedStatusJob

  val expectedVideoId = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = WatchedStatusJob(expectedVideoId)
  }

  @Test
  fun onRunNotifiesServerThatVideoHasBeenWatched() {
    job.onRun()

    verify(mockClient).watched(expectedVideoId)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}
