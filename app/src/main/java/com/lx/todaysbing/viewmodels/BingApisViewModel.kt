package com.lx.todaysbing.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lx.todaysbing.data.BingApisRepository
import com.lx.todaysbing.data.bingapis.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BingApisViewModel @Inject internal constructor(
    private val respository: BingApisRepository
) : ViewModel() {

    private var currentFormValue: String? = null
    private var currentQValue: String? = null

    private var currentSearchResult: Flow<PagingData<Image>>? = null

    fun search(form: String, q: String): Flow<PagingData<Image>> {
        currentFormValue = form
        currentQValue = q
        val newResult: Flow<PagingData<Image>> =
            respository.getSearchResultStream(form, q).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}