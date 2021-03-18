package com.lx.todaysbing.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lx.todaysbing.api.GalleryService
import com.lx.todaysbing.data.opalapi.Image
import java.lang.Exception

private const val GALLERY_STARTING_PAGE_INDEX = 1

class GalleryPagingSource(
    private val service: GalleryService,
    private val body: Map<String, String?>?
) : PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: GALLERY_STARTING_PAGE_INDEX
        return try {
            if (body.isNullOrEmpty()) {
                val response = service.random(20)
                LoadResult.Page(
                    data = response,
                    prevKey = if (page == GALLERY_STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (response.isEmpty()) null else page + 1
                )
            } else {
                val response = service.filters((page - 1) * params.loadSize, params.loadSize, body)
                LoadResult.Page(
                    data = response,
                    prevKey = if (page == GALLERY_STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (response.isEmpty()) null else page + 1
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition
    }

}