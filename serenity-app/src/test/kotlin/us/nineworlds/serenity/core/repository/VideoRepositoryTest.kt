package us.nineworlds.serenity.core.repository

import android.content.res.Resources
import assertk.assertThat
import assertk.assertions.isInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
class VideoRepositoryTest : InjectingTest() {

    private val mockClient = mockk<SerenityClient>(relaxed = true)
    private val mockMediaContainer = mockk<IMediaContainer>(relaxed = true)

    private lateinit var repository: VideoRepository

    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = VideoRepository(mockClient)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
        closeScope()
    }

    override fun installTestModules() {
        scope.installModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(SerenityClient::class.java).toInstance(mockClient)
            bind(Resources::class.java).toInstance(mockk(relaxed = true))
        }
    }

    @Test
    fun `fetchEpisodes calls client with paging parameters`() = runTest {
        val itemId = "item1"
        val startIndex = 0
        val limit = 30

        coEvery { mockClient.retrieveEpisodes(itemId, startIndex, limit) } returns mockMediaContainer

        val result = repository.fetchEpisodes(itemId, startIndex, limit)

        assertThat(result).isInstanceOf(List::class)
        coVerify { mockClient.retrieveEpisodes(itemId, startIndex, limit) }
    }

    @Test
    fun `fetchSimilarItems calls client with paging parameters`() = runTest {
        val itemId = "item1"
        val type = Types.MOVIES
        val startIndex = 0
        val limit = 30

        coEvery { mockClient.fetchSimilarItemById(itemId, type, startIndex, limit) } returns mockMediaContainer

        val result = repository.fetchSimilarItems(itemId, type, startIndex, limit)

        assertThat(result).isInstanceOf(IMediaContainer::class)
        coVerify { mockClient.fetchSimilarItemById(itemId, type, startIndex, limit) }
    }

    @Test
    fun `fetchSimilarItemsList calls client with paging parameters`() = runTest {
        val itemId = "item1"
        val type = Types.MOVIES
        val startIndex = 0
        val limit = 30

        coEvery { mockClient.fetchSimilarItemById(itemId, type, startIndex, limit) } returns mockMediaContainer

        val result = repository.fetchSimilarItemsList(itemId, type, startIndex, limit)

        assertThat(result).isInstanceOf(List::class)
        coVerify { mockClient.fetchSimilarItemById(itemId, type, startIndex, limit) }
    }
}
