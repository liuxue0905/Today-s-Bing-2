package com.lx.todaysbing.data

import com.lx.todaysbing.api.BingService
import com.lx.todaysbing.data.bing.HPImageArchiveResponse
import javax.inject.Inject

class BingRepository @Inject constructor(private val service: BingService) {

    suspend fun getHPImageArchive(mkt: String): HPImageArchiveResponse {
        val response = service.getHPImageArchive(format = "js", n = 8, mkt = mkt);
        return response
    }
}