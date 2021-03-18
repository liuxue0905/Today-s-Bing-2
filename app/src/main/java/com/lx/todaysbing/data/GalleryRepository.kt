package com.lx.todaysbing.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lx.todaysbing.api.GalleryService
import com.lx.todaysbing.data.opalapi.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepository @Inject constructor(private val service: GalleryService) {

    fun getFilterResultStream(body: Map<String, String?>?): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { GalleryPagingSource(service, body) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}