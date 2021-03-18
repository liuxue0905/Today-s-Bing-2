package com.lx.todaysbing.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lx.todaysbing.data.GalleryRepository
import com.lx.todaysbing.data.opalapi.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject internal constructor(
    private val respository: GalleryRepository,
) : ViewModel() {

    private var currentBodyValue: Map<String, String?>? = null
    private var currentFilterResult: Flow<PagingData<Image>>? = null

    val filters: Map<String, String?>?
        get() = currentBodyValue

    fun filterPictures(body: Map<String, String?>?): Flow<PagingData<Image>> {
        currentBodyValue = body
        val newResult: Flow<PagingData<Image>> =
            respository.getFilterResultStream(body).cachedIn(viewModelScope)
        currentFilterResult = newResult
        return newResult
    }
}