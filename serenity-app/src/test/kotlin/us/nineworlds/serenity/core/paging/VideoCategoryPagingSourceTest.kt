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
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.repository.CategoryRepository
import us.nineworlds.serenity.test.InjectingTest

@OptIn(ExperimentalCoroutinesApi::class)
class VideoCategoryPagingSourceTest : InjectingTest() {

    private val mockRepository = mockk<CategoryRepository>(relaxed = true)
    private val mockVideo = mockk<VideoContentInfo>(relaxed = true)

    private lateinit var pagingSource: VideoCategoryPagingSource

    @Before
    override fun setUp() {
        super.setUp()
        pagingSource = VideoCategoryPagingSource(mockRepository, "cat1", "item1", "movies")
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Toothpick.reset()
    }

    override fun installTestModules() {
        scope.installModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(CategoryRepository::class.java).toInstance(mockRepository)
        }
    }

    @Test
    fun `load returns success when repository returns success`() = runTest {
        val videos = listOf(mockVideo)
        coEvery { mockRepository.fetchItemsByCategory("cat1", "item1", "movies", 0, 30) } returns Result.Success(videos)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).isEqualTo(videos)
        assertThat(page.prevKey).isNull()
        // Since loadSize is 30 and we only returned 1, nextKey should be null
        assertThat(page.nextKey).isNull()
    }

    @Test
    fun `load returns nextKey when more data is available`() = runTest {
        val videos = List(30) { mockVideo }
        coEvery { mockRepository.fetchItemsByCategory("cat1", "item1", "movies", 0, 30) } returns Result.Success(videos)

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
    fun `load returns error when repository returns error`() = runTest {
        val exception = Exception("Failed")
        coEvery { mockRepository.fetchItemsByCategory(any(), any(), any(), any(), any()) } returns Result.Error(exception)

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
