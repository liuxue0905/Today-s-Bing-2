package com.lx.todaysbing.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class RecyclerViewScrollViewModel : ViewModel() {

    private val TAG = RecyclerViewScrollViewModel::class.java.canonicalName

    var data: MutableLiveData<RecyclerViewScrollData> =
        MutableLiveData(RecyclerViewScrollData())

    fun scrollToPositionWithOffset(position: Int, offset: Int) {
        val value = data.value
        value?.position = position
        value?.offset = offset
        data.value = value
    }
}

data class RecyclerViewScrollData(
    var position: Int = 0,
    var offset: Int = 0,
)