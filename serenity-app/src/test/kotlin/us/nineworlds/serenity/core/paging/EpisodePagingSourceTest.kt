package us.nineworlds.serenity.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.repository.VideoRepository
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodePagingSourceTest : InjectingTest() {

    private val mockRepository = mockk<VideoRepository>(relaxed = true)
    private val mockVideo = mockk<VideoContentInfo>(relaxed = true)

    private lateinit var pagingSource: EpisodePagingSource

    @Before
    override fun setUp() {
        super.setUp()
        pagingSource = EpisodePagingSource(mockRepository, "item1")
    }

    override fun installTestModules() {
        scope.installModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(VideoRepository::class.java).toInstance(mockRepository)
        }
    }

    @Test
    fun `load returns success when repository returns data`() = runTest {
        val episodes = listOf(mockVideo)
        coEvery { mockRepository.fetchEpisodes("item1", 0, 30) } returns episodes

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).isEqualTo(episodes)
        assertThat(page.prevKey).isNull()
        assertThat(page.nextKey).isNull()
    }

    @Test
    fun `load returns nextKey when more episodes are available`() = runTest {
        val episodes = List(30) { mockVideo }
        coEvery { mockRepository.fetchEpisodes("item1", 0, 30) } returns episodes

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.nextKey).isEqualTo(30)
    }

    @Test
    fun `load returns error when repository throws exception`() = runTest {
        val exception = RuntimeException("Failed to fetch episodes")
        coEvery { mockRepository.fetchEpisodes(any(), any(), any()) } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class)
        val error = result as PagingSource.LoadResult.Error
        assertThat(error.throwable).isEqualTo(exception)
    }

    @Test
    fun `getRefreshKey returns correct key`() {
        val state = PagingState<Int, VideoContentInfo>(
            pages = emptyList(),
            anchorPosition = 10,
            config = androidx.paging.PagingConfig(pageSize = 15),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isNull()
    }
}
