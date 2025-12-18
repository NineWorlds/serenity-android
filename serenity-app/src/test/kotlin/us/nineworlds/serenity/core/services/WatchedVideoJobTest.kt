package us.nineworlds.serenity.core.services

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class WatchedVideoJobTest : InjectingTest() {

    private val mockSerenityClient: SerenityClient = mockk(relaxed = true)

    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        Toothpick.inject(WatchedVideoJob, scope)
    }

    @After
    fun tearDown() {
        WatchedVideoJob.onFinish()
        clearAllMocks()
        Toothpick.reset()
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
    fun updateWatchedStatusCallsSerenityClient() = runTest(UnconfinedTestDispatcher()) {
        coEvery { mockSerenityClient.watched(any()) } returns true

        WatchedVideoJob.updateWatchedStatus("1")

        coVerify(timeout = 2000) { mockSerenityClient.watched("1") }
    }

    @Test
    fun updateWatchedStatusHandlesExceptionGracefully() = runTest(UnconfinedTestDispatcher()) {
        coEvery { mockSerenityClient.watched(any()) } throws RuntimeException("Error")

        WatchedVideoJob.updateWatchedStatus("1")

        coVerify(timeout = 2000) { mockSerenityClient.watched("1") }
    }
}
