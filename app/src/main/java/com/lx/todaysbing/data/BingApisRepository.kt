package com.lx.todaysbing.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lx.todaysbing.api.BingApisService
import com.lx.todaysbing.data.bingapis.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BingApisRepository @Inject constructor(private val service: BingApisService) {

    fun getSearchResultStream(form: String, q: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { BingApisPagingSource(service, form, q) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }
}