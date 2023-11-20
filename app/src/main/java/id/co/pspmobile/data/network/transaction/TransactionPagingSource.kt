package id.co.pspmobile.data.network.transaction

import androidx.paging.PagingSource
import androidx.paging.PagingState

class TransactionPagingSource (
    private val repository: TransactionRepository
) : PagingSource<Int, TransactionResDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionResDto> {
        val page = params.key ?: 0

        return try {
            val entities = repository.getHistoryTopUp(page, params.loadSize)

            // simulate page loading
//            if (page != 0) delay(10000)

            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TransactionResDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}