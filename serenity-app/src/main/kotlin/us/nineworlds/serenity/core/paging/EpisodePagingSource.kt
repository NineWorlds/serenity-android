package us.nineworlds.serenity.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.repository.VideoRepository

class EpisodePagingSource(
    private val repository: VideoRepository,
    private val itemId: String
) : PagingSource<Int, VideoContentInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoContentInfo> {
        val position = params.key ?: 0
        val loadSize = params.loadSize

        return try {
            val data = repository.fetchEpisodes(itemId, position, loadSize)
            LoadResult.Page(
                data = data,
                prevKey = if (position == 0) null else position - loadSize,
                nextKey = if (data.isEmpty() || data.size < loadSize) null else position + data.size
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideoContentInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}
