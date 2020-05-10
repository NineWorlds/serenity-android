package us.nineworlds.serenity.jobs.videos

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.jobs.video.UpdatePlaybackPostionJob
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class UpdatePlaybackPositionJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Mock
  lateinit var mockVideoContentInfo: MoviePosterInfo
  @Inject
  lateinit var mockClient: SerenityClient

  lateinit var job: UpdatePlaybackPostionJob

  val expectedVideoId = RandomStringUtils.randomAlphanumeric(5)

  @Before
  override fun setUp() {
    super.setUp()
    job = UpdatePlaybackPostionJob(mockVideoContentInfo)
  }

  @Test
  fun onRunNotifiesServerThatPlaybackHasStopped() {
    doReturn(expectedVideoId).whenever(mockVideoContentInfo).id()
    doReturn(2000).whenever(mockVideoContentInfo).resumeOffset

    job.onRun()

    verify(mockClient).progress(expectedVideoId, "2000")
  }

  @Test
  fun onRunNotifiesServerThatVideHasBeenWatchedAndResetsResumeOffsetToZero() {
    doReturn(expectedVideoId).whenever(mockVideoContentInfo).id()
    doReturn(true).whenever(mockVideoContentInfo).isWatched

    job.onRun()

    verify(mockClient).watched(expectedVideoId)
    verify(mockClient).progress(expectedVideoId, "0")
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}
