package us.nineworlds.serenity.core.services

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UpdateProgressRequestJobTest : InjectingTest() {

  private val mockSerenityClient: SerenityClient = mockk(relaxed = true)
  private val mockVideoContentInfo: VideoContentInfo = mockk(relaxed = true)

  @Before
  override fun setUp() {
    super.setUp()
    Dispatchers.setMain(UnconfinedTestDispatcher())
    Toothpick.inject(UpdateProgressRequestJob, scope)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  override fun installTestModules() {
    scope.installModules(TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(SerenityClient::class.java).toInstance(mockSerenityClient)
    }
  }

  @Test
  fun updateProgressUpdatesWatchedStatusWhenVideoIsWatched() = runTest(UnconfinedTestDispatcher()) {
    coEvery { mockVideoContentInfo.id() } returns "1"
    coEvery { mockVideoContentInfo.isWatched } returns true

    UpdateProgressRequestJob.updateProgress(1000, mockVideoContentInfo)

    coVerify(timeout = 2000) { mockSerenityClient.watched("1") }
    coVerify(timeout = 2000) { mockSerenityClient.progress("1", "0") }
  }

  @Test
  fun updateProgressUpdatesProgressWhenVideoIsNotWatched() = runTest(UnconfinedTestDispatcher()){
    coEvery { mockVideoContentInfo.id() } returns "1"
    coEvery { mockVideoContentInfo.isWatched } returns false

    UpdateProgressRequestJob.updateProgress(1000, mockVideoContentInfo)

    coVerify(timeout = 2000) { mockSerenityClient.progress("1", "1000") }
    coVerify(inverse = true, timeout = 1000) { mockSerenityClient.watched(any()) }
  }

  @Test
  fun updateProgressHandlesExceptionGracefully() = runTest(UnconfinedTestDispatcher()){
    coEvery { mockVideoContentInfo.id() } returns "1"
    coEvery { mockVideoContentInfo.isWatched } returns false
    coEvery { mockSerenityClient.progress(any(), any()) } throws RuntimeException("Error")

    UpdateProgressRequestJob.updateProgress(1000, mockVideoContentInfo)

    coVerify(timeout = 2000) { mockSerenityClient.progress("1", "1000") }
  }
}
