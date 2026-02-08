package us.nineworlds.serenity.ui.activity.leanback.details

import android.content.res.Resources
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.common.media.model.IDirectory
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.media.model.IVideo
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.repository.VideoRepository
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DetailsMVPPresenterTest : InjectingTest() {

    private val mockRepository: VideoRepository = mockk(relaxed = true)
    private val mockView: DetailsView = mockk(relaxed = true)
    private val mockSerenityClient: SerenityClient = mockk(relaxed = true)
    private val mockResources: Resources = mockk(relaxed = true)
    private val mockAndroidHelper: AndroidHelper = mockk(relaxed = true)

    private lateinit var presenter: DetailsMVPPresenter

    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(Dispatchers.Unconfined)
        every { mockSerenityClient.baseURL() } returns "http://localhost:8096/"
        presenter = DetailsMVPPresenter()
        presenter.attachView(mockView)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        Toothpick.reset()
    }

    @Test
    fun `loadItem for tvshows updates details, seasons and episodes`() = runTest(UnconfinedTestDispatcher()) {
        val itemId = "123"
        val type = "tvshows"
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        val mockDirectory = mockk<IDirectory>(relaxed = true)

        every { mockDirectory.key } returns "season1"
        every { mockDirectory.rating } returns "0.0"
        every { mockDirectory.leafCount } returns "0"
        every { mockDirectory.viewedLeafCount } returns "0"
        every { mockMediaContainer.directories } returns listOf(mockDirectory)
        every { mockMediaContainer.size } returns 1
        coEvery { mockRepository.fetchItemById(itemId) } returns mockMediaContainer
        coEvery { mockRepository.fetchSeasons(itemId) } returns mockMediaContainer
        coEvery { mockRepository.fetchEpisodes(any(), any(), any()) } returns emptyList()

        presenter.loadItem(itemId, type)

        advanceUntilIdle()

        verify { mockView.updateDetails(any()) }
        verify { mockView.addSeasons(any()) }
        verify { mockView.updateSeasonEpisodes(any(), any()) }
    }

    @Test
    fun `loadItem for movies updates details and loads similar items`() = runTest(UnconfinedTestDispatcher()) {
        val itemId = "123"
        val type = "movies"
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        val mockVideo = mockk<IVideo>(relaxed = true)

        every { mockVideo.type } returns "movie"
        every { mockVideo.viewOffset } returns 0
        every { mockVideo.duration } returns 0
        every { mockMediaContainer.videos } returns listOf(mockVideo)
        every { mockMediaContainer.size } returns 1
        coEvery { mockRepository.fetchItemById(itemId) } returns mockMediaContainer
        coEvery { mockRepository.fetchSimilarItemsList(any(), any(), any(), any()) } returns emptyList()

        presenter.loadItem(itemId, type)

        advanceUntilIdle()

        verify { mockView.updateDetails(any()) }
        verify { mockView.addSimilarItems(any()) }
    }

    @Test
    fun `loadSimilarItems for tvshows calls addSimilarSeries`() = runTest(UnconfinedTestDispatcher()) {
        val itemId = "123"
        val type = "tvshows"
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        val mockDirectory = mockk<IDirectory>(relaxed = true)

        every { mockDirectory.rating } returns "0.0"
        every { mockDirectory.leafCount } returns "0"
        every { mockDirectory.viewedLeafCount } returns "0"
        every { mockMediaContainer.directories } returns listOf(mockDirectory)
        every { mockMediaContainer.size } returns 1
        coEvery { mockRepository.fetchSimilarItems(itemId, Types.SERIES) } returns mockMediaContainer

        presenter.loadSimilarItems(itemId, type)

        advanceUntilIdle()

        verify { mockView.addSimilarSeries(any()) }
    }

    @Test
    fun `loadSimilarItems for movies calls addSimilarItems`() = runTest(UnconfinedTestDispatcher()) {
        val itemId = "123"
        val type = "movies"

        coEvery { mockRepository.fetchSimilarItemsList(any(), any(), any(), any()) } returns emptyList()

        presenter.loadSimilarItems(itemId, type)

        advanceUntilIdle()

        verify { mockView.addSimilarItems(any()) }
    }

    @Test
    fun `loadItem for unknown type defaults to movie behavior`() = runTest(UnconfinedTestDispatcher()) {
        val itemId = "123"
        val type = "unknown"
        val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)
        val mockVideo = mockk<IVideo>(relaxed = true)

        every { mockVideo.type } returns "movie"
        every { mockVideo.viewOffset } returns 0
        every { mockVideo.duration } returns 0
        every { mockMediaContainer.videos } returns listOf(mockVideo)
        every { mockMediaContainer.size } returns 1
        coEvery { mockRepository.fetchItemById(itemId) } returns mockMediaContainer
        coEvery { mockRepository.fetchSimilarItemsList(any(), any(), any(), any()) } returns emptyList()

        presenter.loadItem(itemId, type)

        advanceUntilIdle()

        verify { mockView.updateDetails(any()) }
        verify { mockView.addSimilarItems(any()) }
    }

    override fun installTestModules() {
        scope.installTestModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(VideoRepository::class.java).toInstance(mockRepository)
            bind(SerenityClient::class.java).toInstance(mockSerenityClient)
            bind(Resources::class.java).toInstance(mockResources)
            bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
        }
    }
}
