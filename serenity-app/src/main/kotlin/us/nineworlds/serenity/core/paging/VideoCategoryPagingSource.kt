package us.nineworlds.serenity.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.repository.CategoryRepository

class VideoCategoryPagingSource(
    private val repository: CategoryRepository,
    private val categoryId: String,
    private val itemId: String,
    private val type: String
) : PagingSource<Int, VideoContentInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoContentInfo> {
        val position = params.key ?: 0
        val loadSize = params.loadSize

        return when (val result = repository.fetchItemsByCategory(categoryId, itemId, type, position, loadSize)) {
            is Result.Success -> {
                val data = result.data
                LoadResult.Page(
                    data = data,
                    prevKey = if (position == 0) null else position - loadSize,
                    nextKey = if (data.isEmpty() || data.size < loadSize) null else position + data.size
                )
            }
            is Result.Error -> LoadResult.Error(result.exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideoContentInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}
