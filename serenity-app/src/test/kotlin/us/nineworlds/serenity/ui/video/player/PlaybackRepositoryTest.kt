package us.nineworlds.serenity.ui.video.player

import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.model.VideoContentInfo

@OptIn(ExperimentalCoroutinesApi::class)
class PlaybackRepositoryTest {

    private companion object {
        private val mockSerenityClient = mockk<SerenityClient>()
    }

    private lateinit var repository: PlaybackRepository

    @Before
    fun setUp() {
        clearAllMocks()
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = PlaybackRepository(mockSerenityClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun startPlayingPlaysExpectedVideoKey() = runTest(UnconfinedTestDispatcher()) {
        val expectedId = "12345"
        every { mockSerenityClient.startPlaying(any()) } returns UUID.randomUUID().toString()

        repository.startPlaying(expectedId)

        verify { mockSerenityClient.startPlaying(expectedId) }
    }

    @Test
    fun stopPlayingStopsExpectedVideoKey() = runTest(UnconfinedTestDispatcher()) {
        val expectedId = "12345"
        every { mockSerenityClient.stopPlaying(any(), any()) } just Runs

        repository.stopPlaying(expectedId, 0)

        verify { mockSerenityClient.stopPlaying(expectedId, 0) }
    }

    @Test
    fun updatePlaybackPositionUpdatesVideoForWatchedVideos() = runTest(UnconfinedTestDispatcher()) {
        val video = mockk<VideoContentInfo>()
        every { video.id() } returns "12345"
        every { video.isWatched } returns true
        every { mockSerenityClient.watched(any()) } returns true
        every { mockSerenityClient.progress(any(), any(), any()) } returns true

        repository.updatePlaybackPosition(video)

        verify { mockSerenityClient.watched("12345") }
        verify { mockSerenityClient.progress("12345", "0", any()) }
    }

    @Test
    fun updatePlaybackPositionUpdatesVideoUnwatchedVideos() = runTest(UnconfinedTestDispatcher()) {
        val video = mockk<VideoContentInfo>()
        every { video.id() } returns "12345"
        every { video.isWatched } returns false
        every { video.resumeOffset } returns 123
        every { mockSerenityClient.progress(any(), any(), any()) } returns true

        repository.updatePlaybackPosition(video)

        verify(exactly = 0) { mockSerenityClient.watched("12345") }
        verify { mockSerenityClient.progress("12345", "123", any()) }
    }
}
