package com.lx.todaysbing.data.bing

import com.google.gson.annotations.SerializedName

data class HPImageArchiveResponse(
    @field:SerializedName("images") val images: List<Image>
)
