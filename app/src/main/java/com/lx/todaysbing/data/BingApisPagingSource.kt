package com.lx.todaysbing.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lx.todaysbing.api.BingApisService
import com.lx.todaysbing.data.bingapis.Image

private const val STARTING_PAGE_INDE = 1

class BingApisPagingSource(
    private val service: BingApisService,
    private val form: String,
    private val q: String
) : PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: STARTING_PAGE_INDE
        return try {
            val response = service.search(form = form, q = q)
            var data: List<Image> = emptyList()
            if (response.answers.isNotEmpty()) {
                data = response.answers[0].images
            }
            LoadResult.Page(
                data = data,
                prevKey = if (page == STARTING_PAGE_INDE) null else page - 1,
                nextKey = null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition
    }
}