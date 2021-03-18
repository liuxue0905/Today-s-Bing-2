package com.lx.todaysbing.viewmodels

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.lx.todaysbing.data.BingRepository
import com.lx.todaysbing.data.bing.HPImageArchiveResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class BingViewModel @Inject internal constructor(
    private val repository: BingRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG: String? = BingViewModel::class.java.canonicalName

    private val mkt: MutableStateFlow<String> = MutableStateFlow(
        savedStateHandle.get(MKT_SAVED_STATE_KEY) ?: "zh-CN"
    )

    private val _response: MutableLiveData<HPImageArchiveResponse> = MutableLiveData()
    val response: LiveData<HPImageArchiveResponse>
        get() = _response

    var loadState: MutableLiveData<LoadState> = MutableLiveData(LoadState.NotLoading(false))

    init {
        viewModelScope.launch {
            mkt.collect { newMkt ->
                savedStateHandle.set(MKT_SAVED_STATE_KEY, newMkt)

                Log.d(TAG, "newMkt = $newMkt")

                if (newMkt.isEmpty()) {
                    return@collect
                }

                try {
                    loadState.value = LoadState.Loading
                    _response.value = repository.getHPImageArchive(mkt = newMkt)
                    loadState.value = LoadState.NotLoading(true)
                } catch (exception: Exception) {
                    loadState.value = LoadState.Error(exception)
                } finally {
                    Log.d(TAG, "loadState = $loadState")
                    Log.d(TAG, "_response.value = $_response.value")
                }
            }
        }
    }

    fun setMkt(value: String) {
        Log.d(TAG, "setMkt value = $value")
        Log.d(TAG, "setMkt mkt.value = ${mkt.value}")

//        val oldMkt = mkt.value
//        if (value != oldMkt) {
//            _response.value = null
//        }

        mkt.value = value
    }

    fun getMkt(): String {
        return mkt.value
    }

    companion object {
        private const val MKT_SAVED_STATE_KEY = "MKT_SAVED_STATE_KEY"
    }
}