package us.nineworlds.serenity.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.repository.VideoRepository
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
class SimilarItemsPagingSourceTest : InjectingTest() {

    private val mockRepository = mockk<VideoRepository>(relaxed = true)
    private val mockVideo = mockk<VideoContentInfo>(relaxed = true)

    private lateinit var pagingSource: SimilarItemsPagingSource

    @Before
    override fun setUp() {
        super.setUp()
        pagingSource = SimilarItemsPagingSource(mockRepository, "item1", Types.MOVIES)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        closeScope()
    }

    override fun installTestModules() {
        scope.installModules(object : Module() {
            init {
                bind(VideoRepository::class.java).toInstance(mockRepository)
            }
        })
    }

    @Test
    fun `load returns success when repository returns data`() = runTest {
        val similarItems = listOf(mockVideo)
        coEvery { mockRepository.fetchSimilarItemsList("item1", Types.MOVIES, 0, 30) } returns similarItems

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class)
        val page = result as PagingSource.LoadResult.Page<Int, VideoContentInfo>
        assertThat(page.data).isEqualTo(similarItems)
        assertThat(page.prevKey).isNull()
        assertThat(page.nextKey).isNull()
    }

    @Test
    fun `load returns nextKey when more similar items are available`() = runTest {
        val similarItems = List(30) { mockVideo }
        coEvery { mockRepository.fetchSimilarItemsList("item1", Types.MOVIES, 0, 30) } returns similarItems

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class)
        val page = result as PagingSource.LoadResult.Page<Int, VideoContentInfo>
        assertThat(page.nextKey).isEqualTo(30)
    }

    @Test
    fun `load returns error when repository throws exception`() = runTest {
        val exception = RuntimeException("Failed to fetch similar items")
        coEvery { mockRepository.fetchSimilarItemsList(any(), any(), any(), any()) } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class)
        val error = result as PagingSource.LoadResult.Error<Int, VideoContentInfo>
        assertThat(error.throwable).isEqualTo(exception)
    }

    @Test
    fun `getRefreshKey returns null when anchorPosition is null`() {
        val state = PagingState<Int, VideoContentInfo>(
            pages = emptyList(),
            anchorPosition = null,
            config = androidx.paging.PagingConfig(pageSize = 15),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isNull()
    }

    @Test
    fun `getRefreshKey returns null when anchorPosition is not null but closestPageToPosition is null`() {
        val state = PagingState<Int, VideoContentInfo>(
            pages = emptyList(),
            anchorPosition = 0,
            config = androidx.paging.PagingConfig(pageSize = 15),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isNull()
    }

    @Test
    fun `getRefreshKey returns prevKey plus pageSize when prevKey is present`() {
        val page = PagingSource.LoadResult.Page<Int, VideoContentInfo>(
            data = listOf(mockVideo),
            prevKey = 0,
            nextKey = 30
        )
        val state = PagingState<Int, VideoContentInfo>(
            pages = listOf(page),
            anchorPosition = 0,
            config = androidx.paging.PagingConfig(pageSize = 15),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isEqualTo(15)
    }

    @Test
    fun `getRefreshKey returns nextKey minus pageSize when prevKey is null but nextKey is present`() {
        val page = PagingSource.LoadResult.Page<Int, VideoContentInfo>(
            data = listOf(mockVideo),
            prevKey = null,
            nextKey = 30
        )
        val state = PagingState<Int, VideoContentInfo>(
            pages = listOf(page),
            anchorPosition = 0,
            config = androidx.paging.PagingConfig(pageSize = 15),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isEqualTo(15)
    }

    @Test
    fun `getRefreshKey returns null when both prevKey and nextKey are null`() {
        val page = PagingSource.LoadResult.Page<Int, VideoContentInfo>(
            data = listOf(mockVideo),
            prevKey = null,
            nextKey = null
        )
        val state = PagingState<Int, VideoContentInfo>(
            pages = listOf(page),
            anchorPosition = 0,
            config = androidx.paging.PagingConfig(pageSize = 15),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isNull()
    }
}
